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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorArrayTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSimpleArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal(99));
    Expression expression = createExpression("a[0]").with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("99");
  }

  @Test
  void testMultipleEntriesArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = Arrays.asList(new BigDecimal(2), new BigDecimal(4), new BigDecimal(6));
    Expression expression = createExpression("a[0]+a[1]+a[2]").with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("12");
  }

  @Test
  void testExpressionArray() throws ParseException, EvaluationException {
    List<BigDecimal> array = List.of(new BigDecimal(3));
    Expression expression = createExpression("a[4-x]").with("a", array).and("x", new BigDecimal(4));

    assertThat(expression.evaluate().getStringValue()).isEqualTo("3");
  }

  @Test
  void testNestedArray() throws ParseException, EvaluationException {
    List<BigDecimal> arrayA = List.of(new BigDecimal(3));
    List<BigDecimal> arrayB =
        Arrays.asList(new BigDecimal(2), new BigDecimal(4), new BigDecimal(6));
    Expression expression =
        createExpression("a[b[6-4]-x]")
            .with("a", arrayA)
            .and("b", arrayB)
            .and("x", new BigDecimal(6));

    assertThat(expression.evaluate().getStringValue()).isEqualTo("3");
  }

  @Test
  void testStringArray() throws ParseException, EvaluationException {
    List<String> array = Arrays.asList("Hello", "beautiful", "world");
    Expression expression = createExpression("a[0] + \" \" + a[1] + \" \" + a[2]").with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("Hello beautiful world");
  }

  @Test
  void testBooleanArray() throws ParseException, EvaluationException {
    List<Boolean> array = Arrays.asList(true, true, false);
    Expression expression = createExpression("a[0] + \" \" + a[1] + \" \" + a[2]").with("a", array);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("true true false");
  }

  @Test
  void testArrayOfArray() throws EvaluationException, ParseException {
    List<BigDecimal> subArray1 = Arrays.asList(new BigDecimal(1), new BigDecimal(2));
    List<BigDecimal> subArray2 = Arrays.asList(new BigDecimal(4), new BigDecimal(8));

    List<List<BigDecimal>> array = Arrays.asList(subArray1, subArray2);

    Expression expression1 = createExpression("a[0][0]").with("a", array);
    Expression expression2 = createExpression("a[0][1]").with("a", array);
    Expression expression3 = createExpression("a[1][0]").with("a", array);
    Expression expression4 = createExpression("a[1][1]").with("a", array);

    assertThat(expression1.evaluate().getStringValue()).isEqualTo("1");
    assertThat(expression2.evaluate().getStringValue()).isEqualTo("2");
    assertThat(expression3.evaluate().getStringValue()).isEqualTo("4");
    assertThat(expression4.evaluate().getStringValue()).isEqualTo("8");
  }

  @Test
  void testMixedArray() throws ParseException, EvaluationException {
    List<?> array = Arrays.asList("Hello", new BigDecimal(4), true);
    Expression expression1 = createExpression("a[0]").with("a", array);
    Expression expression2 = createExpression("a[1]").with("a", array);
    Expression expression3 = createExpression("a[2]").with("a", array);

    assertThat(expression1.evaluate().getStringValue()).isEqualTo("Hello");
    assertThat(expression2.evaluate().getStringValue()).isEqualTo("4");
    assertThat(expression3.evaluate().getStringValue()).isEqualTo("true");
  }

  @Test
  void testArrayAndList() throws EvaluationException, ParseException {
    Expression expression =
        createExpression("values[i-1] * factors[i-1]")
            .with("values", List.of(2, 3, 4))
            .and("factors", new Object[] {2, 4, 6})
            .and("i", 1);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("4");
  }

  @Test
  void testArrayTypes() throws EvaluationException, ParseException {
    Expression expression =
        createExpression("decimals[1] + integers[1] + doubles[1] + strings[1] + booleans[1]")
            .with("decimals", new BigDecimal[] {new BigDecimal(1), new BigDecimal(2)})
            .and("integers", new Integer[] {1, 2})
            .and("doubles", new Double[] {1.1, 2.2})
            .and("strings", new String[] {" Hello ", " World "})
            .and("booleans", new Boolean[] {true, false});

    assertThat(expression.evaluate().getStringValue()).isEqualTo("6.2 World false");
  }

  @Test
  void testThrowsUnsupportedDataTypeForArray() {
    assertThatThrownBy(() -> createExpression("a[0]").with("a", "aString").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }

  @Test
  void testThrowsUnsupportedDataTypeForIndex() {
    assertThatThrownBy(
            () -> {
              List<?> array = List.of("Hello");
              createExpression("a[b]").with("a", array).and("b", "anotherString").evaluate();
            })
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }
}
