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
package com.ezylang.evalex.operators;

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_MULTIPLICATIVE;
import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_UNARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import org.junit.jupiter.api.Test;

class OperatorTest {

  @Test
  void testPrefixOperator() {
    OperatorIfc operator = new CorrectPrefixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(OPERATOR_PRECEDENCE_UNARY);
    assertThat(operator.isLeftAssociative()).isFalse();

    assertThat(operator.isPrefix()).isTrue();
    assertThat(operator.isPostfix()).isFalse();
    assertThat(operator.isInfix()).isFalse();
  }

  @Test
  void testPostfixOperator() {
    OperatorIfc operator = new CorrectPostfixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(88);
    assertThat(operator.isLeftAssociative()).isTrue();

    assertThat(operator.isPrefix()).isFalse();
    assertThat(operator.isPostfix()).isTrue();
    assertThat(operator.isInfix()).isFalse();
  }

  @Test
  void testInfixOperator() {
    OperatorIfc operator = new CorrectInfixOperator();

    assertThat(operator.getPrecedence()).isEqualTo(OPERATOR_PRECEDENCE_MULTIPLICATIVE);
    assertThat(operator.isLeftAssociative()).isTrue();

    assertThat(operator.isPrefix()).isFalse();
    assertThat(operator.isPostfix()).isFalse();
    assertThat(operator.isInfix()).isTrue();
  }

  @Test
  void testThrowsFunctionParameterAnnotationNotFoundException() {

    assertThatThrownBy(DummyAnnotationOperator::new)
        .isInstanceOf(OperatorAnnotationNotFoundException.class)
        .hasMessage(
            "Operator annotation for"
                + " 'com.ezylang.evalex.operators.OperatorTest$DummyAnnotationOperator' not"
                + " found");
  }

  @PrefixOperator(leftAssociative = false)
  private static class CorrectPrefixOperator extends DummyAnnotationOperator {}

  @PostfixOperator(precedence = 88)
  private static class CorrectPostfixOperator extends DummyAnnotationOperator {}

  @InfixOperator(precedence = OPERATOR_PRECEDENCE_MULTIPLICATIVE)
  private static class CorrectInfixOperator extends DummyAnnotationOperator {}

  private static class DummyAnnotationOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.stringValue("OK");
    }
  }
}
