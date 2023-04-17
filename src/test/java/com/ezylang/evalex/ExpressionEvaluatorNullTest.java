/*
  Copyright 2012-2023 Udo Klimaschewski

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
import org.junit.jupiter.api.Test;

public class ExpressionEvaluatorNullTest extends BaseExpressionEvaluatorTest {

  @Test
  void testNullEquals() throws ParseException, EvaluationException {
    Expression expression = createExpression("a == null");
    assertExpressionHasExpectedResult(expression.with("a", null), "true");
    assertExpressionHasExpectedResult(expression.with("a", 99), "false");
  }

  @Test
  void testNullNotEquals() throws ParseException, EvaluationException {
    Expression expression = new Expression("a != null");
    assertExpressionHasExpectedResult(expression.with("a", null), "false");
    assertExpressionHasExpectedResult(expression.with("a", 99), "true");
  }

  @Test
  void testHandleWithIf() throws EvaluationException, ParseException {
    Expression expression1 = createExpression("IF(a != null, a * 5, 1)");
    assertExpressionHasExpectedResult(expression1.with("a", null), "1");
    assertExpressionHasExpectedResult(expression1.with("a", 3), "15");

    Expression expression2 =
        createExpression("IF(a == null, \"Unknown name\", \"The name is \" + a)");
    assertExpressionHasExpectedResult(expression2.with("a", null), "Unknown name");
    assertExpressionHasExpectedResult(expression2.with("a", "Max"), "The name is Max");
  }

  @Test
  void testFailWithNoHandling() {
    Expression expression1 = createExpression("a * 5");
    assertThatThrownBy(() -> expression1.with("a", null).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");

    Expression expression2 = createExpression("FLOOR(a)");
    assertThatThrownBy(() -> expression2.with("a", null).evaluate())
        .isInstanceOf(NullPointerException.class);

    Expression expression3 = createExpression("a > 5");
    assertThatThrownBy(() -> expression3.with("a", null).evaluate())
        .isInstanceOf(NullPointerException.class);
  }

  private void assertExpressionHasExpectedResult(Expression expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertThat(expression.evaluate().getStringValue()).isEqualTo(expectedResult);
  }
}
