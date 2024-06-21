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
package com.ezylang.evalex.data.conversion;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** Converter to convert to the DATE_TIME data type. */
public class DateTimeConverter implements ConverterIfc {

  @Override
  public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {

    Instant instant;

    if (object instanceof Instant) {
      instant = (Instant) object;
    } else if (object instanceof ZonedDateTime) {
      instant = ((ZonedDateTime) object).toInstant();
    } else if (object instanceof OffsetDateTime) {
      instant = ((OffsetDateTime) object).toInstant();
    } else if (object instanceof LocalDate) {
      instant = ((LocalDate) object).atStartOfDay().atZone(configuration.getZoneId()).toInstant();
    } else if (object instanceof LocalDateTime) {
      instant = ((LocalDateTime) object).atZone(configuration.getZoneId()).toInstant();
    } else if (object instanceof Date) {
      instant = ((Date) object).toInstant();
    } else if (object instanceof Calendar) {
      instant = ((Calendar) object).toInstant();
    } else {
      throw illegalArgument(object);
    }
    return EvaluationValue.dateTimeValue(instant);
  }

  /**
   * Tries to parse a date-time string by trying out each format in the list. The first matching
   * result is returned. If none of the formats can be used to parse the string, <code>null</code>
   * is returned.
   *
   * @param value The string to parse.
   * @param zoneId The {@link ZoneId} to use for parsing.
   * @param formatters The list of formatters.
   * @return A parsed {@link Instant} if parsing was successful, else <code>null</code>.
   */
  public Instant parseDateTime(String value, ZoneId zoneId, List<DateTimeFormatter> formatters) {
    for (DateTimeFormatter formatter : formatters) {
      try {
        return parseToInstant(value, zoneId, formatter);
      } catch (DateTimeException ignored) {
        // ignore
      }
    }
    return null;
  }

  private Instant parseToInstant(String value, ZoneId zoneId, DateTimeFormatter formatter) {
    TemporalAccessor ta = formatter.parse(value);
    ZoneId parsedZoneId = ta.query(TemporalQueries.zone());
    if (parsedZoneId == null) {
      LocalDate parsedDate = ta.query(TemporalQueries.localDate());
      LocalTime parsedTime = ta.query(TemporalQueries.localTime());
      if (parsedTime == null) {
        parsedTime = parsedDate.atStartOfDay().toLocalTime();
      }
      ta = ZonedDateTime.of(parsedDate, parsedTime, zoneId);
    }
    return Instant.from(ta);
  }

  @Override
  public boolean canConvert(Object object, ExpressionConfiguration configuration) {
    return (object instanceof Instant
        || object instanceof ZonedDateTime
        || object instanceof OffsetDateTime
        || object instanceof LocalDate
        || object instanceof LocalDateTime
        || object instanceof Date
        || object instanceof Calendar);
  }
}
