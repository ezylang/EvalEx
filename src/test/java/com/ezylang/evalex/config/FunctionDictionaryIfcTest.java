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
package com.ezylang.evalex.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.functions.FunctionIfc;
import org.junit.jupiter.api.Test;

class FunctionDictionaryIfcTest {

  private final FunctionDictionaryIfc functionDictionaryIfc =
      new FunctionDictionaryIfc() {
        @Override
        public void addFunction(String functionName, FunctionIfc function) {}

        @Override
        public FunctionIfc getFunction(String functionName) {
          return null;
        }
      };

  @Test
  void testGetAvailableFunctionNamesIsUnsupportedByDefault() {
    assertThatThrownBy(functionDictionaryIfc::getAvailableFunctionNames)
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailableFunctionsIsUnsupportedByDefault() {
    assertThatThrownBy(functionDictionaryIfc::getAvailableFunctions)
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
