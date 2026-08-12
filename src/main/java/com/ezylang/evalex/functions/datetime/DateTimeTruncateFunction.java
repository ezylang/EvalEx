/*
  Copyright 2012-2026 Udo Klimaschewski

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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

/**
 * An EvalEx library function that truncates an Instant date-time value to a specific time unit,
 * allowing developers to normalize timestamps for groupings, reports, or interval calculations.
 *
 * <p>To maximize usability and maintain strict internal consistency within the EvalEx ecosystem,
 * this function utilizes standard Java {@link java.time.format.DateTimeFormatter} single-character
 * format tokens. It follows a strict positional argument pattern for its optional parameters; if
 * you wish to skip the {@code timeUnit} parameter but specify a custom {@code zoneId}, you must
 * pass an explicit {@code NULL} as the second argument.
 *
 * <h3>Syntax</h3>
 *
 * {@code DT_TRUNCATE(dateTime [, timeUnit [, zoneId]])}
 *
 * <h3>Function Parameters</h3>
 *
 * <ul>
 *   <li><b>dateTime</b> - the date-time value to truncate.
 *   <li><b>timeUnit</b> - <i>(Optional)</i> a string containing a valid case-sensitive formatting
 *       character. Defaults to {@code 'd'} (Day) if omitted or set to {@code NULL}:
 *       <ul>
 *         <li>{@code 'y'} - Truncates to the start of the <b>Year</b>.
 *         <li>{@code 'M'} - Truncates to the start of the <b>Month</b> (Uppercase)
 *         <li>{@code 'd'} - Truncates to the start of the <b>Day</b>
 *         <li>{@code 'H'} - Truncates to the start of the <b>Hour</b> (Uppercase)
 *         <li>{@code 'm'} - Truncates to the start of the <b>Minute</b> (Lowercase)
 *         <li>{@code 's'} - Truncates to the start of the <b>Second</b>
 *       </ul>
 *   <li><b>zoneId</b> - <i>(Optional)</i> A string representing a valid Java ZoneId. Defaults to
 *       the expression configuration's time zone if omitted or set to {@code NULL}.
 * </ul>
 *
 * <h3>Examples</h3>
 *
 * Assuming UTC is the system default time zone, and the variable {@code dt} represents {@code
 * 2026-08-08T14:30:45Z}:
 *
 * <pre>
 *   DT_TRUNCATE(dt)                          -&gt; 2026-08-08T00:00:00Z (Truncated to Day)
 *   DT_TRUNCATE(dt, "M")                     -&gt; 2026-08-01T00:00:00Z (Truncated to Month)
 *   DT_TRUNCATE(dt, "H")                     -&gt; 2026-08-08T14:00:00Z (Truncated to Hour)
 *   DT_TRUNCATE(dt, NULL, "America/Chicago") -&gt; 2026-08-08T05:00:00Z (Truncated to Day in Chicago time)
 * </pre>
 *
 * *
 *
 * @author oswaldo.bapvic.jr
 * @since 3.8.0
 */
@FunctionParameter(name = "dateTime")
@FunctionParameter(name = "optionalParams", isVarArg = true) // timeUnit and zoneId
public class DateTimeTruncateFunction extends AbstractFunction {

  private static final String DEFAULT_TIME_UNIT = "d";

  private static final Map<String, ChronoUnit> JAVA_FORMAT_MAPPING =
      Map.of(
          DEFAULT_TIME_UNIT,
          ChronoUnit.DAYS,
          "y",
          ChronoUnit.YEARS,
          "M",
          ChronoUnit.MONTHS,
          "H",
          ChronoUnit.HOURS,
          "m",
          ChronoUnit.MINUTES,
          "s",
          ChronoUnit.SECONDS);

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {

    Instant instant = parameterValues[0].getDateTimeValue();

    // Resolve the time unit
    String unitToken =
        (parameterValues.length > 1 && !parameterValues[1].isNullValue())
            ? parameterValues[1].getStringValue().trim()
            : DEFAULT_TIME_UNIT;

    ChronoUnit unit = JAVA_FORMAT_MAPPING.get(unitToken);
    if (unit == null) {
      throw new EvaluationException(
          functionToken,
          String.format(
              "Invalid time unit '%s'. Use standard Java format tokens: 'y', 'M', 'd', 'H', 'm',"
                  + " 's'.",
              unitToken));
    }

    // Resolve the time zone
    ZoneId zoneId = expression.getConfiguration().getZoneId();
    if (parameterValues.length > 2 && !parameterValues[2].isNullValue()) {
      String zoneString = parameterValues[2].getStringValue().trim();
      try {
        zoneId = ZoneId.of(zoneString);
      } catch (Exception e) {
        throw new EvaluationException(
            functionToken, "Time zone with id '" + zoneString + "' not found");
      }
    }

    // Apply truncation
    ZonedDateTime localDateTime = ZonedDateTime.ofInstant(instant, zoneId);
    ZonedDateTime truncatedDateTime;
    switch (unit) {
      case YEARS:
        truncatedDateTime =
            localDateTime.truncatedTo(ChronoUnit.DAYS).with(TemporalAdjusters.firstDayOfYear());
        break;
      case MONTHS:
        truncatedDateTime =
            localDateTime.truncatedTo(ChronoUnit.DAYS).with(TemporalAdjusters.firstDayOfMonth());
        break;
      default:
        truncatedDateTime = localDateTime.truncatedTo(unit);
        break;
    }

    Instant resultInstant = truncatedDateTime.toInstant();
    return EvaluationValue.dateTimeValue(resultInstant);
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
