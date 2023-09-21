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

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MapBasedDataAccessorTest {

  @Test
  void testSetGetData() {
    DataAccessorIfc dataAccessor = new MapBasedDataAccessor();

    EvaluationValue num = EvaluationValue.numberValue(new BigDecimal("123"));
    EvaluationValue string = EvaluationValue.stringValue("hello");
    EvaluationValue bool = EvaluationValue.booleanValue(true);

    dataAccessor.setData("num", num);
    dataAccessor.setData("string", string);
    dataAccessor.setData("bool", bool);

    assertThat(dataAccessor.getData("num")).isEqualTo(num);
    assertThat(dataAccessor.getData("string")).isEqualTo(string);
    assertThat(dataAccessor.getData("bool")).isEqualTo(bool);
  }

  @Test
  void testCaseInsensitivity() {
    DataAccessorIfc dataAccessor = new MapBasedDataAccessor();

    EvaluationValue num = EvaluationValue.numberValue(new BigDecimal("123"));
    dataAccessor.setData("Hello", num);

    assertThat(dataAccessor.getData("Hello")).isEqualTo(num);
    assertThat(dataAccessor.getData("hello")).isEqualTo(num);
    assertThat(dataAccessor.getData("HELLO")).isEqualTo(num);
  }
}
