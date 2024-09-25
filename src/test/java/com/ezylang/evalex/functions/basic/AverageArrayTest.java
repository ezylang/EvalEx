/*
  Copyright 2012-2024 Udo Klimaschewski

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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the AVERAGE function with arrays.
 *
 * @author oswaldo.bapvic.jr
 */
class AverageArrayTest {

  @Test
  void testAverageSingleArray() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("AVERAGE(numbers)").with("numbers", numbers);

    assertThat(expression.evaluate().getNumberValue().doubleValue()).isEqualTo(2);
  }

  @Test
  void testAverageMultipleArray() throws EvaluationException, ParseException {
    Integer[] numbers1 = {1, 2, 3};
    Integer[] numbers2 = {4, 5, 6};

    Expression expression =
        new Expression("AVERAGE(numbers1, numbers2)")
            .with("numbers1", numbers1)
            .with("numbers2", numbers2);

    assertThat(expression.evaluate().getNumberValue().doubleValue()).isEqualTo(3.5);
  }

  @Test
  void testAverageMixedArrayNumber() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("AVERAGE(numbers, 4)").with("numbers", numbers);

    assertThat(expression.evaluate().getNumberValue().doubleValue()).isEqualTo(2.5);
  }

  @Test
  void testAverageNestedArray() throws EvaluationException, ParseException {
    Integer[][] numbers = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

    Expression expression = new Expression("AVERAGE(numbers)").with("numbers", numbers);
    assertThat(expression.evaluate().getNumberValue().doubleValue()).isEqualTo(5);
  }
}
