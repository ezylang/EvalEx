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

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.config.TestConfigurationProvider.DummyFunction;
import com.ezylang.evalex.data.DataAccessorIfc;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.MapBasedDataAccessor;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.operators.arithmetic.InfixPlusOperator;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExpressionConfigurationTest {

  @Test
  void testDefaultSetup() {
    ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration();

    assertThat(configuration.getMathContext())
        .isEqualTo(ExpressionConfiguration.DEFAULT_MATH_CONTEXT);
    assertThat(configuration.getOperatorDictionary())
        .isInstanceOf(MapBasedOperatorDictionary.class);
    assertThat(configuration.getFunctionDictionary())
        .isInstanceOf(MapBasedFunctionDictionary.class);
    assertThat(configuration.getDataAccessorSupplier().get())
        .isInstanceOf(MapBasedDataAccessor.class);
    assertThat(configuration.isArraysAllowed()).isTrue();
    assertThat(configuration.isStructuresAllowed()).isTrue();
    assertThat(configuration.isImplicitMultiplicationAllowed()).isTrue();
    assertThat(configuration.getPowerOfPrecedence())
        .isEqualTo(OperatorIfc.OPERATOR_PRECEDENCE_POWER);
    assertThat(configuration.getDefaultConstants())
        .containsAllEntriesOf(ExpressionConfiguration.StandardConstants);
    assertThat(configuration.getDecimalPlacesRounding())
        .isEqualTo(ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED);
    assertThat(configuration.isStripTrailingZeros()).isTrue();
    assertThat(configuration.isAllowOverwriteConstants()).isTrue();
    assertThat(configuration.isSingleQuoteStringLiteralsAllowed()).isFalse();
  }

  @Test
  void testWithAdditionalOperators() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalOperators(
                Map.entry("ADDED1", new InfixPlusOperator()),
                Map.entry("ADDED2", new InfixPlusOperator()));

    assertThat(configuration.getOperatorDictionary().hasInfixOperator("ADDED1")).isTrue();
    assertThat(configuration.getOperatorDictionary().hasInfixOperator("ADDED2")).isTrue();
  }

  @Test
  void testWithAdditionalFunctions() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalFunctions(
                Map.entry("ADDED1", new DummyFunction()), Map.entry("ADDED2", new DummyFunction()));

    assertThat(configuration.getFunctionDictionary().hasFunction("ADDED1")).isTrue();
    assertThat(configuration.getFunctionDictionary().hasFunction("ADDED2")).isTrue();
  }

  @Test
  void testCustomMathContext() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).build();

    assertThat(configuration.getMathContext()).isEqualTo(MathContext.DECIMAL32);
  }

  @Test
  void testCustomOperatorDictionary() {
    OperatorDictionaryIfc mockedOperatorDictionary = Mockito.mock(OperatorDictionaryIfc.class);

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().operatorDictionary(mockedOperatorDictionary).build();

    assertThat(configuration.getOperatorDictionary()).isEqualTo(mockedOperatorDictionary);
  }

  @Test
  void testCustomFunctionDictionary() {
    FunctionDictionaryIfc mockedFunctionDictionary = Mockito.mock(FunctionDictionaryIfc.class);

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().functionDictionary(mockedFunctionDictionary).build();

    assertThat(configuration.getFunctionDictionary()).isEqualTo(mockedFunctionDictionary);
  }

  @Test
  void testCustomDataAccessorSupplier() {
    DataAccessorIfc mockedDataAccessor = Mockito.mock(DataAccessorIfc.class);

    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().dataAccessorSupplier(() -> mockedDataAccessor).build();

    assertThat(configuration.getDataAccessorSupplier().get()).isEqualTo(mockedDataAccessor);
  }

  @Test
  void testDataAccessorSupplierReturnsNewInstance() {
    ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration();

    DataAccessorIfc accessor1 = configuration.getDataAccessorSupplier().get();
    DataAccessorIfc accessor2 = configuration.getDataAccessorSupplier().get();

    assertThat(accessor1).isNotEqualTo(accessor2);
  }

  @Test
  void testCustomConstants() {
    Map<String, EvaluationValue> constants =
        new HashMap<>() {
          {
            put("A", EvaluationValue.stringValue("a"));
            put("B", EvaluationValue.stringValue("b"));
          }
        };
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().defaultConstants(constants).build();

    assertThat(configuration.getDefaultConstants()).containsAllEntriesOf(constants);
  }

  @Test
  void testArraysAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().arraysAllowed(false).build();

    assertThat(configuration.isArraysAllowed()).isFalse();
  }

  @Test
  void testStructuresAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().structuresAllowed(false).build();

    assertThat(configuration.isStructuresAllowed()).isFalse();
  }

  @Test
  void testSingleQuoteStringLiteralsAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertThat(configuration.isSingleQuoteStringLiteralsAllowed()).isTrue();
  }

  @Test
  void testImplicitMultiplicationAllowed() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().implicitMultiplicationAllowed(false).build();

    assertThat(configuration.isImplicitMultiplicationAllowed()).isFalse();
  }

  @Test
  void testPowerOfPrecedence() {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder()
            .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER_HIGHER)
            .build();

    assertThat(configuration.getPowerOfPrecedence())
        .isEqualTo(OperatorIfc.OPERATOR_PRECEDENCE_POWER_HIGHER);
  }
}
