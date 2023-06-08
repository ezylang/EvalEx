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

import com.ezylang.evalex.functions.FunctionParameter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@FunctionParameter(name = "value", isVarArg = true)
public class ZonedDateTimeParseFunction extends AbstractDateTimeParseFunction {
  protected Instant parse(String value, String format, ZoneId zoneId) {
    return parseZonedDateTime(value, format, zoneId)
        .orElseThrow(
            () -> new IllegalArgumentException("Unable to parse zoned date/time: " + value));
  }

  private Optional<Instant> parseZonedDateTime(String value, String format, ZoneId zoneId) {
    try {
      DateTimeFormatter formatter =
          (format == null
                  ? DateTimeFormatter.ISO_ZONED_DATE_TIME
                  : DateTimeFormatter.ofPattern(format))
              .withZone(zoneId);
      ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, formatter);
      return Optional.of(zonedDateTime.toInstant());
    } catch (DateTimeParseException ex) {
      return Optional.empty();
    }
  }
}
