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
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ArrayConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final ArrayConverter converter = new ArrayConverter();

  @Test
  void testArrayMixed() {
    EvaluationValue value =
        converter.convert(
            Arrays.asList(new BigDecimal(1), new BigDecimal(2), "hello", null),
            defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(4);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("hello");
    assertThat(value.getArrayValue().get(3).isNullValue()).isTrue();
    assertThat(value.getArrayValue().get(3).getStringValue()).isNull();

    assertThat(value.getValue()).isInstanceOf(List.class);
  }

  @Test
  void testArrayEmpty() {
    EvaluationValue value = converter.convert(Collections.EMPTY_LIST, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).isEmpty();
  }

  @Test
  void testArrayFromJavaArray() {
    Instant now = Instant.now();
    Object[] array =
        new Object[] {"1", 2, new BigDecimal(3), null, EvaluationValue.dateTimeValue(now)};

    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(5);
    assertThat(value.getArrayValue().get(0).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3");
    assertThat(value.getArrayValue().get(3).isNullValue()).isTrue();
    assertThat(value.getArrayValue().get(3).getStringValue()).isNull();
    assertThat(value.getArrayValue().get(4).isDateTimeValue()).isTrue();
    assertThat(value.getArrayValue().get(4).getDateTimeValue()).isEqualTo(now);
  }

  @Test
  void testLongArray() {
    long[] array = new long[] {1, 2, 3};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3");
  }

  @Test
  void testIntArray() {
    int[] array = new int[] {1, 2, 3};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3");
  }

  @Test
  void testShortArray() {
    short[] array = new short[] {1, 2, 3};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3");
  }

  @Test
  void testDoubleArray() {
    double[] array = new double[] {1.0, 2.0, 3.0};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1.0");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2.0");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3.0");
  }

  @Test
  void testFloatArray() {
    float[] array = new float[] {1.0f, 2.0f, 3.0f};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1.0");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2.0");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3.0");
  }

  @Test
  void testBooleanArray() {
    boolean[] array = new boolean[] {true, false};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(2);
    assertThat(value.getArrayValue().get(0).isBooleanValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getBooleanValue()).isTrue();
    assertThat(value.getArrayValue().get(1).isBooleanValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getBooleanValue()).isFalse();
  }

  @Test
  void testByteArray() {
    byte[] array = new byte[] {1, 2, 3};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("1");
    assertThat(value.getArrayValue().get(1).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("2");
    assertThat(value.getArrayValue().get(2).isNumberValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("3");
  }

  @Test
  void testCharArray() {
    char[] array = new char[] {'a', 'b', 'c'};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(3);
    assertThat(value.getArrayValue().get(0).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("a");
    assertThat(value.getArrayValue().get(1).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("b");
    assertThat(value.getArrayValue().get(2).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(2).getStringValue()).isEqualTo("c");
  }

  @Test
  void testStringArray() {
    String[] array = new String[] {"hello", "world"};
    EvaluationValue value = converter.convert(array, defaultConfiguration);

    assertThat(value.isArrayValue()).isTrue();
    assertThat(value.getArrayValue()).hasSize(2);
    assertThat(value.getArrayValue().get(0).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(0).getStringValue()).isEqualTo("hello");
    assertThat(value.getArrayValue().get(1).isStringValue()).isTrue();
    assertThat(value.getArrayValue().get(1).getStringValue()).isEqualTo("world");
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert(new String[] {"1", "2", "3"})).isTrue();
    assertThat(converter.canConvert(Collections.EMPTY_LIST)).isTrue();
    assertThat(converter.canConvert(Arrays.asList(1, 2, 3))).isTrue();
    assertThat(converter.canConvert(new Integer[] {1, 2, 3})).isTrue();
    assertThat(converter.canConvert(new int[] {1, 2, 3})).isTrue();
    assertThat(converter.canConvert(new double[] {1.0, 2.0, 3.0})).isTrue();
    assertThat(converter.canConvert(new boolean[] {true, false})).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert(new BigDecimal(1))).isFalse();
  }

  @Test
  void testIllegalArgumentException() {
    assertThatThrownBy(() -> converter.convert("test", defaultConfiguration))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.lang.String'");
  }
}
