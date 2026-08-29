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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.MapBasedDataAccessor;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameterDefinition;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
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
 * Tests for {@link PreparedExpression}.
 *
 * @since 3.8
 */
class PreparedExpressionTest {

  @Test
  void testBasicEvaluationWithDataAccessor() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x + y", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    accessor.setData("x", EvaluationValue.numberValue(new BigDecimal("3")));
    accessor.setData("y", EvaluationValue.numberValue(new BigDecimal("4")));

    EvaluationValue result = prepared.newExpression(accessor).evaluate();

    assertThat(result.getNumberValue()).isEqualByComparingTo("7");
  }

  @Test
  void testReuseWithDifferentValues() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("a * b + c", ExpressionConfiguration.defaultConfiguration());

    // First evaluation
    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("a", EvaluationValue.numberValue(new BigDecimal("2")));
    accessor1.setData("b", EvaluationValue.numberValue(new BigDecimal("3")));
    accessor1.setData("c", EvaluationValue.numberValue(new BigDecimal("1")));

    assertThat(prepared.newExpression(accessor1).evaluate().getNumberValue())
        .isEqualByComparingTo("7");

    // Second evaluation — same PreparedExpression, different data
    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("a", EvaluationValue.numberValue(new BigDecimal("10")));
    accessor2.setData("b", EvaluationValue.numberValue(new BigDecimal("5")));
    accessor2.setData("c", EvaluationValue.numberValue(new BigDecimal("2")));

    assertThat(prepared.newExpression(accessor2).evaluate().getNumberValue())
        .isEqualByComparingTo("52");
  }

  @Test
  void testASTIsReused() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x + 1", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("x", EvaluationValue.numberValue(BigDecimal.ONE));

    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("x", EvaluationValue.numberValue(BigDecimal.TEN));

    Expression expr1 = prepared.newExpression(accessor1);
    Expression expr2 = prepared.newExpression(accessor2);

    // Both expressions share the same AST instance
    ASTNode ast1 = expr1.getAbstractSyntaxTree();
    ASTNode ast2 = expr2.getAbstractSyntaxTree();
    assertThat(ast1).isSameAs(ast2).isSameAs(prepared.getAbstractSyntaxTree());
  }

  @Test
  void testConcurrentEvaluations() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("value * 2", ExpressionConfiguration.defaultConfiguration());

    int threadCount = 100;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<BigDecimal>> futures = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int val = i;
      futures.add(
          executor.submit(
              () -> {
                MapBasedDataAccessor accessor = new MapBasedDataAccessor();
                accessor.setData("value", EvaluationValue.numberValue(new BigDecimal(val)));
                return prepared.newExpression(accessor).evaluate().getNumberValue();
              }));
    }

    executor.shutdown();
    assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

    for (int i = 0; i < threadCount; i++) {
      assertThat(futures.get(i).get()).isEqualByComparingTo(new BigDecimal(i * 2));
    }
  }

  @Test
  void testConcurrentEvaluationsWithStringExpressions() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression(
            "name + \" is \" + age", ExpressionConfiguration.defaultConfiguration());

    int threadCount = 50;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<String>> futures = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int val = i;
      futures.add(
          executor.submit(
              () -> {
                MapBasedDataAccessor accessor = new MapBasedDataAccessor();
                accessor.setData("name", EvaluationValue.stringValue("User" + val));
                accessor.setData("age", EvaluationValue.stringValue(String.valueOf(20 + val)));
                return prepared.newExpression(accessor).evaluate().getStringValue();
              }));
    }

    executor.shutdown();
    assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

    for (int i = 0; i < threadCount; i++) {
      assertThat(futures.get(i).get()).isEqualTo("User" + i + " is " + (20 + i));
    }
  }

  @Test
  void testFunctionsReceiveCorrectDataAccessor() throws Exception {
    // Custom function that reads a variable via expression.getDataAccessor()
    AbstractFunction readVarFunction =
        new AbstractFunction() {
          @Override
          public EvaluationValue evaluate(
              Expression expression, Token functionToken, EvaluationValue... parameterValues)
              throws EvaluationException {
            return expression.getDataAccessor().getData("secret");
          }

          @Override
          public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
            return List.of();
          }
        };

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalFunctions(Map.entry("readSecret", readVarFunction));

    PreparedExpression prepared = new PreparedExpression("readSecret()", config);

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    accessor.setData("secret", EvaluationValue.numberValue(new BigDecimal("42")));

    assertThat(prepared.newExpression(accessor).evaluate().getNumberValue())
        .isEqualByComparingTo("42");
  }

  @Test
  void testFunctionsWithConcurrentAccessors() throws Exception {
    // Verifies that each thread's function call resolves the correct accessor
    AbstractFunction doubleVarFunction =
        new AbstractFunction() {
          @Override
          public EvaluationValue evaluate(
              Expression expression, Token functionToken, EvaluationValue... parameterValues)
              throws EvaluationException {
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
            .withAdditionalFunctions(Map.entry("doubleIt", doubleVarFunction));

    PreparedExpression prepared = new PreparedExpression("doubleIt()", config);

    int threadCount = 50;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<BigDecimal>> futures = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int val = i;
      futures.add(
          executor.submit(
              () -> {
                MapBasedDataAccessor accessor = new MapBasedDataAccessor();
                accessor.setData("input", EvaluationValue.numberValue(new BigDecimal(val)));
                return prepared.newExpression(accessor).evaluate().getNumberValue();
              }));
    }

    executor.shutdown();
    assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

    for (int i = 0; i < threadCount; i++) {
      assertThat(futures.get(i).get()).isEqualByComparingTo(new BigDecimal(i * 2));
    }
  }

  @Test
  void testWithValuesWorksOnExpressionFromPrepared() throws Exception {
    // Tests that withValues() (used by map/filter functions) works correctly
    PreparedExpression prepared =
        new PreparedExpression("x + offset", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    accessor.setData("x", EvaluationValue.numberValue(new BigDecimal("10")));

    Expression expr = prepared.newExpression(accessor);
    // Simulate what map/filter functions do internally
    expr.withValues(Map.of("offset", new BigDecimal("5")));

    assertThat(expr.evaluate().getNumberValue()).isEqualByComparingTo("15");
  }

  @Test
  void testWithValuesDoesNotAffectOtherInstances() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x + y", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("x", EvaluationValue.numberValue(new BigDecimal("1")));
    accessor1.setData("y", EvaluationValue.numberValue(new BigDecimal("1")));

    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("x", EvaluationValue.numberValue(new BigDecimal("100")));
    accessor2.setData("y", EvaluationValue.numberValue(new BigDecimal("100")));

    Expression expr1 = prepared.newExpression(accessor1);
    Expression expr2 = prepared.newExpression(accessor2);

    // Modifying constants on expr1 must not affect expr2
    expr1.with("x", new BigDecimal("999"));

    assertThat(expr2.evaluate().getNumberValue()).isEqualByComparingTo("200");
  }

  @Test
  void testDefaultAccessorFromConfiguration() throws Exception {
    MapBasedDataAccessor defaultAccessor = new MapBasedDataAccessor();
    defaultAccessor.setData("x", EvaluationValue.numberValue(new BigDecimal("7")));

    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration().toBuilder()
            .dataAccessorSupplier(() -> defaultAccessor)
            .build();

    PreparedExpression prepared = new PreparedExpression("x * 3", config);

    // newExpression() without accessor uses the configuration's supplier
    assertThat(prepared.newExpression().evaluate().getNumberValue()).isEqualByComparingTo("21");
  }

  @Test
  void testParseExceptionOnInvalidExpression() {
    assertThatThrownBy(
            () ->
                new PreparedExpression(
                    "(((invalid", ExpressionConfiguration.defaultConfiguration()))
        .isInstanceOf(ParseException.class);
  }

  @Test
  void testParseExceptionOnEmptyExpression() {
    assertThatThrownBy(
            () -> new PreparedExpression("", ExpressionConfiguration.defaultConfiguration()))
        .isInstanceOf(ParseException.class);
  }

  @Test
  void testGetExpressionString() throws Exception {
    String expr = "a + b * c";
    PreparedExpression prepared =
        new PreparedExpression(expr, ExpressionConfiguration.defaultConfiguration());

    assertThat(prepared.getExpressionString()).isEqualTo(expr);
  }

  @Test
  void testGetConfiguration() throws Exception {
    ExpressionConfiguration config = ExpressionConfiguration.defaultConfiguration();
    PreparedExpression prepared = new PreparedExpression("1 + 1", config);

    assertThat(prepared.getConfiguration()).isSameAs(config);
  }

  @Test
  void testGetAbstractSyntaxTreeNotNull() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x + 1", ExpressionConfiguration.defaultConfiguration());

    assertThat(prepared.getAbstractSyntaxTree()).isNotNull();
  }

  @Test
  void testBooleanExpression() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x > 10 && y < 5", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    accessor.setData("x", EvaluationValue.numberValue(new BigDecimal("15")));
    accessor.setData("y", EvaluationValue.numberValue(new BigDecimal("3")));

    assertThat(prepared.newExpression(accessor).evaluate().getBooleanValue()).isTrue();
  }

  @Test
  void testConditionalExpression() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression(
            "IF(amount > 100, \"high\", \"low\")", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("amount", EvaluationValue.numberValue(new BigDecimal("200")));
    assertThat(prepared.newExpression(accessor1).evaluate().getStringValue()).isEqualTo("high");

    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("amount", EvaluationValue.numberValue(new BigDecimal("50")));
    assertThat(prepared.newExpression(accessor2).evaluate().getStringValue()).isEqualTo("low");
  }

  @Test
  void testDefaultConfigurationConstructor() throws Exception {
    PreparedExpression prepared = new PreparedExpression("2 + 3");

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    assertThat(prepared.newExpression(accessor).evaluate().getNumberValue())
        .isEqualByComparingTo("5");
  }

  @Test
  void testConstantsAreAvailable() throws Exception {
    // EvalEx default constants (TRUE, FALSE, PI, E, etc.) should work
    ExpressionConfiguration config = ExpressionConfiguration.defaultConfiguration();
    PreparedExpression prepared = new PreparedExpression("PI", config);

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    EvaluationValue result = prepared.newExpression(accessor).evaluate();

    // Compare against the actual PI constant from the configuration
    BigDecimal expectedPI = config.getDefaultConstants().get("PI").getNumberValue();
    assertThat(result.getNumberValue()).isEqualByComparingTo(expectedPI);
  }

  @Test
  void testHighVolumeSequentialEvaluations() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x * x + 1", ExpressionConfiguration.defaultConfiguration());

    for (int i = 0; i < 10_000; i++) {
      MapBasedDataAccessor accessor = new MapBasedDataAccessor();
      accessor.setData("x", EvaluationValue.numberValue(new BigDecimal(i)));

      BigDecimal expected = new BigDecimal(i).multiply(new BigDecimal(i)).add(BigDecimal.ONE);
      assertThat(prepared.newExpression(accessor).evaluate().getNumberValue())
          .isEqualByComparingTo(expected);
    }
  }

  @Test
  void testExpressionIndependenceAfterEvaluation() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("x", EvaluationValue.numberValue(new BigDecimal("1")));
    Expression expr1 = prepared.newExpression(accessor1);

    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("x", EvaluationValue.numberValue(new BigDecimal("2")));
    Expression expr2 = prepared.newExpression(accessor2);

    // Evaluate in reverse order — expr2 first
    assertThat(expr2.evaluate().getNumberValue()).isEqualByComparingTo("2");
    assertThat(expr1.evaluate().getNumberValue()).isEqualByComparingTo("1");
  }

  @Test
  void testNestedFunctionCalls() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("MAX(a, MIN(b, c))", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor = new MapBasedDataAccessor();
    accessor.setData("a", EvaluationValue.numberValue(new BigDecimal("5")));
    accessor.setData("b", EvaluationValue.numberValue(new BigDecimal("10")));
    accessor.setData("c", EvaluationValue.numberValue(new BigDecimal("3")));

    assertThat(prepared.newExpression(accessor).evaluate().getNumberValue())
        .isEqualByComparingTo("5");
  }

  @Test
  void testDataAccessorIsolationBetweenExpressions() throws Exception {
    PreparedExpression prepared =
        new PreparedExpression("x", ExpressionConfiguration.defaultConfiguration());

    MapBasedDataAccessor accessor1 = new MapBasedDataAccessor();
    accessor1.setData("x", EvaluationValue.stringValue("hello"));

    MapBasedDataAccessor accessor2 = new MapBasedDataAccessor();
    accessor2.setData("x", EvaluationValue.stringValue("world"));

    Expression expr1 = prepared.newExpression(accessor1);
    Expression expr2 = prepared.newExpression(accessor2);

    // Each expression sees its own accessor
    assertThat(expr1.getDataAccessor()).isSameAs(accessor1);
    assertThat(expr2.getDataAccessor()).isSameAs(accessor2);

    assertThat(expr1.evaluate().getStringValue()).isEqualTo("hello");
    assertThat(expr2.evaluate().getStringValue()).isEqualTo("world");
  }
}
