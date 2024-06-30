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
package com.ezylang.evalex.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.conversion.DefaultEvaluationValueConverter;
import org.junit.jupiter.api.Test;

class DefaultEvaluationValueConverterTest {

  private final DefaultEvaluationValueConverter converter = new DefaultEvaluationValueConverter();
  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  @Test
  void testNull() {
    EvaluationValue converted = converter.convertObject(null, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.NULL);
  }

  @Test
  void testNestedEvaluationValueNull() {
    EvaluationValue converted =
        converter.convertObject(EvaluationValue.stringValue("Hello"), defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.STRING);
    assertThat(converted.getStringValue()).isEqualTo("Hello");
  }

  @Test
  void testDefaultEvaluationWithBinaryAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().binaryAllowed(true).build();
    final Object rawValue = new Object();
    EvaluationValue converted = converter.convertObject(rawValue, configuration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.BINARY);
    assertThat(converted.isBinaryValue()).isTrue();
    assertThat(converted.getValue()).isSameAs(rawValue);
  }

  @Test
  void testExceptionWithBinaryNotAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().binaryAllowed(false).build();
    final Error error = new Error();
    assertThatThrownBy(() -> converter.convertObject(error, configuration))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type 'java.lang.Error'");
  }
}
