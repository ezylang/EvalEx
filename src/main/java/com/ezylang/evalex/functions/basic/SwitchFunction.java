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
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

/**
 * A function that evaluates one value (or expression) against a list of values, and returns the
 * result corresponding to the first matching value. If there is no match, an optional default value
 * may be returned.
 *
 * <p><strong>Syntax:</strong>
 *
 * <blockquote>
 *
 * {@code SWITCH(expression, value1, result1, [value, result, ...], [default])}
 *
 * </blockquote>
 *
 * <p><strong>Examples:</strong>
 *
 * <p>1. The following function will return either "Sunday", "Monday", or "Tuesday", depending on
 * the result of the variable {@code weekday}. Since no default value was specified, the function
 * will return a null value if there is no match:
 *
 * <blockquote>
 *
 * {@code SWITCH(weekday, 1, "Sunday", 2, "Monday", 3, "Tuesday")}
 *
 * </blockquote>
 *
 * <p>2. The following function will return either "Sunday", "Monday", "Tuesday", or "No match",
 * depending on the result of the variable {@code weekday}:
 *
 * <blockquote>
 *
 * {@code SWITCH(weekday, 1, "Sunday", 2, "Monday", 3, "Tuesday", "No match")}
 *
 * </blockquote>
 *
 * @author oswaldo.bapvic.jr
 */
@FunctionParameter(name = "expression")
@FunctionParameter(name = "value1")
@FunctionParameter(name = "result1", isLazy = true)
@FunctionParameter(name = "additionalValues", isLazy = true, isVarArg = true)
public class SwitchFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    EvaluationValue result = EvaluationValue.NULL_VALUE;

    // First get the first parameter
    EvaluationValue value = parameterValues[0];

    // Iterate through the parameters to parse the pairs of value-result and the default result if
    // present.
    int index = 1;
    while (index < parameterValues.length) {
      int next = index + 1;
      if (next < parameterValues.length) {
        if (value.equals(evaluateParameter(expression, parameterValues[index]))) {
          result = parameterValues[next];
          break;
        }
        index += 2;
      } else {
        // The default result
        result = parameterValues[index++];
      }
    }
    return evaluateParameter(expression, result);
  }

  private EvaluationValue evaluateParameter(Expression expression, EvaluationValue parameter)
      throws EvaluationException {
    return parameter.isExpressionNode()
        ? expression.evaluateSubtree(parameter.getExpressionNode())
        : parameter;
  }
}
