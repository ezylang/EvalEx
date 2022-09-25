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

import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorSimpleVariablesTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSingleStringVariable() throws ParseException, EvaluationException {
    Expression expression = createExpression("a").with("a", "hello");
    EvaluationValue result = expression.evaluate();
    assertThat(result.isStringValue()).isTrue();
    assertThat(result.getStringValue()).isEqualTo("hello");
  }

  @Test
  void testSingleNumberVariable() throws ParseException, EvaluationException {
    Expression expression = createExpression("a").with("a", BigDecimal.valueOf(9));
    EvaluationValue result = expression.evaluate();
    assertThat(result.isNumberValue()).isTrue();
    assertThat(result.getNumberValue()).isEqualTo(BigDecimal.valueOf(9));
  }

  @Test
  void testNumbers() throws ParseException, EvaluationException {
    Expression expression =
        createExpression("(a+b)*(a-b)")
            .with("a", BigDecimal.valueOf(9))
            .and("b", BigDecimal.valueOf(5));
    EvaluationValue result = expression.evaluate();
    assertThat(result.isNumberValue()).isTrue();
    assertThat(result.getNumberValue()).isEqualTo(BigDecimal.valueOf(56));
  }

  @Test
  void testStrings() throws ParseException, EvaluationException {
    Expression expression =
        createExpression("prefix+infix+postfix")
            .with("prefix", "Hello")
            .and("infix", " ")
            .and("postfix", "world");
    EvaluationValue result = expression.evaluate();
    assertThat(result.isStringValue()).isTrue();
    assertThat(result.getStringValue()).isEqualTo("Hello world");
  }

  @Test
  void testStringNumberCombined() throws ParseException, EvaluationException {
    Expression expression =
        createExpression("prefix+infix+postfix")
            .with("prefix", "Hello")
            .and("infix", BigDecimal.valueOf(2))
            .and("postfix", "world");
    EvaluationValue result = expression.evaluate();
    assertThat(result.isStringValue()).isTrue();
    assertThat(result.getStringValue()).isEqualTo("Hello2world");
  }
}
