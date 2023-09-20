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
import java.util.Calendar;
import java.util.Date;

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

  @Override
  public boolean canConvert(Object object) {
    return (object instanceof Instant
        || object instanceof ZonedDateTime
        || object instanceof OffsetDateTime
        || object instanceof LocalDate
        || object instanceof LocalDateTime
        || object instanceof Date
        || object instanceof Calendar);
  }
}
