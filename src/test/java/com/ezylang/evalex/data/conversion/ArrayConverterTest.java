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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import java.math.BigDecimal;
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
  void testCanConvert() {
    assertThat(converter.canConvert(Collections.EMPTY_LIST)).isTrue();
    assertThat(converter.canConvert(Arrays.asList(1, 2, 3))).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert(new String[] {"1", "2", "3"})).isFalse();
    assertThat(converter.canConvert(new BigDecimal(1))).isFalse();
  }
}
