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

class SumMinMaxArrayTest {

  @Test
  void testSumSingleArray() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("SUM(numbers)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("6");
  }

  @Test
  void testSumMultipleArray() throws EvaluationException, ParseException {
    Integer[] numbers1 = {1, 2, 3};
    Integer[] numbers2 = {4, 5, 6};

    Expression expression =
        new Expression("SUM(numbers1, numbers2)")
            .with("numbers1", numbers1)
            .with("numbers2", numbers2);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("21");
  }

  @Test
  void testSumMixedArrayNumber() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("SUM(numbers, 4)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("10");
  }

  @Test
  void testSumNestedArray() throws EvaluationException, ParseException {
    Integer[][] numbers = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

    Expression expression = new Expression("SUM(numbers)").with("numbers", numbers);
    assertThat(expression.evaluate().getStringValue()).isEqualTo("45");
  }

  @Test
  void testMinSingleArray() throws EvaluationException, ParseException {
    Integer[] numbers = {5, 2, 3};

    Expression expression = new Expression("MIN(numbers)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2");
  }

  @Test
  void testMinMultipleArray() throws EvaluationException, ParseException {
    Integer[] numbers1 = {5, 8, 3};
    Integer[] numbers2 = {9, 2, 6};

    Expression expression =
        new Expression("MIN(numbers1, numbers2)")
            .with("numbers1", numbers1)
            .with("numbers2", numbers2);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2");
  }

  @Test
  void testMinMixedArrayNumberMinNumber() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("MIN(numbers, 0)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("0");
  }

  @Test
  void testMinMixedArrayNumberMinArray() throws EvaluationException, ParseException {
    Integer[] numbers = {8, 2, 3};

    Expression expression = new Expression("MIN(numbers, 7)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("2");
  }

  @Test
  void testMinNestedArray() throws EvaluationException, ParseException {
    Integer[][] numbers = {{4, 5, 6}, {1, 2, 3}, {7, 8, 9}};

    Expression expression = new Expression("MIN(numbers)").with("numbers", numbers);
    assertThat(expression.evaluate().getStringValue()).isEqualTo("1");
  }

  @Test
  void testMaxSingleArray() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 5, 3};

    Expression expression = new Expression("MAX(numbers)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("5");
  }

  @Test
  void testMaxMultipleArray() throws EvaluationException, ParseException {
    Integer[] numbers1 = {5, 2, 3};
    Integer[] numbers2 = {4, 9, 6};

    Expression expression =
        new Expression("MAX(numbers1, numbers2)")
            .with("numbers1", numbers1)
            .with("numbers2", numbers2);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("9");
  }

  @Test
  void testMaxMixedArrayNumberMaxNumber() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 2, 3};

    Expression expression = new Expression("MAX(numbers, 4)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("4");
  }

  @Test
  void testMaxMixedArrayNumberMaxArray() throws EvaluationException, ParseException {
    Integer[] numbers = {1, 9, 3};

    Expression expression = new Expression("MAX(numbers, 4)").with("numbers", numbers);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("9");
  }

  @Test
  void testMaxNestedArray() throws EvaluationException, ParseException {
    Integer[][] numbers = {{1, 2, 3}, {7, 8, 9}, {4, 5, 6}};

    Expression expression = new Expression("MAX(numbers)").with("numbers", numbers);
    assertThat(expression.evaluate().getStringValue()).isEqualTo("9");
  }
}
