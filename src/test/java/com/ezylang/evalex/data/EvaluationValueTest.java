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
package com.ezylang.evalex.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;
import com.ezylang.evalex.parser.Token.TokenType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EvaluationValueTest {

  @Test
  void testUnsupportedDataType() {
    assertThatThrownBy(() -> new EvaluationValue(Locale.FRANCE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.util.Locale'");
  }

  @Test
  void testString() {
    EvaluationValue value = new EvaluationValue("Hello World");

    assertThat(value.isStringValue()).isTrue();
    assertThat(value.isNumberValue()).isFalse();
    assertThat(value.isBooleanValue()).isFalse();
    assertThat(value.isStructureValue()).isFalse();
    assertThat(value.isArrayValue()).isFalse();
    assertThat(value.isExpressionNode()).isFalse();
    assertDataIsCorrect(
        value, "Hello World", BigDecimal.ZERO, false, Instant.EPOCH, Duration.ZERO, String.class);
  }

  @Test
  void testStringBuilder() {
    EvaluationValue value = new EvaluationValue(new StringBuilder("Hello StringBuilder World"));

    assertThat(value.isStringValue()).isTrue();
    assertDataIsCorrect(
        value,
        "Hello StringBuilder World",
        BigDecimal.ZERO,
        false,
        Instant.EPOCH,
        Duration.ZERO,
        String.class);
  }

  @Test
  void testCharacter() {
    EvaluationValue value = new EvaluationValue('a');

    assertThat(value.isStringValue()).isTrue();
    assertDataIsCorrect(
        value, "a", BigDecimal.ZERO, false, Instant.EPOCH, Duration.ZERO, String.class);
  }

  @Test
  void testBooleanTrue() {
    EvaluationValue value = new EvaluationValue(true);

    assertThat(value.isBooleanValue()).isTrue();
    assertThat(value.isNumberValue()).isFalse();
    assertThat(value.isStringValue()).isFalse();
    assertThat(value.isStructureValue()).isFalse();
    assertThat(value.isArrayValue()).isFalse();
    assertThat(value.isExpressionNode()).isFalse();
    assertDataIsCorrect(
        value, "true", BigDecimal.ONE, true, Instant.EPOCH, Duration.ZERO, Boolean.class);
  }

  @Test
  void testBooleanFalse() {
    EvaluationValue value = new EvaluationValue(false);

    assertThat(value.isBooleanValue()).isTrue();
    assertDataIsCorrect(
        value, "false", BigDecimal.ZERO, false, Instant.EPOCH, Duration.ZERO, Boolean.class);
  }

  @Test
  void testBooleanString() {
    EvaluationValue value = new EvaluationValue("true");

    assertThat(value.isStringValue()).isTrue();
    assertDataIsCorrect(
        value, "true", BigDecimal.ONE, true, Instant.EPOCH, Duration.ZERO, String.class);
  }

  @Test
  void testBooleanNumberZero() {
    EvaluationValue value = new EvaluationValue(BigDecimal.ZERO);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value, "0", BigDecimal.ZERO, false, Instant.EPOCH, Duration.ZERO, BigDecimal.class);
  }

  @Test
  void testInstant() {
    Instant instant = Instant.parse("2022-10-30T00:00:00Z");
    EvaluationValue value = new EvaluationValue(instant);

    assertThat(value.isDateTimeValue()).isTrue();
    assertDataIsCorrect(
        value, instant.toString(), BigDecimal.ZERO, false, instant, Duration.ZERO, Instant.class);
  }

  @Test
  void testLocalDate() {
    ZoneId zoneId = ZoneId.of("UTC+2");
    LocalDate localDate = LocalDate.parse("2022-10-30");
    EvaluationValue value = new EvaluationValue(localDate);

    assertThat(value.isDateTimeValue()).isTrue();
    assertDataIsCorrect(
        value,
        "2022-10-30T00:00:00Z",
        BigDecimal.ZERO,
        false,
        Instant.parse("2022-10-30T00:00:00Z"),
        Duration.ZERO,
        Instant.class);
  }

  @Test
  void testLocalDateTime() {
    ZoneId zoneId = ZoneId.of("UTC+2");
    LocalDateTime localDateTime = LocalDateTime.parse("2022-10-30T11:20:30");
    EvaluationValue value = new EvaluationValue(localDateTime, zoneId);

    assertThat(value.isDateTimeValue()).isTrue();
    assertDataIsCorrect(
        value,
        "2022-10-30T09:20:30Z",
        BigDecimal.ZERO,
        false,
        localDateTime.atZone(zoneId).toInstant(),
        Duration.ZERO,
        Instant.class);
  }

  @Test
  void testZonedDateTime() {
    ZonedDateTime zonedDateTime =
        ZonedDateTime.of(LocalDateTime.of(2022, 10, 30, 11, 20, 30), ZoneId.of("GMT+05:30"));
    EvaluationValue value = new EvaluationValue(zonedDateTime);

    assertThat(value.isDateTimeValue()).isTrue();
    assertDataIsCorrect(
        value,
        zonedDateTime.toInstant().toString(),
        BigDecimal.ZERO,
        false,
        zonedDateTime.toInstant(),
        Duration.ZERO,
        Instant.class);
  }

  @Test
  void testOffsetDateTime() {
    OffsetDateTime offsetDateTime =
        OffsetDateTime.of(LocalDateTime.of(2022, 10, 30, 11, 20, 30), ZoneOffset.of("+05:30"));
    EvaluationValue value = new EvaluationValue(offsetDateTime);

    assertThat(value.isDateTimeValue()).isTrue();
    assertDataIsCorrect(
        value,
        offsetDateTime.toInstant().toString(),
        BigDecimal.ZERO,
        false,
        offsetDateTime.toInstant(),
        Duration.ZERO,
        Instant.class);
  }

  @Test
  void testStringDateTime() {
    EvaluationValue value = new EvaluationValue("2022-10-30T11:20:30Z");

    assertThat(value.isDateTimeValue()).isFalse();
    assertDataIsCorrect(
        value,
        "2022-10-30T11:20:30Z",
        BigDecimal.ZERO,
        false,
        Instant.parse("2022-10-30T11:20:30Z"),
        Duration.ZERO,
        String.class);
  }

  @Test
  void testDuration() {
    EvaluationValue value = new EvaluationValue(Duration.ofMinutes(1));

    assertThat(value.isDurationValue()).isTrue();
    assertDataIsCorrect(
        value,
        "PT1M",
        BigDecimal.ZERO,
        false,
        Instant.EPOCH,
        Duration.ofMinutes(1),
        Duration.class);
  }

  @Test
  void testStringDuration() {
    EvaluationValue value = new EvaluationValue("PT24H");

    assertThat(value.isDurationValue()).isFalse();
    assertDataIsCorrect(
        value, "PT24H", BigDecimal.ZERO, false, Instant.EPOCH, Duration.ofHours(24), String.class);
  }

  @Test
  void testBigDecimal() {
    EvaluationValue value = new EvaluationValue(new BigDecimal("123.5"));

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "123.5",
        new BigDecimal("123.5"),
        true,
        Instant.ofEpochMilli(123),
        Duration.ofMillis(123),
        BigDecimal.class);
  }

  @Test
  void testFloat() {
    EvaluationValue value = new EvaluationValue((float) 4.5);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "4.5",
        BigDecimal.valueOf((float) 4.5),
        true,
        Instant.ofEpochMilli(4),
        Duration.ofMillis(4),
        BigDecimal.class);
  }

  @Test
  void testDouble() {
    EvaluationValue value = new EvaluationValue(8.5);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "8.5",
        BigDecimal.valueOf(8.5),
        true,
        Instant.ofEpochMilli(8),
        Duration.ofMillis(8),
        BigDecimal.class);
  }

  @Test
  void testLong() {
    EvaluationValue value = new EvaluationValue(6L);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "6",
        new BigDecimal(6),
        true,
        Instant.ofEpochMilli(6),
        Duration.ofMillis(6),
        BigDecimal.class);
  }

  @Test
  void testInteger() {
    EvaluationValue value = new EvaluationValue(5);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "5",
        new BigDecimal(5),
        true,
        Instant.ofEpochMilli(5),
        Duration.ofMillis(5),
        BigDecimal.class);
  }

  @Test
  void testShort() {
    EvaluationValue value = new EvaluationValue((short) 4);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "4",
        new BigDecimal(4),
        true,
        Instant.ofEpochMilli(4),
        Duration.ofMillis(4),
        BigDecimal.class);
  }

  @Test
  void testByte() {
    EvaluationValue value = new EvaluationValue((byte) 3);

    assertThat(value.isNumberValue()).isTrue();
    assertDataIsCorrect(
        value,
        "3",
        new BigDecimal(3),
        true,
        Instant.ofEpochMilli(3),
        Duration.ofMillis(3),
        BigDecimal.class);
  }

  @Test
  void testArray() {
    EvaluationValue value =
        new EvaluationValue(Arrays.asList(new BigDecimal(1), new BigDecimal(2)));

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.isNumberValue()).isFalse();
    assertThat(value.isBooleanValue()).isFalse();
    assertThat(value.isStructureValue()).isFalse();
    assertThat(value.isStringValue()).isFalse();
    assertThat(value.isExpressionNode()).isFalse();

    assertThat(value.getArrayValue()).hasSize(2);
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");

    assertThat(value.getValue()).isInstanceOf(List.class);
  }

  @Test
  void testArrayEmpty() {
    EvaluationValue value = new EvaluationValue(new BigDecimal(1));

    assertThat(value.getArrayValue()).isEmpty();
  }

  @Test
  void testStructure() {
    Map<String, Object> structure = new HashMap<>();
    structure.put("a", "Hello");
    structure.put("b", new BigDecimal(99));
    EvaluationValue value = new EvaluationValue(structure);

    assertThat(value.isStructureValue()).isTrue();
    assertThat(value.isNumberValue()).isFalse();
    assertThat(value.isBooleanValue()).isFalse();
    assertThat(value.isStringValue()).isFalse();
    assertThat(value.isArrayValue()).isFalse();
    assertThat(value.isExpressionNode()).isFalse();

    assertThat(value.getStructureValue()).hasSize(2);
    assertThat(value.getStructureValue().get("a").getStringValue()).isEqualTo("Hello");
    assertThat(value.getStructureValue().get("b").getStringValue()).isEqualTo("99");
    assertThat(value.getValue()).isInstanceOf(Map.class);
  }

  @Test
  void testStructureEmpty() {
    EvaluationValue value = new EvaluationValue(new BigDecimal(1));

    assertThat(value.getStructureValue()).isEmpty();
  }

  @Test
  void testExpressionNode() {
    ASTNode node = new ASTNode(new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT));
    EvaluationValue value = new EvaluationValue(node);

    assertThat(value.isExpressionNode()).isTrue();
    assertThat(value.isNumberValue()).isFalse();
    assertThat(value.isBooleanValue()).isFalse();
    assertThat(value.isStructureValue()).isFalse();
    assertThat(value.isArrayValue()).isFalse();
    assertThat(value.isStringValue()).isFalse();

    assertDataIsCorrect(
        value,
        "ASTNode(parameters=[], token=Token(startPosition=1, value=a, type=VARIABLE_OR_CONSTANT))",
        BigDecimal.ZERO,
        false,
        Instant.EPOCH,
        Duration.ZERO,
        ASTNode.class);
  }

  @Test
  void testExpressionNodeReturnsNull() {
    EvaluationValue value = new EvaluationValue(false);
    assertThat(value.getExpressionNode()).isNull();
  }

  @Test
  void testNumberOfString() {
    EvaluationValue value = EvaluationValue.numberOfString("123.987", MathContext.DECIMAL128);

    assertThat(value.isNumberValue()).isTrue();
    assertThat(value.getNumberValue()).isEqualTo(new BigDecimal("123.987", MathContext.DECIMAL128));
    assertThat(value).hasToString("EvaluationValue(value=123.987, dataType=NUMBER)");
  }

  @Test
  void testCompare() {
    assertThat(new EvaluationValue("Hello")).isEqualByComparingTo(new EvaluationValue("Hello"));
    assertThat(new EvaluationValue(123)).isEqualByComparingTo(new EvaluationValue(123));
    assertThat(new EvaluationValue(true)).isEqualByComparingTo(new EvaluationValue(true));

    assertThat(new EvaluationValue("Hello")).isGreaterThan(new EvaluationValue("Hell"));
    assertThat(new EvaluationValue(124)).isGreaterThan(new EvaluationValue(123));
    assertThat(new EvaluationValue(true)).isGreaterThan(new EvaluationValue(false));

    assertThat(new EvaluationValue("Hell")).isLessThan(new EvaluationValue("Hello"));
    assertThat(new EvaluationValue(123)).isLessThan(new EvaluationValue(124));
    assertThat(new EvaluationValue(false)).isLessThan(new EvaluationValue(true));
  }

  @Test
  void testDoubleMathContext() {
    assertThat(new EvaluationValue(3.9876, MathContext.DECIMAL64).getNumberValue())
        .isEqualByComparingTo("3.9876");

    assertThat(new EvaluationValue(3.9876, new MathContext(3)).getNumberValue())
        .isEqualByComparingTo("3.99");
  }

  @Test
  void nestedEvaluationValue() {
    try {
      EvaluationValue value1 = new EvaluationValue("Hello");
      EvaluationValue value2 = new EvaluationValue("World");

      Map<String, EvaluationValue> structure = new HashMap<>();
      structure.put("a", value1);
      structure.put("b", value2);

      EvaluationValue structureMap = new EvaluationValue(structure);

      Expression exp = new Expression("value.a == \"Hello\"").with("value", structureMap);

      EvaluationValue result = exp.evaluate();
      assertThat(result.getBooleanValue()).isTrue();
    } catch (EvaluationException | ParseException e) {
      e.printStackTrace();
    }
  }

  private void assertDataIsCorrect(
      EvaluationValue value,
      String stringValue,
      BigDecimal numberValue,
      Boolean booleanValue,
      Instant dateTimeValue,
      Duration durationValue,
      Class<?> valueInstance) {
    assertThat(value.getStringValue()).isEqualTo(stringValue);
    assertThat(value.getNumberValue()).isEqualTo(numberValue);
    assertThat(value.getBooleanValue()).isEqualTo(booleanValue);
    assertThat(value.getDateTimeValue()).isEqualTo(dateTimeValue);
    assertThat(value.getDurationValue()).isEqualTo(durationValue);
    assertThat(value.getValue()).isInstanceOf(valueInstance);
  }
}
