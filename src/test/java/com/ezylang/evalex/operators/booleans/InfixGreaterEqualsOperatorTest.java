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
package com.ezylang.evalex.operators.booleans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InfixGreaterEqualsOperatorTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1>=1 : true",
        "2>=1 : true",
        "2.1>=2.0 : true",
        "2.0>=2.0 : true",
        "21.677>=21.678 : false",
        "21.679>=21.678 : true",
        "\"abc\">=\"abc\" : true",
        "\"abd\">=\"abc\" : true",
        "\"abc\">=\"xyz\" : false",
        "\"ABC\">=\"abc\" : false",
        "\"9\">=\"5\" : true",
        "\"9\">=\"9\" : true",
        "-4>=-4 :true",
        "-4>=-5 :true",
        "DT_DATE_NEW(2022,10,30)>=DT_DATE_NEW(2022,10,30) : true",
        "DT_DATE_NEW(2022,10,30)>=DT_DATE_NEW(2022,10,28) : true",
        "DT_DATE_NEW(2022,10,30)>=DT_DATE_NEW(2022,10,31) : false",
        "DT_DURATION_PARSE(\"P2D\")>=DT_DURATION_PARSE(\"PT24H\") : true"
      })
  void testInfixGreaterEqualsLiterals(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixGreaterEqualsVariablesLenient() throws EvaluationException, ParseException {
    Expression expression =
        new Expression("a>=b", TestConfigurationProvider.StandardConfigurationLenient);

    assertThatThrownBy(() -> expression.evaluate())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Can not compare an undefined value");

    assertThat(expression.with("a", "").evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", "Hello").evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", new BigDecimal(-1)).evaluate().getBooleanValue()).isFalse();

    assertThat(expression.with("a", new BigDecimal(0)).evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", new BigDecimal(1)).evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", false).evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", true).evaluate().getBooleanValue()).isTrue();

    assertThat(
            expression
                .with("a", Instant.parse("2026-02-25T00:00:00Z"))
                .evaluate()
                .getBooleanValue())
        .isTrue();

    assertThat(
            expression
                .with("b", Instant.parse("2026-02-25T00:00:00Z"))
                .evaluate()
                .getBooleanValue())
        .isTrue();

    assertThat(expression.with("a", Duration.parse("PT0S")).evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", Duration.parse("PT1H")).evaluate().getBooleanValue()).isTrue();

    assertThat(expression.with("a", Duration.parse("PT-1H")).evaluate().getBooleanValue())
        .isFalse();
  }
}
