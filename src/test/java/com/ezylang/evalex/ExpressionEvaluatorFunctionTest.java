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

import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExpressionEvaluatorFunctionTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSingleParameterFunction() throws ParseException, EvaluationException {
    assertThat(evaluate("FACT(3)")).isEqualTo("6");
  }

  @Test
  void testDualParameterFunction() throws ParseException, EvaluationException {
    assertThat(evaluate("ROUND(3.123, 2)")).isEqualTo("3.12");
  }

  @Test
  void testNestedFunctions() throws ParseException, EvaluationException {
    assertThat(evaluate("FACT(ROUND(3.95, 0))")).isEqualTo("24");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"MAX(5) : 5", "MAX(4,6,3,8) : 8"})
  void testVarArgFunction(String expressionString, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expressionString)).isEqualTo(expectedResult);
  }
}
