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
package com.ezylang.evalex.functions.trigonometric;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.valueOf;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;

/** Returns the arc-sine (in degrees). */
@FunctionParameter(name = "value")
public class AsinFunction extends AbstractFunction {

  private static final BigDecimal MINUS_ONE = valueOf(-1);

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    BigDecimal parameterValue = parameterValues[0].getNumberValue();

    if (parameterValue.compareTo(ONE) > 0) {
      throw new EvaluationException(
          functionToken, "Illegal asin(x) for x > 1: x = " + parameterValue);
    }
    if (parameterValue.compareTo(MINUS_ONE) < 0) {
      throw new EvaluationException(
          functionToken, "Illegal asin(x) for x < -1: x = " + parameterValue);
    }
    return expression.convertDoubleValue(Math.toDegrees(Math.asin(parameterValue.doubleValue())));
  }
}
