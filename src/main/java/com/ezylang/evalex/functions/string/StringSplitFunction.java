/*
  Copyright 2012-2025 Udo Klimaschewski

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
import java.util.regex.Pattern;

/**
 * A function that splits a string into an array, separators specified.
 *
 * <p>For example:
 *
 * <p>
 *
 * <pre>
 * STR_SPLIT("2024/07/15", "/")  = ["2024", "07", "15"]
 * STR_SPLIT("myFile.json", ".") = ["myFile", "json"]
 * </pre>
 *
 * @author oswaldobapvicjr
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "separator")
public class StringSplitFunction extends AbstractFunction {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    String string = parameterValues[0].getStringValue();
    String separator = parameterValues[1].getStringValue();
    return expression.convertValue(string.split(Pattern.quote(separator)));
  }
}
