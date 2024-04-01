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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Function to format a DATE_TIME vale. Required parameter is the DATE_TIME value to format. First
 * optional parameter is the format to use, using a pattern used by {@link DateTimeFormatter}. If no
 * format is given, the first format defined in the configured formats is used. Second optional
 * parameter is the zone-id to use wit formatting. Default is the configured zone-id.
 */
@FunctionParameter(name = "value")
@FunctionParameter(name = "parameters", isVarArg = true)
public class DateTimeFormatFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    DateTimeFormatter formatter = expression.getConfiguration().getDateTimeFormatters().get(0);
    if (parameterValues.length > 1) {
      formatter = DateTimeFormatter.ofPattern(parameterValues[1].getStringValue());
    }

    ZoneId zoneId = expression.getConfiguration().getZoneId();
    if (parameterValues.length == 3) {
      zoneId = ZoneIdConverter.convert(functionToken, parameterValues[2].getStringValue());
    }

    return expression.convertValue(
        parameterValues[0].getDateTimeValue().atZone(zoneId).format(formatter));
  }

  @Override
  public void validatePreEvaluation(Token token, EvaluationValue... parameterValues)
      throws EvaluationException {
    super.validatePreEvaluation(token, parameterValues);
    if (parameterValues.length > 3) {
      throw new EvaluationException(token, "Too many parameters");
    }
    if (!parameterValues[0].isDateTimeValue()) {
      throw new EvaluationException(
          token,
          String.format(
              "Unable to format a '%s' type as a date-time",
              parameterValues[0].getDataType().name()));
    }
  }
}
