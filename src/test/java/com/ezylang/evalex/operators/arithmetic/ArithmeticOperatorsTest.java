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
package com.ezylang.evalex.operators.arithmetic;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.parser.ParseException;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ArithmeticOperatorsTest extends BaseEvaluationTest {

  @ParameterizedTest
  @ValueSource(
      strings = {
        "3/\"string\"",
        "\"string\"/3",
        "3-\"string\"",
        "\"string\"-3",
        "3%\"string\"",
        "\"string\"%3",
        "3*\"string\"",
        "\"string\"*3",
        "3^\"string\"",
        "\"string\"^3",
        "-\"string\"",
        "+\"string\""
      })
  void testUnsupportedDataType(String expression) {
    assertThatThrownBy(() -> assertExpressionHasExpectedResult(expression, "0"))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1/1 : 1",
        "1/2 : 0.5",
        "1/3 : 0.33333333333333333333333333333333333333333333333333333333333333333333",
        "2/8 : 0.25",
        "8/2 : 4",
        "426376/478 : 892"
      })
  void testInfixDivision(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixDivisionByZero() {
    assertThatThrownBy(() -> assertExpressionHasExpectedResult("3/0", "0"))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Division by zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1-1 : 0",
        "1-0 : 1",
        "0-1 : -1",
        "22-22 : 0",
        "2.8-2.4 : 0.4",
        "1-89282 : -89281",
        "1.54321-1.5432 : 0.00001"
      })
  void testInfixMinusNumber(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_NEW(2022,10,30,11,50,30)-2000 | 2022-10-30T09:50:28Z",
        "DT_DATE_NEW(2022,10,30,11,50,30)-DT_DATE_NEW(2022,10,30,11,50,28) | PT2S",
        "DT_DATE_NEW(2022,10,30,11,50,30)-DT_DURATION_PARSE(\"PT2S\") | 2022-10-30T09:50:28Z",
        "DT_DURATION_PARSE(\"PT5S\")-DT_DURATION_PARSE(\"PT2S\") | PT3S"
      })
  void testInfixMinusDateTime(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        expression,
        expectedResult,
        TestConfigurationProvider.StandardConfigurationWithAdditionalTestOperators.toBuilder()
            .zoneId(ZoneId.of("UTC+2"))
            .build());
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1%1 : 0",
        "0%1 : 0",
        "22%2 : 0",
        "2.8%2 : 0.8",
        "1%89282 : 1",
        "154321%7 : 6",
        "848484%7373 : 589"
      })
  void testInfixModulo(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixModuloByZero() {
    assertThatThrownBy(() -> assertExpressionHasExpectedResult("3%0", "0"))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Division by zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "0*0 : 0",
        "0*1 : 0",
        "1*0 : 0",
        "1*1 : 1",
        "2*2 : 4",
        "17*76 : 1292",
        "1.5*2 : 3",
        "2.125*4 : 8.5"
      })
  void testInfixMultiplication(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "0+0 : 0",
        "0+1 : 1",
        "1+0 : 1",
        "1+1 : 2",
        "2+3 : 5",
        "17+76 : 93",
        "1.5+2 : 3.5",
        "2.125+4.125 : 6.25"
      })
  void testInfixPlusNumbers(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "\"hello\"+\" world\" : hello world",
        "77+\"hello\" : 77hello",
        "\"hello\"+77 : hello77",
        "\"1\"+\"1\" : 11",
        "1+\"1\" : 11",
        "\"1\"+1 : 11"
      })
  void testInfixPlusStrings(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_NEW(2022,10,30,11,50,30)+2000 | 2022-10-30T09:50:32Z",
        "DT_DATE_NEW(2022,10,30,11,50,30)+DT_DURATION_PARSE(\"PT2S\") | 2022-10-30T09:50:32Z",
        "DT_DURATION_PARSE(\"PT5S\")+DT_DURATION_PARSE(\"PT2S\") | PT7S"
      })
  void testInfixPlusDateTime(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        expression,
        expectedResult,
        TestConfigurationProvider.StandardConfigurationWithAdditionalTestOperators.toBuilder()
            .zoneId(ZoneId.of("UTC+2"))
            .build());
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "0^0 : 1",
        "0^2 : 0",
        "2^0 : 1",
        "1^1 : 1",
        "1^2 : 1",
        "2^2 : 4",
        "4^4 : 256",
        "2.3^2.2 : 6.248866394748043436",
        "2^-3 : 0.125",
        "-2^-3 : -0.125",
        "-2^-3 : -0.125"
      })
  void testInfixPower(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"-0 : 0", "-1 : -1", "-2.5 : -2.5", "-987.654 : -987.654"})
  void testPrefixMinus(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"+0 : 0", "+1 : 1", "+2.5 : 2.5", "+987.654 : 987.654"})
  void testPrefixPlus(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
