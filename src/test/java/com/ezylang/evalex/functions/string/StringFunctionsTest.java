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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
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
        "STR_CONTAINS(null, \"a\") : false",
        "STR_CONTAINS(\"abc\", null) : false",
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

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_STARTS_WITH(\"\", \"\") : true",
        "STR_STARTS_WITH(\"a\", \"a\") : true",
        "STR_STARTS_WITH(\"Hello World\", \"Hello\") : true",
        "STR_STARTS_WITH(\"Hello World\", \"hello\") : false",
        "STR_STARTS_WITH(\"Hello world\", \"text\") : false",
        "STR_STARTS_WITH(\"\", \"text\") : false"
      })
  void testStartsWith(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_ENDS_WITH(\"\", \"\") : true",
        "STR_ENDS_WITH(\"a\", \"a\") : true",
        "STR_ENDS_WITH(\"Hello World\", \"World\") : true",
        "STR_ENDS_WITH(\"Hello World\", \"world\") : false",
        "STR_ENDS_WITH(\"Hello world\", \"text\") : false",
        "STR_ENDS_WITH(\"\", \"text\") : false"
      })
  void testEndsWith(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_FORMAT(\"True False %b %b\", true, false) : True False true false",
        "STR_FORMAT(\"Welcome to %s!\", \"EvalEx\") : Welcome to EvalEx!",
        "STR_FORMAT(\"%s is %.2f\", \"Result\", 11.1) : Result is 11.10",
        "STR_FORMAT(\"%1$s_%3$s_%2$s\", \"foo\", \"baz\", \"bar\") : foo_bar_baz",
        "STR_FORMAT(\"%03.0f\", 1) : 001",
        "STR_FORMAT(\"No format\") : No format",
      })
  void testFormat(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "STR_FORMAT(\"Float %.2f\", 11.1) | Float 11,10",
        "STR_FORMAT(\"Float %.4f\", 12345678.987) | Float 12345678,9870",
        "STR_FORMAT(\"%TD\", DT_DATE_NEW(2024, 4, 21)) | 04/21/24",
        "STR_FORMAT(\"%Tc\", DT_DATE_NEW(2024, 4, 21, 11, 35, 59)) | SO. APR. 21 11:35:59 MESZ"
            + " 2024",
      })
  void testFormatBerlin(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        expression, expectedResult, TestConfigurationProvider.GermanConfiguration);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "STR_FORMAT(\"Float %.2f\", 11.1) | Float 11.10",
        "STR_FORMAT(\"Float %.4f\", 12345678.987) | Float 12345678.9870",
        "STR_FORMAT(\"%TD\", DT_DATE_NEW(2024, 4, 21)) | 04/21/24",
        "STR_FORMAT(\"%Tc\", DT_DATE_NEW(2024, 4, 21, 11, 35, 59)) | SUN APR 21 11:35:59 CDT 2024",
      })
  void testFormatChicago(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        expression, expectedResult, TestConfigurationProvider.ChicagoConfiguration);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_TRIM(\" Two whitespace \") : Two whitespace",
        "STR_TRIM(\" Left whitespace\") : Left whitespace",
        "STR_TRIM(\"Right whitespace  \") : Right whitespace",
        "STR_TRIM(\"No whitespace\") : No whitespace"
      })
  void testTrimString(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_LENGTH(\"\") : 0",
        "STR_LENGTH(\"a\") : 1",
        "STR_LENGTH(\"AbCdEf\") : 6",
        "STR_LENGTH(\"A1b3C4/?\") : 8",
        "STR_LENGTH(\"äöüß\") : 4"
      })
  void testLength(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_MATCHES(\"\", \"\") : true",
        "STR_MATCHES(\"a\", \"a\") : true",
        "STR_MATCHES(\"Hello World\", \"Hello\") : false",
        "STR_MATCHES(\"Hello World\", \"hello\") : false",
        "STR_MATCHES(\"Hello world\", \"text\") : false",
        "STR_MATCHES(\"\", \"text\") : false",
        "STR_MATCHES(\"Hello World\", \".*World\") : true",
        "STR_MATCHES(\"Hello World\", \".*world\") : false",
      })
  void testMatches(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_SPLIT(\"Hello World\", \" \")[0] : Hello",
        "STR_SPLIT(\"Hello World\", \" \")[1] : World",
        "STR_SPLIT(\"myFile.json\", \".\")[0] : myFile",
        "STR_SPLIT(\"myFile.json\", \".\")[1] : json",
        "STR_SPLIT(\"Hello World\", \"_\")[0] : Hello World"
      })
  void testSplit(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_SUBSTRING(\"\", 0, 0) : ''",
        "STR_SUBSTRING(\"Hello World\", 0) : Hello World",
        "STR_SUBSTRING(\"Hello World\", 6) : World",
        "STR_SUBSTRING(\"Hello World\", 0, 5) : Hello",
        "STR_SUBSTRING(\"Hello World\", 6, 11) : World",
        "STR_SUBSTRING(\"Hello World\", 6, 6) : ''",
        "STR_SUBSTRING(\"Hello World\", 0, 11) : Hello World",
        "STR_SUBSTRING(\"Hello World\", 0, 12) : Hello World",
      })
  void testSubstring(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testSubstringEndLessThanStart() {
    assertThatThrownBy(() -> evaluate("STR_SUBSTRING(\"Hello World\", 6, 5)"))
        .isInstanceOf(EvaluationException.class)
        .hasMessage("End index must be greater than or equal to start index");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_LEFT(\"\", 0) : ''",
        "STR_LEFT(\"Hello World\", 0) : ''",
        "STR_LEFT(\"Hello World\", 5) : Hello",
        "STR_LEFT(\"Hello World\", 20) : Hello World"
      })
  void testLeftString(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "STR_RIGHT(\"\", 0) : ''",
        "STR_RIGHT(\"Hello World\", 0) : ''",
        "STR_RIGHT(\"Hello World\", 5) : World",
        "STR_RIGHT(\"Hello World\", 20) : Hello World"
      })
  void testRightString(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
