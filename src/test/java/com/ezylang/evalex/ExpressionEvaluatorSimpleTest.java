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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExpressionEvaluatorSimpleTest extends BaseExpressionEvaluatorTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1 : 1",
        "1+1 : 2",
        "1-1 : 0",
        "1-2 :  -1",
        "5-3-1 : 1",
        "-1+1 : 0",
        "1+ -1 : 0",
        "-1 + -1 : -2",
        "1+2+3 : 6",
        "8-4+1-2+4-2 : 5"
      })
  void testSimpleAdditiveEvaluation(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2/2 : 1",
        "4/2 : 2",
        "2*2 : 4",
        "2*4 : 8",
        "6/3*2 : 4",
        "6*2/3 : 4",
        "-1 * -1 : 1",
        "-2 * 2 : -4",
        "4*2/4 : 2",
        "4*2*3/2/2 : 6"
      })
  void testSimpleMultiplicativeEvaluation(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2+3*2 : 8",
        "2+3*2+2 : 10",
        "2+6/3+1 : 5",
        "2+6/3-1 : 3",
        "1+2+3+4/2+4-2 : 10",
        "2*2*2+4/2 : 10",
        "1*2-2*2/2+8 : 8"
      })
  void testSimpleMixedPrecedence(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "(3) : 3",
        "(2+3)*2 : 10",
        "(2+3)*(2+2) : 20",
        "(2+6)/2+1 : 5",
        "(2+6)/(3-1) : 4",
        "(((1+2)+3)+4)/(2+4-4) : 5",
        "2*2*((2+4)/2) : 12",
        "1*(2-2*2)/2+8 : 7"
      })
  void testSimpleBraces(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "true : true",
        "false : false",
        "NOT(true) : false",
        "NOT(false) : true",
        "true || false : true",
        "true || true : true",
        "true && false : false",
        "true && true : true"
      })
  void testSimpleBooleanExpressions(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }
}
