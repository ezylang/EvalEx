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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.parser.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DateTimeFunctionsTest extends BaseEvaluationTest {

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Berlin");

  private static final ExpressionConfiguration DateTimeTestConfiguration =
      TestConfigurationProvider.StandardConfigurationWithAdditionalTestOperators.toBuilder()
          .zoneId(DEFAULT_ZONE_ID)
          .build();

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_NEW(2022,10,30) | 2022-10-29T22:00:00Z",
        "DT_DATE_NEW(2022,09,30,11) | 2022-09-30T09:00:00Z",
        "DT_DATE_NEW(2022,10,30,11) | 2022-10-30T10:00:00Z",
        "DT_DATE_NEW(2022,10,30,11,50,20) | 2022-10-30T10:50:20Z",
        "DT_DATE_NEW(2022,10,30,11,50,20,30) | 2022-10-30T10:50:20.000000030Z",
        "DT_DATE_NEW(2022,10,30,\"Africa/Nairobi\") | 2022-10-29T21:00:00Z",
        "DT_DATE_NEW(2022,10,30,11,\"Africa/Nairobi\") | 2022-10-30T08:00:00Z",
        "DT_DATE_NEW(2022,10,30,11,50,\"Africa/Nairobi\") | 2022-10-30T08:50:00Z",
        "DT_DATE_NEW(2022,10,30,11,50,20,\"Africa/Nairobi\") | 2022-10-30T08:50:20Z",
        "DT_DATE_NEW(2022,10,30,11,50,20,30,\"Africa/Nairobi\") | 2022-10-30T08:50:20.000000030Z"
      })
  void testDateTimeNew(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult, DateTimeTestConfiguration);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "DT_DATE_NEW(2022,10)",
        "DT_DATE_NEW(2022,10,\"Africa/Nairobi\")",
        "DT_DATE_NEW(2022,\"Africa/Nairobi\")"
      })
  void testDateTimeNewTooFewParameters(String expression) {
    assertThatThrownBy(() -> new Expression(expression).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("A minimum of 3 parameters (year, month, day) is required");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "DT_DATE_NEW(2022,10,30,11,50,20,30,40)",
        "DT_DATE_NEW(2022,10,30,11,50,20,30,40,50)",
        "DT_DATE_NEW(2022,10,30,11,50,20,30,40,\"Africa/Nairobi\")",
        "DT_DATE_NEW(2022,10,30,11,50,20,30,40,50,\"Africa/Nairobi\")"
      })
  void testDateTimeNewTooManyParameters(String expression) {
    assertThatThrownBy(() -> new Expression(expression).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Too many parameters to function");
  }

  @Test
  void testDateTimeNewWrongMillisNewDate() {
    assertThatThrownBy(() -> new Expression("DT_DATE_NEW(\"nan\")").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Expected a number value for the time in milliseconds since the epoch");
  }

  @Test
  void testDateTimeNewZoneIdNotFound() {
    assertThatThrownBy(
            () -> new Expression("DT_DATE_NEW(2022,10,30,\"Mars/Olympus-Mons\")").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Time zone with id 'Mars/Olympus-Mons' not found");
  }

  @Test
  void testDateTimeNewNoParam() {
    assertThatThrownBy(() -> new Expression("DT_DATE_NEW()").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Not enough parameters for function");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_PARSE(\"2022-09-30T11:50:20Z\") | 2022-09-30T11:50:20Z",
        "DT_DATE_PARSE(\"2022-11-01T11:50:20Z\") | 2022-11-01T11:50:20Z",
        "DT_DATE_PARSE(\"2022-09-30T11:50:20\") | 2022-09-30T09:50:20Z",
        "DT_DATE_PARSE(\"2022-11-01T11:50:20\") | 2022-11-01T10:50:20Z",
        "DT_DATE_PARSE(\"2022-11-01T11:50:20+03:00\") | 2022-11-01T08:50:20Z",
        "DT_DATE_PARSE(\"2011-12-03T10:15:30+01:00[Europe/Paris]\") | 2011-12-03T09:15:30Z",
        "DT_DATE_PARSE(\"2022-10-30T11:50:20.000000030\") | 2022-10-30T10:50:20.000000030Z",
        "DT_DATE_PARSE(\"2022-06-13\") | 2022-06-12T22:00:00Z",
        "DT_DATE_PARSE(\"2022-11-30\") | 2022-11-29T23:00:00Z",
        "DT_DATE_PARSE(\"Tue, 3 Jun 2008 11:05:30 GMT\") | 2008-06-03T11:05:30Z"
      })
  void testDateTimeParseDefault(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult, DateTimeTestConfiguration);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_PARSE(\"2022-09-30T11:50:20Z\", \"America/New_York\") | 2022-09-30T11:50:20Z",
        "DT_DATE_PARSE(\"2022-11-10T11:50:20Z\", \"America/New_York\") | 2022-11-10T11:50:20Z",
        "DT_DATE_PARSE(\"2022-09-30T11:50:20\", \"America/New_York\") | 2022-09-30T15:50:20Z",
        "DT_DATE_PARSE(\"2022-11-10T11:50:20\", \"America/New_York\") | 2022-11-10T16:50:20Z",
        "DT_DATE_PARSE(\"2022-11-10T11:50:20+03:00\", \"America/New_York\") | 2022-11-10T08:50:20Z",
        "DT_DATE_PARSE(\"2011-12-03T10:15:30+01:00[Europe/Paris]\", \"America/New_York\") |"
            + " 2011-12-03T09:15:30Z",
        "DT_DATE_PARSE(\"2022-10-30T11:50:20.000000030\", \"America/New_York\") |"
            + " 2022-10-30T15:50:20.000000030Z",
        "DT_DATE_PARSE(\"2022-06-13\", \"America/New_York\") | 2022-06-13T04:00:00Z",
        "DT_DATE_PARSE(\"2022-11-30\", \"America/New_York\") | 2022-11-30T05:00:00Z",
        "DT_DATE_PARSE(\"Tue, 3 Jun 2008 11:05:30 GMT\", \"America/New_York\") |"
            + " 2008-06-03T11:05:30Z"
      })
  void testDateTimeParseWithZone(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult, DateTimeTestConfiguration);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_PARSE(\"23.05.2023\", NULL, \"dd.MM.yyyy\") | 2023-05-22T22:00:00Z",
        "DT_DATE_PARSE(\"23.11.2023\", NULL, \"dd.MM.yyyy\") | 2023-11-22T23:00:00Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10\", NULL, \"dd.MM.yyyy HH:mm:ss\") |"
            + " 2023-11-23T13:35:10Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10 America/Chicago\", NULL, \"dd.MM.yyyy HH:mm:ss z\") |"
            + " 2023-11-23T20:35:10Z",
        "DT_DATE_PARSE(\"23.05.2023\", NULL, \"dd.MM.yyyy\", \"dd.MM.yyyy HH:mm:ss\", \"dd.MM.yyyy"
            + " HH:mm:ss z\") | 2023-05-22T22:00:00Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10\", NULL, \"dd.MM.yyyy\", \"dd.MM.yyyy HH:mm:ss\","
            + " \"dd.MM.yyyy HH:mm:ss z\") | 2023-11-23T13:35:10Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10 America/Chicago\", NULL, \"dd.MM.yyyy\", \"dd.MM.yyyy"
            + " HH:mm:ss\", \"dd.MM.yyyy HH:mm:ss z\") | 2023-11-23T20:35:10Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10\", \"America/Argentina/Cordoba\", \"dd.MM.yyyy"
            + " HH:mm:ss\") | 2023-11-23T17:35:10Z",
        "DT_DATE_PARSE(\"23.11.2023 14:35:10\", \"Australia/Canberra\", \"dd.MM.yyyy HH:mm:ss\") |"
            + " 2023-11-23T03:35:10Z",
      })
  void testDateTimeParseWithFormats(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult, DateTimeTestConfiguration);
  }

  @Test
  void testDateTimeParseIllegalFormat() {
    assertThatThrownBy(
            () -> new Expression("DT_DATE_PARSE(\"2023-01-01\", NULL, \"defect\")").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Illegal date-time format in parameter 3: 'defect'");
  }

  @Test
  void testDateTimeParseUnableToParse() {
    assertThatThrownBy(() -> new Expression("DT_DATE_PARSE(\"2023-99-99\")").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unable to parse date-time string '2023-99-99'");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_FORMAT(DT_DATE_PARSE(\"2022-10-20T11:50:20\")) |"
            + " 2022-10-20T11:50:20+02:00[Europe/Berlin]",
        "DT_DATE_FORMAT(DT_DATE_PARSE(\"2022-12-20T11:50:20\")) |"
            + " 2022-12-20T11:50:20+01:00[Europe/Berlin]",
        "DT_DATE_FORMAT(DT_DATE_PARSE(\"2022-12-20T11:50:20\"), \"dd.MM.yyyy HH:mm:ss\") |"
            + " 20.12.2022 11:50:20",
        "DT_DATE_FORMAT(DT_DATE_PARSE(\"2022-12-20T11:50:20\"), \"dd.MM.yyyy HH:mm:ss\","
            + " \"America/New_York\") | 20.12.2022 05:50:20"
      })
  void testDateTimeFormat(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult, DateTimeTestConfiguration);
  }

  @Test
  void testDateTimeFormatTooManyParameters() {
    assertThatThrownBy(
            () ->
                new Expression(
                        "DT_DATE_FORMAT(DT_DATE_PARSE(\"2022-12-20T11:50:20\"), \"dd.MM.yyyy\","
                            + " \"America/New_York\", \"invalid\")")
                    .evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Too many parameters");
  }

  @Test
  void testDateTimeFormatNotADateTime() {
    assertThatThrownBy(() -> new Expression("DT_DATE_FORMAT(value)").with("value", 23).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unable to format a 'NUMBER' type as a date-time");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DATE_TO_EPOCH(DT_DATE_NEW(1667130620000)) | 1667130620000",
        "DT_DATE_TO_EPOCH(DT_DATE_NEW(0)) | 0"
      })
  void testDateTimeToEpoch(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testDateTimeNow() throws EvaluationException, ParseException {
    Instant expectedTime = Instant.now();
    Instant actualTime = evaluate("DT_NOW()").getDateTimeValue();
    assertThat(actualTime).isCloseTo(expectedTime, within(1, ChronoUnit.SECONDS));
  }

  @Test
  void testDateTimeTodayDefaultTimeZone() throws EvaluationException, ParseException {
    Instant expectedTime = LocalDate.now().atStartOfDay(DEFAULT_ZONE_ID).toInstant();
    Instant actualTime = evaluate("DT_TODAY()").getDateTimeValue();
    assertThat(actualTime).isEqualTo(expectedTime);
  }

  @Test
  void testDateTimeTodayDifferentTimeZone() throws EvaluationException, ParseException {
    Instant expectedTime = LocalDate.now().atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant();
    Instant actualTime = evaluate("DT_TODAY(\"America/Sao_Paulo\")").getDateTimeValue();
    assertThat(actualTime).isEqualTo(expectedTime);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_NEW(6) | PT144H",
        "DT_DURATION_NEW(6,5) | PT149H",
        "DT_DURATION_NEW(6,5,4) | PT149H4M",
        "DT_DURATION_NEW(6,5,4,3) | PT149H4M3S",
        "DT_DURATION_NEW(6,5,4,3,2) | PT149H4M3.002S",
        "DT_DURATION_NEW(6,5,4,3,2,1) | PT149H4M3.002000001S"
      })
  void testDurationNew(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_FROM_MILLIS(1667130620000) | PT463091H50M20S",
        "DT_DURATION_FROM_MILLIS(0) | PT0S"
      })
  void testDurationFromMillis(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_TO_MILLIS(DT_DURATION_PARSE(\"PT463091H50M20S\")) | 1667130620000",
        "DT_DURATION_TO_MILLIS(DT_DURATION_PARSE(\"PT0S\")) | 0"
      })
  void testDurationToMillis(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "DT_DURATION_PARSE(\"PT1277184H\") | PT1277184H",
        "DT_DURATION_PARSE(\"P1D\") | PT24H"
      })
  void testDurationParse(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testDurationString() throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        "\"Value: \" + DT_DURATION_PARSE(\"P1DT3H4M5S\")", "Value: PT27H4M5S");
  }

  @Test
  void testFormatBerlin() throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        "DT_DATE_FORMAT(DT_DATE_NEW(2022,10,30,11,50,20), \"EEE, d MMM yyyy HH:mm:ss Z\")",
        "So., 30 Okt. 2022 11:50:20 +0100",
        ExpressionConfiguration.builder()
            .locale(Locale.GERMAN)
            .zoneId(ZoneId.of("Europe/Berlin"))
            .build());
  }

  @Test
  void testFormatChicago() throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(
        "DT_DATE_FORMAT(DT_DATE_NEW(2022,10,30,11,50,20), \"EEE, d MMM yyyy HH:mm:ss Z\")",
        "Sun, 30 Oct 2022 11:50:20 -0500",
        ExpressionConfiguration.builder()
            .locale(Locale.US)
            .zoneId(ZoneId.of("America/Chicago"))
            .build());
  }
}
