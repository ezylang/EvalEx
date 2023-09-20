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
import org.junit.jupiter.api.Test;

class NumberConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final NumberConverter converter = new NumberConverter();

  @Test
  void testBigDecimal() {
    BigDecimal value = new BigDecimal("23");

    EvaluationValue converted = converter.convert(value, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getValue()).isEqualTo(value);
  }

  @Test
  void testDouble() {
    double value = Double.parseDouble("2.5");

    EvaluationValue converted = converter.convert(value, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("2.5");
  }

  @Test
  void testFloat() {
    double value = Float.parseFloat("3.5");

    EvaluationValue converted = converter.convert(value, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("3.5");
  }

  @Test
  void testInteger() {
    EvaluationValue converted = converter.convert(5, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("5");
  }

  @Test
  void testLong() {
    EvaluationValue converted = converter.convert(949345345343345673L, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("949345345343345673");
  }

  @Test
  void testShort() {
    double value = Short.parseShort("7");

    EvaluationValue converted = converter.convert(value, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("7.0");
  }

  @Test
  void testByte() {
    double value = Byte.parseByte("4");

    EvaluationValue converted = converter.convert(value, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NUMBER);
    assertThat(converted.getNumberValue().toPlainString()).isEqualTo("4.0");
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert(new BigDecimal(8))).isTrue();
    assertThat(converter.canConvert(Double.parseDouble("3.0"))).isTrue();
    assertThat(converter.canConvert(Float.parseFloat("2.0"))).isTrue();
    assertThat(converter.canConvert(3)).isTrue();
    assertThat(converter.canConvert(3L)).isTrue();
    assertThat(converter.canConvert(Short.parseShort("79"))).isTrue();
    assertThat(converter.canConvert(Byte.parseByte("2"))).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert("hello")).isFalse();
    assertThat(converter.canConvert(true)).isFalse();
  }

  @Test
  void testException() {
    assertThatThrownBy(
            () -> {
              converter.convert("not a number", defaultConfiguration);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.lang.String'");
  }
}
