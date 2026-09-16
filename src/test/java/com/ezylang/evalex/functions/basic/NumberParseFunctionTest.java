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
package com.ezylang.evalex.functions.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link NumberParseFunction}.
 *
 * @author oswaldo.bapvic.jr
 * @since 3.8.0
 */
class NumberParseFunctionTest extends BaseEvaluationTest {

  @ParameterizedTest(name = "Parsing expression {0} should yield number {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        // --- Standard Fallback Parsers ---
        "NUMBER_PARSE(\"123.45\") | 123.45",
        "NUMBER_PARSE(\"1.23E4\") | 12300",
        "NUMBER_PARSE(\"   789.10   \") | 789.10",

        // --- International Formatting and Locales ---
        "NUMBER_PARSE(\"1.234,56\", \"###,##0.00\", \"de\") | 1234.56",
        "NUMBER_PARSE(\"1,234.56\", \"###,##0.00\", \"en-US\") | 1234.56",
        "NUMBER_PARSE(\"1234,56\", \"###,##0.00\", \"pt-BR\") | 1234.56",

        // --- Advanced Accounting & Financial Formats ---
        "NUMBER_PARSE(\"(1,250.50)\", \"###,##0.00;(###,##0.00)\", \"en-US\") | -1250.50",

        // --- Percentages Handling ---
        "NUMBER_PARSE(\"85.5%\", \"###.0%\", \"en-US\") | 0.855",

        // --- Bypassing Format Position Using NULL ---
        "NUMBER_PARSE(\"456.78\", null, \"fr-FR\") | 456.78"
      })
  void testToNumberSuccessScenarios(String expression, String expectedResult)
      throws EvaluationException, ParseException {

    BigDecimal result =
        new Expression(expression, TestConfigurationProvider.ChicagoConfiguration)
            .evaluate()
            .getNumberValue();

    assertThat(result).isEqualByComparingTo(new BigDecimal(expectedResult));
  }

  @Test
  void testToNumberEnforcesMathContextRounding() throws EvaluationException, ParseException {
    ExpressionConfiguration restrictedConfig =
        TestConfigurationProvider.ChicagoConfiguration.toBuilder()
            .mathContext(new MathContext(4, RoundingMode.HALF_UP))
            .build();

    BigDecimal result =
        new Expression("NUMBER_PARSE(\"1.234567\", \"###.000\", NULL)", restrictedConfig)
            .evaluate()
            .getNumberValue();

    assertThat(result).isEqualByComparingTo(new BigDecimal("1.235"));
  }

  @Test
  void testToNumberThrowsExceptionOnPatternMismatch() {
    Expression expression =
        new Expression(
            "NUMBER_PARSE(\"invalid-digits\", \"###,##0.00\")",
            TestConfigurationProvider.ChicagoConfiguration);

    assertThatThrownBy(expression::evaluate)
        .isInstanceOf(EvaluationException.class)
        .hasMessageContaining("does not match the specified format pattern.");
  }

  @Test
  void testToNumberThrowsExceptionOnInvalidUnformattedString() {
    Expression expression =
        new Expression(
            "NUMBER_PARSE(\"not_a_number\")", TestConfigurationProvider.ChicagoConfiguration);

    assertThatThrownBy(expression::evaluate)
        .isInstanceOf(EvaluationException.class)
        .hasMessageContaining("cannot be safely parsed into a valid number.");
  }

  @Test
  void testToNumberThrowsExceptionOnTooManyParams() {
    Expression expression =
        new Expression("NUMBER_PARSE(\"1\", null, \"pt-BR\", true)", TestConfigurationProvider.ChicagoConfiguration);

    assertThatThrownBy(expression::evaluate)
        .isInstanceOf(EvaluationException.class)
        .hasMessageContaining("Too many parameters");
  }

  @Test
  void testToNumberThrowsExceptionOnNullInput() {
    Expression expression =
        new Expression("NUMBER_PARSE(null)", TestConfigurationProvider.ChicagoConfiguration);

    assertThatThrownBy(expression::evaluate)
        .isInstanceOf(EvaluationException.class)
        .hasMessageContaining("The input string cannot be null");
  }
}
