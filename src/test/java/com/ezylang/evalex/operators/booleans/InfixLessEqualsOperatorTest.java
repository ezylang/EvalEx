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

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InfixLessEqualsOperatorTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1<=1 : true",
        "1<=2 : true",
        "2.0<=2.1 : true",
        "2.0<=2.0 : true",
        "21.678<=21.677 : false",
        "21.678<=21.679 : true",
        "\"abc\"<=\"abc\" : true",
        "\"abc\"<=\"abd\" : true",
        "\"xyz\"<=\"abc\" : false",
        "\"abc\"<=\"ABC\" : false",
        "\"5\"<=\"9\" : true",
        "\"9\"<=\"9\" : true",
        "-4<=-4 :true",
        "-5<=-4 :true",
        "DT_DATE_TIME(2022,10,30)<=DT_DATE_TIME(2022,10,30) : true",
        "DT_DATE_TIME(2022,10,30)<=DT_DATE_TIME(2022,10,28) : false",
        "DT_DATE_TIME(2022,10,30)<=DT_DATE_TIME(2022,10,31) : true",
        "DT_DURATION_PARSE(\"P2D\")<=DT_DURATION_PARSE(\"PT24H\") : false"
      })
  void testInfixLessEqualsLiterals(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
