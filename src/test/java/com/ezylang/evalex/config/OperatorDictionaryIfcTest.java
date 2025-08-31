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

import com.ezylang.evalex.operators.OperatorIfc;
import org.junit.jupiter.api.Test;

class OperatorDictionaryIfcTest {

  private OperatorDictionaryIfc operatorDictionaryIfc =
      new OperatorDictionaryIfc() {

        @Override
        public void addOperator(String operatorString, OperatorIfc operator) {}

        @Override
        public OperatorIfc getPrefixOperator(String operatorString) {
          return null;
        }

        @Override
        public OperatorIfc getPostfixOperator(String operatorString) {
          return null;
        }

        @Override
        public OperatorIfc getInfixOperator(String operatorString) {
          return null;
        }
      };

  @Test
  void testGetAvailablePrefixOperatorsIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailablePrefixOperators())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailablePrefixOperatorNamesIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailablePrefixOperatorNames())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailableInfixOperatorsIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailableInfixOperators())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailableInfixOperatorNamesIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailableInfixOperatorNames())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailablePostfixOperatorsIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailablePostfixOperators())
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailablePostfixOperatorNamesIsUnsupportedByDefault() {
    assertThatThrownBy(() -> operatorDictionaryIfc.getAvailablePostfixOperatorNames())
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
