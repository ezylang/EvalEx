/*
  Copyright 2012-2026 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.MapBasedDataAccessor;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Expression#copy(com.ezylang.evalex.data.DataAccessorIfc)}.
 *
 * <p>Verifies the parse-once, evaluate-many pattern: a single parsed {@code Expression} is used as
 * a source to create accessor-bound copies that reuse its AST without re-parsing.
 *
 * @author oswaldo.bapvic.jr
 * @since 3.8.0
 */
class ExpressionCopyWithAccessorTest {

  private static MapBasedDataAccessor accessor(Object... keyValuePairs) {
    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    for (int i = 0; i < keyValuePairs.length; i += 2) {
      accessor.setData(
          (String) keyValuePairs[i],
          EvaluationValue.numberValue(new BigDecimal(keyValuePairs[i + 1].toString())));
    }
    return accessor;
  }

  @Test
  void testCopyWithAccessorEvaluates() throws BaseException {
    Expression source = new Expression("x + y");

    EvaluationValue result = source.copy(accessor("x", 3, "y", 4)).evaluate();

    assertThat(result.getNumberValue()).isEqualByComparingTo("7");
  }

  @Test
  void testCopyReusesAst() throws BaseException {
    Expression source = new Expression("x * 2");
    ASTNode sourceAst = source.getAbstractSyntaxTree();

    Expression copy1 = source.copy(accessor("x", 1));
    Expression copy2 = source.copy(accessor("x", 2));

    // Copies share the exact same AST instance as the source — no re-parsing
    assertThat(copy1.getAbstractSyntaxTree()).isSameAs(sourceAst);
    assertThat(copy2.getAbstractSyntaxTree()).isSameAs(sourceAst);
  }

  @Test
  void testCopyBindsSuppliedAccessorNotConfigSupplier() throws BaseException {
    // Configuration supplies a "default" accessor that must NOT be used by copy(accessor)
    MapBasedDataAccessor defaultAccessor = new MapBasedDataAccessor();
    defaultAccessor.setData("x", EvaluationValue.numberValue(new BigDecimal("999")));

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration().toBuilder()
            .dataAccessorSupplier(() -> defaultAccessor)
            .build();

    Expression source = new Expression("x", config);

    // The explicitly supplied accessor must win
    Expression copy = source.copy(accessor("x", 42));
    assertThat(copy.getDataAccessor()).isNotSameAs(defaultAccessor);
    assertThat(copy.evaluate().getNumberValue()).isEqualByComparingTo("42");
  }

  @Test
  void testReuseWithDifferentValues() throws BaseException {
    Expression source = new Expression("a * b + c");

    assertThat(source.copy(accessor("a", 2, "b", 3, "c", 1)).evaluate().getNumberValue())
        .isEqualByComparingTo("7");
    assertThat(source.copy(accessor("a", 10, "b", 5, "c", 2)).evaluate().getNumberValue())
        .isEqualByComparingTo("52");
  }

  @Test
  void testConstantsMapIsolationBetweenCopies() throws BaseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration().toBuilder()
            .allowOverwriteConstants(true)
            .build();
    Expression source = new Expression("PI", config);

    Expression copy1 = source.copy(new MapBasedDataAccessor());
    Expression copy2 = source.copy(new MapBasedDataAccessor());

    copy1.with("PI", new BigDecimal("999"));

    assertThat(copy1.evaluate().getNumberValue()).isEqualByComparingTo("999");
    assertThat(copy2.evaluate()).isEqualTo(config.getDefaultConstants().get("PI"));
  }

  @Test
  void testWithValuesWorksOnCopy() throws BaseException {
    // Simulates what map/filter/reduce functions do: withValues() on the evaluating expression
    Expression source = new Expression("x + offset");

    Expression copy = source.copy(accessor("x", 10));
    copy.withValues(Map.of("offset", new BigDecimal("5")));

    assertThat(copy.evaluate().getNumberValue()).isEqualByComparingTo("15");
  }

  @Test
  void testFunctionsSeeSuppliedAccessor() throws BaseException {
    AbstractFunction readSecret =
        new AbstractFunction() {
          @Override
          public EvaluationValue evaluate(
              Expression expression, Token functionToken, EvaluationValue... parameterValues) {
            return expression.getDataAccessor().getData("secret");
          }

          @Override
          public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
            return List.of();
          }
        };

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalFunctions(Map.entry("readSecret", readSecret));

    Expression source = new Expression("readSecret()", config);

    assertThat(source.copy(accessor("secret", 42)).evaluate().getNumberValue())
        .isEqualByComparingTo("42");
  }

  @Test
  void testConcurrentCopiesWithDifferentAccessors() throws Exception {
    Expression source = new Expression("value * 2");

    int threadCount = 100;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<BigDecimal>> futures = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int val = i;
      futures.add(
          executor.submit(() -> source.copy(accessor("value", val)).evaluate().getNumberValue()));
    }

    executor.shutdown();
    assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

    for (int i = 0; i < threadCount; i++) {
      assertThat(futures.get(i).get()).isEqualByComparingTo(new BigDecimal(i * 2));
    }
  }

  @Test
  void testConcurrentCopiesWithFunctionAccessingAccessor() throws Exception {
    AbstractFunction doubleInput =
        new AbstractFunction() {
          @Override
          public EvaluationValue evaluate(
              Expression expression, Token functionToken, EvaluationValue... parameterValues) {
            EvaluationValue val = expression.getDataAccessor().getData("input");
            return EvaluationValue.numberValue(val.getNumberValue().multiply(new BigDecimal("2")));
          }

          @Override
          public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
            return List.of();
          }
        };

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalFunctions(Map.entry("doubleIt", doubleInput));

    Expression source = new Expression("doubleIt()", config);

    int threadCount = 50;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<BigDecimal>> futures = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int val = i;
      futures.add(
          executor.submit(() -> source.copy(accessor("input", val)).evaluate().getNumberValue()));
    }

    executor.shutdown();
    assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

    for (int i = 0; i < threadCount; i++) {
      assertThat(futures.get(i).get()).isEqualByComparingTo(new BigDecimal(i * 2));
    }
  }

  @Test
  void testCopyAccessorIsolation() throws BaseException {
    Expression source = new Expression("x");

    MapBasedDataAccessor a1 = new MapBasedDataAccessor();
    a1.setData("x", EvaluationValue.stringValue("hello"));
    MapBasedDataAccessor a2 = new MapBasedDataAccessor();
    a2.setData("x", EvaluationValue.stringValue("world"));

    Expression copy1 = source.copy(a1);
    Expression copy2 = source.copy(a2);

    assertThat(copy1.getDataAccessor()).isSameAs(a1);
    assertThat(copy2.getDataAccessor()).isSameAs(a2);
    assertThat(copy1.evaluate().getStringValue()).isEqualTo("hello");
    assertThat(copy2.evaluate().getStringValue()).isEqualTo("world");
  }

  @Test
  void testEvaluationOrderIndependence() throws BaseException {
    Expression source = new Expression("x");

    Expression copy1 = source.copy(accessor("x", 1));
    Expression copy2 = source.copy(accessor("x", 2));

    // Evaluate in reverse creation order
    assertThat(copy2.evaluate().getNumberValue()).isEqualByComparingTo("2");
    assertThat(copy1.evaluate().getNumberValue()).isEqualByComparingTo("1");
  }

  @Test
  void testNestedFunctions() throws BaseException {
    Expression source = new Expression("MAX(a, MIN(b, c))");

    assertThat(source.copy(accessor("a", 5, "b", 10, "c", 3)).evaluate().getNumberValue())
        .isEqualByComparingTo("5");
  }

  @Test
  void testHighVolumeSequentialCopies() throws BaseException {
    Expression source = new Expression("x * x + 1");

    for (int i = 0; i < 10_000; i++) {
      BigDecimal expected = new BigDecimal(i).multiply(new BigDecimal(i)).add(BigDecimal.ONE);
      assertThat(source.copy(accessor("x", i)).evaluate().getNumberValue())
          .isEqualByComparingTo(expected);
    }
  }

  @Test
  void testCopyDoesNotAffectSourceEvaluate() throws BaseException {
    MapBasedDataAccessor defaultAccessor = new MapBasedDataAccessor();
    defaultAccessor.setData("x", EvaluationValue.numberValue(new BigDecimal("1")));

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration().toBuilder()
            .dataAccessorSupplier(() -> defaultAccessor)
            .build();

    Expression source = new Expression("x", config);

    // A copy with a different accessor must not change how the source evaluates
    assertThat(source.copy(accessor("x", 99)).evaluate().getNumberValue())
        .isEqualByComparingTo("99");
    assertThat(source.evaluate().getNumberValue()).isEqualByComparingTo("1");
  }

  @Test
  void testBooleanAndConditional() throws BaseException {
    Expression source = new Expression("IF(amount > 100, \"high\", \"low\")");

    assertThat(source.copy(accessor("amount", 200)).evaluate().getStringValue()).isEqualTo("high");
    assertThat(source.copy(accessor("amount", 50)).evaluate().getStringValue()).isEqualTo("low");
  }
}
