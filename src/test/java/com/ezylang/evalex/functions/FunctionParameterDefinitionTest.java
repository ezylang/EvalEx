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
package com.ezylang.evalex.functions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FunctionParameterDefinitionTest {

  @Test
  void testCreation() {
    FunctionParameterDefinition definition =
        FunctionParameterDefinition.builder()
            .name("name")
            .isVarArg(true)
            .isLazy(true)
            .nonZero(true)
            .nonNegative(true)
            .build();

    assertThat(definition.getName()).isEqualTo("name");
    assertThat(definition.isVarArg()).isTrue();
    assertThat(definition.isLazy()).isTrue();
    assertThat(definition.isNonZero()).isTrue();
    assertThat(definition.isNonNegative()).isTrue();

    assertThat(definition)
        .hasToString(
            "FunctionParameterDefinition(name=name, isVarArg=true, isLazy=true, nonZero=true,"
                + " nonNegative=true)");
  }
}
