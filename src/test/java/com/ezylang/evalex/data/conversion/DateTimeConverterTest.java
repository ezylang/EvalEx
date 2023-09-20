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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import java.math.BigDecimal;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

class DateTimeConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final ExpressionConfiguration cetConfiguration =
      ExpressionConfiguration.builder().zoneId(ZoneId.of("Europe/Berlin")).build();

  private final DateTimeConverter converter = new DateTimeConverter();

  @Test
  void testInstant() {
    Instant now = Instant.now();

    EvaluationValue converted = converter.convert(now, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue()).isEqualTo(now);
  }

  @Test
  void testZonedDateTime() {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("US/Central"));

    EvaluationValue converted = converter.convert(now, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue()).isEqualTo(now.toInstant());
  }

  @Test
  void testOffsetDateTime() {
    OffsetDateTime now = OffsetDateTime.now(ZoneId.of("US/Central"));

    EvaluationValue converted = converter.convert(now, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue()).isEqualTo(now.toInstant());
  }

  @Test
  void testLocalDateDaylightSaving() {
    LocalDate localDate = LocalDate.parse("2022-10-20");

    EvaluationValue converted = new EvaluationValue(localDate, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue().toString()).hasToString("2022-10-19T22:00:00Z");
  }

  @Test
  void testLocalDateNoDaylightSaving() {
    LocalDate localDate = LocalDate.parse("2022-11-20");

    EvaluationValue converted = new EvaluationValue(localDate, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue().toString()).hasToString("2022-11-19T23:00:00Z");
  }

  @Test
  void testLocalDateTimeDaylightSaving() {
    LocalDateTime localDateTime = LocalDateTime.parse("2022-10-20T11:21:30");

    EvaluationValue converted = new EvaluationValue(localDateTime, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue().toString()).hasToString("2022-10-20T09:21:30Z");
  }

  @Test
  void testLocalDateTimeNoDaylightSaving() {
    LocalDateTime localDateTime = LocalDateTime.parse("2022-11-20T11:21:30");

    EvaluationValue converted = new EvaluationValue(localDateTime, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue().toString()).hasToString("2022-11-20T10:21:30Z");
  }

  @Test
  void testDate() {
    Date now = new Date();

    EvaluationValue converted = converter.convert(now, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue()).isEqualTo(now.toInstant());
  }

  @Test
  void testCalendar() {
    Calendar now = Calendar.getInstance();

    EvaluationValue converted = converter.convert(now, cetConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.DATE_TIME);
    assertThat(converted.getValue()).isEqualTo(now.toInstant());
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert(Instant.now())).isTrue();
    assertThat(converter.canConvert(ZonedDateTime.now())).isTrue();
    assertThat(converter.canConvert(OffsetDateTime.now())).isTrue();
    assertThat(converter.canConvert(LocalDate.now())).isTrue();
    assertThat(converter.canConvert(LocalDateTime.now())).isTrue();
    assertThat(converter.canConvert(new Date())).isTrue();
    assertThat(converter.canConvert(Calendar.getInstance())).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert("hello")).isFalse();
    assertThat(converter.canConvert(new BigDecimal(1))).isFalse();
  }

  @Test
  void testException() {
    assertThatThrownBy(
            () -> converter.convert("hello", defaultConfiguration))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.lang.String'");
  }
}
