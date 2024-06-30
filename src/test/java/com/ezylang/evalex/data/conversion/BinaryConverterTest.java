/*
  Copyright 2012-2024 Udo Klimaschewski

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
import org.junit.jupiter.api.Test;

class BinaryConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final BinaryConverter converter = new BinaryConverter();

  @Test
  void testObject() {
    Object object = new Object();

    EvaluationValue converted = converter.convert(object, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.BINARY);
    assertThat(converted.getValue()).isSameAs(object);
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert(new Object())).isTrue();
  }
}
