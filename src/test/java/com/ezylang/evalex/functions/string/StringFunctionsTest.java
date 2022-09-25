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
package com.ezylang.evalex.functions.string;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringFunctionsTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_UPPER(\"\") : ''",
        "STR_UPPER(\"a\") : A",
        "STR_UPPER(\"A\") : A",
        "STR_UPPER(\"AbCdEf\") : ABCDEF",
        "STR_UPPER(\"A1b3C4/?\") : A1B3C4/?",
        "STR_UPPER(\"äöüß\") : ÄÖÜSS"
      })
  void testUpper(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_LOWER(\"\") : ''",
        "STR_LOWER(\"A\") : a",
        "STR_LOWER(\"a\") : a",
        "STR_LOWER(\"AbCdEf\") : abcdef",
        "STR_LOWER(\"A1b3C4/?\") : a1b3c4/?",
        "STR_LOWER(\"ÄÖÜSS\") : äöüss"
      })
  void testLower(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_CONTAINS(\"\", \"\") : true",
        "STR_CONTAINS(\"a\", \"a\") : true",
        "STR_CONTAINS(\"Hello World\", \"Wor\") : true",
        "STR_CONTAINS(\"What a world\", \"what\") : true",
        "STR_CONTAINS(\"What a world\", \"a world\") : true",
        "STR_CONTAINS(\"What a world\", \"moon\") : false",
        "STR_CONTAINS(\"\", \"text\") : false"
      })
  void testContains(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
