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

/**
 * Conditional evaluation function. If parameter <code>condition</code> is <code>true</code>, the
 * <code>resultIfTrue</code> value is returned, else the <code>resultIfFalse</code> value. <code>
 * resultIfTrue</code> and <code>resultIfFalse</code> are only evaluated (lazily evaluated),
 * <b>after</b> the condition was evaluated.
 */
@FunctionParameter(name = "condition")
@FunctionParameter(name = "resultIfTrue", isLazy = true)
@FunctionParameter(name = "resultIfFalse", isLazy = true)
public class IfFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    if (Boolean.TRUE.equals(parameterValues[0].getBooleanValue())) {
      return expression.evaluateSubtree(parameterValues[1].getExpressionNode());
    } else {
      return expression.evaluateSubtree(parameterValues[2].getExpressionNode());
    }
  }
}
