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

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_POWER;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.InfixOperator;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Power of operator, calculates the power of right operand of left operand. The precedence is read
 * from the configuration during parsing.
 *
 * @see #getPrecedence(ExpressionConfiguration)
 */
@InfixOperator(precedence = OPERATOR_PRECEDENCE_POWER, leftAssociative = false)
public class InfixPowerOfOperator extends AbstractOperator {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token operatorToken, EvaluationValue... operands)
      throws EvaluationException {
    EvaluationValue leftOperand = operands[0];
    EvaluationValue rightOperand = operands[1];

    if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
      /*-
       * Thanks to Gene Marin:
       * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
       */

      MathContext mathContext = expression.getConfiguration().getMathContext();
      BigDecimal v1 = leftOperand.getNumberValue();
      BigDecimal v2 = rightOperand.getNumberValue();

      int signOf2 = v2.signum();
      double dn1 = v1.doubleValue();
      v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
      BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
      BigDecimal n2IntPart = v2.subtract(remainderOf2);
      BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mathContext);
      BigDecimal doublePow = BigDecimal.valueOf(Math.pow(dn1, remainderOf2.doubleValue()));

      BigDecimal result = intPow.multiply(doublePow, mathContext);
      if (signOf2 == -1) {
        result = BigDecimal.ONE.divide(result, mathContext.getPrecision(), RoundingMode.HALF_UP);
      }
      return expression.convertValue(result);
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
    }
  }

  @Override
  public int getPrecedence(ExpressionConfiguration configuration) {
    return configuration.getPowerOfPrecedence();
  }
}
