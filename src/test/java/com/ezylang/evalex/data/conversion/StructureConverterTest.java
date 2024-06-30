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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StructureConverterTest {

  private final ExpressionConfiguration defaultConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  private final StructureConverter converter = new StructureConverter();

  @Test
  void testStructureMixed() {
    Map<String, Object> testMap = new HashMap<>();

    testMap.put("key1", "value1");
    testMap.put("key2", 4);
    testMap.put("key3", true);

    EvaluationValue converted = converter.convert(testMap, defaultConfiguration);

    assertThat(converted.getDataType()).isEqualTo(EvaluationValue.DataType.STRUCTURE);
    assertThat(converted.getStructureValue()).hasSize(3);

    assertThat(converted.getStructureValue().get("key1").isStringValue()).isTrue();
    assertThat(converted.getStructureValue().get("key1").getStringValue()).isEqualTo("value1");

    assertThat(converted.getStructureValue().get("key2").isNumberValue()).isTrue();
    assertThat(converted.getStructureValue().get("key2").getNumberValue())
        .isEqualTo(new BigDecimal("4"));

    assertThat(converted.getStructureValue().get("key3").isBooleanValue()).isTrue();
    assertThat(converted.getStructureValue().get("key3").getBooleanValue()).isTrue();
  }

  @Test
  void testMapEmpty() {
    EvaluationValue value = converter.convert(Collections.EMPTY_MAP, defaultConfiguration);

    assertThat(value.isStructureValue()).isTrue();
    assertThat(value.getStructureValue()).isEmpty();
  }

  @Test
  void testCanConvert() {
    assertThat(converter.canConvert(Collections.EMPTY_MAP)).isTrue();
  }

  @Test
  void testCanNotConvert() {
    assertThat(converter.canConvert(new int[] {1, 2, 3})).isFalse();
    assertThat(converter.canConvert(new BigDecimal(1))).isFalse();
  }
}
