/*
  Copyright 2012-2024 Udo Klimaschewski

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
package com.ezylang.evalex.functions.string;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

/**
 * Returns a substring of a string.
 *
 * @author HSGamer
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "start", nonNegative = true)
@FunctionParameter(name = "end", isVarArg = true, nonNegative = true)
public class StringSubstringFunction extends AbstractFunction {
  @Override
  public void validatePreEvaluation(Token token, EvaluationValue... parameterValues)
      throws EvaluationException {
    super.validatePreEvaluation(token, parameterValues);
    if (parameterValues.length > 2) {
      if (parameterValues[2].getNumberValue().intValue()
          < parameterValues[1].getNumberValue().intValue()) {
        throw new EvaluationException(
            token, "End index must be greater than or equal to start index");
      }
    }
  }

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    String string = parameterValues[0].getStringValue();
    int start = parameterValues[1].getNumberValue().intValue();
    String result;
    if (parameterValues.length > 2) {
      int end = parameterValues[2].getNumberValue().intValue();
      int length = string.length();
      int finalEnd = Math.min(end, length);
      result = string.substring(start, finalEnd);
    } else {
      result = string.substring(start);
    }
    return expression.convertValue(result);
  }
}
