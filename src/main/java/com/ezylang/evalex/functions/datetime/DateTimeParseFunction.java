/*
  Copyright 2012-2023 Udo Klimaschewski

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
import com.ezylang.evalex.data.conversion.DateTimeConverter;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a date-time string to a {@link EvaluationValue.DataType#DATE_TIME} value.
 *
 * <p>Optional arguments is the time zone and a list of {@link java.time.format.DateTimeFormatter}
 * patterns. Each pattern will be tried to convert the string to a date-time. The first matching
 * pattern will be used. If <code>NULL</code> is specified for the time zone, the currently
 * configured zone is used. If no formatters a
 */
@FunctionParameter(name = "parameters", isVarArg = true)
public class DateTimeParseFunction extends AbstractFunction {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    String value = parameterValues[0].getStringValue();

    ZoneId zoneId = expression.getConfiguration().getZoneId();
    if (parameterValues.length > 1 && !parameterValues[1].isNullValue()) {
      zoneId = ZoneIdConverter.convert(functionToken, parameterValues[1].getStringValue());
    }

    List<DateTimeFormatter> formatters;

    if (parameterValues.length > 2) {
      formatters = new ArrayList<>();
      for (int i = 2; i < parameterValues.length; i++) {
        try {
          formatters.add(DateTimeFormatter.ofPattern(parameterValues[i].getStringValue()));
        } catch (IllegalArgumentException ex) {
          throw new EvaluationException(
              functionToken,
              String.format(
                  "Illegal date-time format in parameter %d: '%s'",
                  i + 1, parameterValues[i].getStringValue()));
        }
      }
    } else {
      formatters = expression.getConfiguration().getDateTimeFormatters();
    }
    DateTimeConverter converter = new DateTimeConverter();
    Instant instant = converter.parseDateTime(value, zoneId, formatters);

    if (instant == null) {
      throw new EvaluationException(
          functionToken, String.format("Unable to parse date-time string '%s'", value));
    }
    return EvaluationValue.dateTimeValue(instant);
  }
}
