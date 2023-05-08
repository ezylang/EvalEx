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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.time.LocalDateTime;

@FunctionParameter(name = "values", isVarArg = true, nonNegative = true)
public class DateTimeFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    int year = parameterValues[0].getNumberValue().intValue();
    int month = parameterValues[1].getNumberValue().intValue();
    int day = parameterValues[2].getNumberValue().intValue();
    int hour = parameterValues.length >= 4 ? parameterValues[3].getNumberValue().intValue() : 0;
    int minute = parameterValues.length >= 5 ? parameterValues[4].getNumberValue().intValue() : 0;
    int second = parameterValues.length >= 6 ? parameterValues[5].getNumberValue().intValue() : 0;
    int nanoOfs = parameterValues.length >= 7 ? parameterValues[6].getNumberValue().intValue() : 0;

    return new EvaluationValue(LocalDateTime.of(year, month, day, hour, minute, second, nanoOfs));
  }
}
