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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DateTimeFunctionsTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_TIME(2022,10,30) | 2022-10-30T00:00:00Z",
        "DT_DATE_TIME(2022,10,30,11) | 2022-10-30T11:00:00Z",
        "DT_DATE_TIME(2022,10,30,11,50,20) | 2022-10-30T11:50:20Z",
        "DT_DATE_TIME(2022,10,30,11,50,20,30) | 2022-10-30T11:50:20.000000030Z"
      })
  void testDateTime(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_PARSE(\"2022-10-30T11:50:20Z\") | 2022-10-30T11:50:20Z",
        "DT_PARSE(\"2022-10-30T11:50:20.000000030Z\") | 2022-10-30T11:50:20.000000030Z",
        "DT_PARSE(\"30/10/2022 11:50:20\", \"dd/MM/yyyy HH:mm:ss\") | 2022-10-30T11:50:20Z",
        "DT_PARSE(\"30/10/2022\", \"dd/MM/yyyy\") | 2022-10-30T00:00:00Z"
      })
  void testDateTimeParse(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_FORMAT(DT_PARSE(\"2022-10-30T11:50:20Z\")) | 2022-10-30T11:50:20",
        "DT_FORMAT(DT_PARSE(\"2022-10-30T11:50:20.000000030Z\"), \"dd/MM/yyyy\") | 30/10/2022",
        "DT_FORMAT(DT_PARSE(\"2022-10-30T11:50:20.000000030Z\"), \"dd/MM/yyyy HH:mm:ss\") |"
            + " 30/10/2022 11:50:20"
      })
  void testDateTimeFormat(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_EPOCH(DT_PARSE(\"2022-10-30T11:50:20Z\")) | 1667130620000",
        "DT_EPOCH(DT_PARSE(\"1970-01-01T00:00:00Z\")) | 0"
      })
  void testDateTimeToEpoch(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_TIME_EPOCH(1667130620000) | 2022-10-30T11:50:20Z",
        "DT_DATE_TIME_EPOCH(0) | 1970-01-01T00:00:00Z"
      })
  void testDateTimeFromEpoch(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_MILLIS(1667130620000) | PT463091H50M20S",
        "DT_DURATION_MILLIS(0) | PT0S"
      })
  void testDurationFromMillis(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {"DT_DURATION_DAYS(53216) | PT1277184H", "DT_DURATION_DAYS(1) | PT24H"})
  void testDurationFromDays(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_PARSE(\"PT1277184H\") | PT1277184H",
        "DT_DURATION_PARSE(\"P1D\") | PT24H"
      })
  void testDurationParse(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
