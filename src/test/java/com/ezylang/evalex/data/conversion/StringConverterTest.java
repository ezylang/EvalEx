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

class StringConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final StringConverter converter = new StringConverter();

  @Test
  void testString() {
    EvaluationValue converted = converter.convert("Hello World!", defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.STRING);
    assertThat(converted.getValue()).isEqualTo("Hello World!");
  }

  @Test
  void testCharacter() {
    EvaluationValue converted = converter.convert('P', defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.STRING);
    assertThat(converted.getValue()).isEqualTo("P");
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert("Hello")).isTrue();
    assertThat(converter.canConvert('P')).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert(new BigDecimal(3))).isFalse();
    assertThat(converter.canConvert(true)).isFalse();
  }

  @Test
  void testException() {
    assertThatThrownBy(
            () -> {
              converter.convert(7, defaultConfiguration);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.lang.Integer'");
  }
}
