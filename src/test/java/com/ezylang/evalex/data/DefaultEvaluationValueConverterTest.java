package com.ezylang.evalex.data;

import com.ezylang.evalex.config.ExpressionConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
  void testException() {
    assertThatThrownBy(() -> converter.convertObject(new int[] {1, 2, 3}, defaultConfiguration))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported data type '[I'");
  }
}
