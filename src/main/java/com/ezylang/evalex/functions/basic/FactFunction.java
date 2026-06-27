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
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;

/** Factorial function, calculates the factorial of a base value. */
@FunctionParameter(name = "base")
public class FactFunction extends AbstractFunction {

  /**
   * Maximum allowed input value for factorial calculation, aligned with industry-standard upper
   * limits. This prevents uncontrolled resource consumption (CWE-400) while maintaining
   * compatibility with user expectations from widely-used tools.
   *
   * @see https://github.com/ezylang/EvalEx/issues/570
   */
  private static final int MAX_FACTORIAL_INPUT = 170;

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    int number = parameterValues[0].getNumberValue().intValue();

    if (number > MAX_FACTORIAL_INPUT) {
      throw new EvaluationException(
          functionToken,
          String.format(
              "Factorial input exceeds maximum allowed value: %d > %d",
              number, MAX_FACTORIAL_INPUT));
    }

    BigDecimal factorial = BigDecimal.ONE;
    for (int i = 1; i <= number; i++) {
      factorial =
          factorial.multiply(
              new BigDecimal(i, expression.getConfiguration().getMathContext()),
              expression.getConfiguration().getMathContext());
    }
    return expression.convertValue(factorial);
  }
}
