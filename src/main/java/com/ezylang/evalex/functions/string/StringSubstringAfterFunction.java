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
 * Returns the substring after the first occurrence of another (separator) string, or an empty
 * string if the first string does not contain the second string.
 *
 * <p>For example: {@code STR_SUBSTRING_AFTER("2024/07/15", "/")} returns {@code "07/15"}.
 *
 * @author oswaldobapvicjr
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "separator")
public class StringSubstringAfterFunction extends AbstractFunction {
  private static final String STR_EMPTY = "";

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    String string = parameterValues[0].getStringValue();
    String separator = parameterValues[1].getStringValue();
    return expression.convertValue(substringAfter(string, separator));
  }

  private String substringAfter(String string, String separator) {
    int position = string.indexOf(separator);
    return (position == -1) ? STR_EMPTY : string.substring(position + separator.length());
  }
}
