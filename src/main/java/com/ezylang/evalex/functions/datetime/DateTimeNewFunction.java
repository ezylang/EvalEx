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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.TimeZone;

/**
 * Creates a new DATE_TIME value with the given parameters. If only one parameter is given, it is
 * treated as the time in milliseconds from the epoch of 1970-01-01T00:00:00Z and a corresponding
 * date/time value is created. Else, A minimum of three parameters (year, month, day) must be
 * specified. Optionally the hour, minute, second and nanosecond can be specified. If the last
 * parameter is a string value, it is treated as a zone ID. If no zone ID is specified, the
 * configured zone ID is used.
 */
@FunctionParameter(name = "values", isVarArg = true, nonNegative = true)
public class DateTimeNewFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    int parameterLength = parameterValues.length;

    if (parameterLength == 1) {
      BigDecimal millis = parameterValues[0].getNumberValue();
      return expression.convertValue(Instant.ofEpochMilli(millis.longValue()));
    }

    ZoneId zoneId = expression.getConfiguration().getZoneId();
    if (parameterValues[parameterLength - 1].isStringValue()) {
      zoneId =
          ZoneIdConverter.convert(
              functionToken, parameterValues[parameterLength - 1].getStringValue());
      parameterLength--;
    }

    int year = parameterValues[0].getNumberValue().intValue();
    int month = parameterValues[1].getNumberValue().intValue();
    int day = parameterValues[2].getNumberValue().intValue();
    int hour = parameterLength >= 4 ? parameterValues[3].getNumberValue().intValue() : 0;
    int minute = parameterLength >= 5 ? parameterValues[4].getNumberValue().intValue() : 0;
    int second = parameterLength >= 6 ? parameterValues[5].getNumberValue().intValue() : 0;
    int nanoOfs = parameterLength == 7 ? parameterValues[6].getNumberValue().intValue() : 0;

    return expression.convertValue(
        LocalDateTime.of(year, month, day, hour, minute, second, nanoOfs)
            .atZone(zoneId)
            .toInstant());
  }

  @Override
  public void validatePreEvaluation(Token token, EvaluationValue... parameterValues)
      throws EvaluationException {

    super.validatePreEvaluation(token, parameterValues);

    int parameterLength = parameterValues.length;

    if (parameterLength == 1) {
      if (!parameterValues[0].isNumberValue()) {
        throw new EvaluationException(
            token, "Expected a number value for the time in milliseconds since the epoch");
      } else {
        return;
      }
    }

    if (parameterValues[parameterLength - 1].isStringValue()) {
      if (!Set.of(TimeZone.getAvailableIDs())
          .contains(parameterValues[parameterLength - 1].getStringValue())) {
        throw new EvaluationException(
            token,
            "Time zone with id '"
                + parameterValues[parameterLength - 1].getStringValue()
                + "' not found");
      }
      parameterLength--;
    }

    if (parameterLength < 3) {
      throw new EvaluationException(
          token, "A minimum of 3 parameters (year, month, day) is required");
    }

    if (parameterLength > 7) {
      throw new EvaluationException(token, "Too many parameters to function");
    }
  }
}
