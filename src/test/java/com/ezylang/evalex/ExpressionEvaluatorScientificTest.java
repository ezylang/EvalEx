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

import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorScientificTest extends BaseExpressionEvaluatorTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2e3 : 2000 ",
        "2E3 : 2000",
        "1e-10 : 0.0000000001",
        "1E-10 : 0.0000000001",
        "1e+10 : 10000000000",
        "1E+10 : 10000000000",
        "2e10 : 20000000000 ",
        "0.5e2 : 50",
        "0.35E4 + 0.5e1 : 3505",
        "2135E-4 : 0.2135",
        "2135E+4 : 21350000",
        "3.e1: 30",
        "3.e-1: 0.3",
        "3.e+2: 300",
        "3.E1: 30",
        "3.E-1: 0.3",
        "3.E+2: 300"
      })
  void testScientificLiteralsEvaluation(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }
}
