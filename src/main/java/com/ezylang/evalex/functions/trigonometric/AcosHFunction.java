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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

/** Returns the hyperbolic arc-cosine. */
@FunctionParameter(name = "value")
public class AcosHFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    /* Formula: acosh(x) = ln(x + sqrt(x^2 - 1)) */
    double value = parameterValues[0].getNumberValue().doubleValue();
    if (Double.compare(value, 1) < 0) {
      throw new EvaluationException(functionToken, "Value must be greater or equal to one");
    }
    return expression.convertDoubleValue(Math.log(value + (Math.sqrt(Math.pow(value, 2) - 1))));
  }
}
