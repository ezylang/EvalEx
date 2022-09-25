/*
  Copyright 2012-2022 Udo Klimaschewski

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
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorDecimalPlacesTest extends BaseExpressionEvaluatorTest {

  @Test
  void testDefaultNoRoundingLiteral() throws ParseException, EvaluationException {
    assertThat(evaluate("2.12345")).isEqualTo("2.12345");
  }

  @Test
  void testDefaultNoRoundingVariable() throws ParseException, EvaluationException {
    Expression expression = new Expression("a").with("a", new BigDecimal("2.12345"));

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2.12345");
  }

  @Test
  void testDefaultNoRoundingInfixOperator() throws ParseException, EvaluationException {
    assertThat(evaluate("2.12345+1.54321")).isEqualTo("3.66666");
  }

  @Test
  void testDefaultNoRoundingPrefixOperator() throws ParseException, EvaluationException {
    assertThat(evaluate("-2.12345")).isEqualTo("-2.12345");
  }

  @Test
  void testDefaultNoRoundingFunction() throws ParseException, EvaluationException {
    assertThat(evaluate("SUM(2.12345,1.54321)")).isEqualTo("3.66666");
  }

  @Test
  void testDefaultNoRoundingArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal("1.12345"));
    Expression expression = createExpression("a[0]").with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("1.12345");
  }

  @Test
  void testDefaultNoRoundingStructure() throws ParseException, EvaluationException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal("1.12345"));
          }
        };

    Expression expression = createExpression("a.b").with("a", structure);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("1.12345");
  }

  @Test
  void testCustomRoundingDecimalsLiteral() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(2).build();
    Expression expression = new Expression("2.12345", config);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2.12");
  }

  @Test
  void testCustomRoundingDecimalsVariable() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(2).build();
    Expression expression = new Expression("a", config).with("a", new BigDecimal("2.126"));

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2.13");
  }

  @Test
  void testCustomRoundingDecimalsInfixOperator() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("2.12345+1.54321", config);

    // literals are rounded first, the added and rounded again.
    assertThat(expression.evaluate().getStringValue()).isEqualTo("3.666");
  }

  @Test
  void testCustomRoundingDecimalsPrefixOperator() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("-2.12345", config);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("-2.123");
  }

  @Test
  void testCustomRoundingDecimalsFunction() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Expression expression = new Expression("SUM(2.12345,1.54321)", config);

    // literals are rounded first, the added and rounded again.
    assertThat(expression.evaluate().getStringValue()).isEqualTo("3.666");
  }

  @Test
  void testCustomRoundingDecimalsArray() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    List<BigDecimal> array = List.of(new BigDecimal("1.12345"));
    Expression expression = new Expression("a[0]", config).with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("1.123");
  }

  @Test
  void testCustomRoundingStructure() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().decimalPlacesRounding(3).build();
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal("1.12345"));
          }
        };

    Expression expression = new Expression("a.b", config).with("a", structure);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("1.123");
  }

  @Test
  void testDefaultStripZeros() throws EvaluationException, ParseException {
    Expression expression = new Expression("9.000");
    assertThat(expression.evaluate().getNumberValue()).isEqualTo("9");
  }

  @Test
  void testDoNotStripZeros() throws EvaluationException, ParseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().stripTrailingZeros(false).build();

    Expression expression = new Expression("9.000", config);
    assertThat(expression.evaluate().getNumberValue()).isEqualTo("9.000");
  }
}
