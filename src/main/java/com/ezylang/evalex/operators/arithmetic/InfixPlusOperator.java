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
package com.ezylang.evalex.operators.arithmetic;

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_ADDITIVE;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.InfixOperator;
import com.ezylang.evalex.parser.Token;

/**
 * Addition of numbers and strings. If one operand is a string, a string concatenation is performed.
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_ADDITIVE)
public class InfixPlusOperator extends AbstractOperator {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token operatorToken, EvaluationValue... operands) {
    EvaluationValue leftOperand = operands[0];
    EvaluationValue rightOperand = operands[1];

    if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
      return new EvaluationValue(
          leftOperand
              .getNumberValue()
              .add(rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
    } else {
      return new EvaluationValue(leftOperand.getStringValue() + rightOperand.getStringValue());
    }
  }
}
