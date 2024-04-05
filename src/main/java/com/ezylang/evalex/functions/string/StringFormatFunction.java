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

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.util.stream.IntStream;

/**
 * Returns a formatted string using the specified format string and arguments.
 *
 * <p>For example:
 *
 * <blockquote>
 *
 * {@code STR_FORMAT("Welcome to %s!", "EvalEx")}
 *
 * </blockquote>
 *
 * <p>The result is produced using {@link String#format(String, Object...)}.
 *
 * @author oswaldobapvicjr
 */
@FunctionParameter(name = "format")
@FunctionParameter(name = "arguments", isVarArg = true)
public class StringFormatFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    String format = parameterValues[0].getStringValue();
    Object[] arguments = getArguments(parameterValues);
    return expression.convertValue(String.format(format, arguments));
  }

  private Object[] getArguments(EvaluationValue[] parameterValues) {
    if (parameterValues.length > 1) {
      return IntStream.range(1, parameterValues.length)
          .mapToObj(i -> parameterValues[i])
          .map(EvaluationValue::getValue)
          .toArray();
    }
    return new Object[0];
  }
}
