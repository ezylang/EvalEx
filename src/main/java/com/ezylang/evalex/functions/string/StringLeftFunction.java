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

/**
 * Represents a function that extracts a substring from the left side of a given string. This class
 * extends the {@link AbstractFunction} and implements the logic for the `LEFT` string function,
 * which returns a specified number of characters from the beginning (left) of the input string.
 *
 * <p>Two parameters are required for this function:
 *
 * <ul>
 *   <li><b>string</b> - The input string from which the substring will be extracted.
 *   <li><b>length</b> - The number of characters to extract from the left side of the string. If
 *       the specified length is greater than the string's length, the entire string is returned. If
 *       the length is negative or zero, an empty string is returned.
 * </ul>
 *
 * <p>Example usage: If the input string is "hello" and the length is 2, the result will be "he".
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "length")
public class StringLeftFunction extends AbstractFunction {

  /**
   * Evaluates the `LEFT` string function by extracting a substring from the left side of the given
   * string.
   *
   * @param expression the current expression being evaluated
   * @param functionToken the token representing the function being called
   * @param parameterValues the parameters passed to the function; expects exactly two parameters: a
   *     string and a numeric value for length
   * @return the substring extracted from the left side of the input string as an {@link
   *     EvaluationValue}
   */
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    String string = parameterValues[0].getStringValue();
    int length =
        Math.max(0, Math.min(parameterValues[1].getNumberValue().intValue(), string.length()));
    String substr = string.substring(0, length);
    return expression.convertValue(substr);
  }
}
