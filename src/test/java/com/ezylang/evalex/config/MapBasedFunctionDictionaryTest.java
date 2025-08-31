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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.functions.basic.MaxFunction;
import com.ezylang.evalex.functions.basic.MinFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MapBasedFunctionDictionaryTest {

  @Test
  void testCreationOfFunctions() {
    FunctionIfc min = new MinFunction();
    FunctionIfc max = new MaxFunction();

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary =
        MapBasedFunctionDictionary.ofFunctions(Map.entry("min", min), Map.entry("max", max));

    assertThat(dictionary.getAvailableFunctionNames()).containsExactlyInAnyOrder("min", "max");
    assertThat(dictionary.getFunction("min")).isEqualTo(min);
    assertThat(dictionary.getFunction("max")).isEqualTo(max);
  }

  @Test
  void testCaseInsensitivity() {
    FunctionIfc min = new MinFunction();
    FunctionIfc max = new MaxFunction();

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary =
        MapBasedFunctionDictionary.ofFunctions(Map.entry("Min", min), Map.entry("MAX", max));

    assertThat(dictionary.hasFunction("min")).isTrue();
    assertThat(dictionary.hasFunction("MIN")).isTrue();
    assertThat(dictionary.hasFunction("Min")).isTrue();
    assertThat(dictionary.hasFunction("max")).isTrue();
    assertThat(dictionary.hasFunction("MAX")).isTrue();
    assertThat(dictionary.hasFunction("Max")).isTrue();
  }

  @Test
  void testGetAvailableFunctionNames() {
    HashMap<String, FunctionIfc> functionsToRegister = new HashMap<>();
    functionsToRegister.put("min", new MinFunction());
    functionsToRegister.put("max", new MaxFunction());

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary = MapBasedFunctionDictionary.ofFunctions();
    functionsToRegister.forEach(dictionary::addFunction);

    assertThat(dictionary.getAvailableFunctionNames())
        .containsExactlyInAnyOrderElementsOf(functionsToRegister.keySet());
  }

  @Test
  void testGetAvailableFunctionNamesEmpty() {
    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc functionDictionaryIfc = MapBasedFunctionDictionary.ofFunctions();

    assertThat(functionDictionaryIfc.getAvailableFunctionNames()).isEmpty();
  }

  @Test
  void testGetAvailableFunctionNamesReturnsAnImmutableCopy() {
    @SuppressWarnings({"unchecked", "varargs"})
    Set<String> availableFunctions =
        MapBasedFunctionDictionary.ofFunctions().getAvailableFunctionNames();

    assertThatThrownBy(availableFunctions::clear).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetAvailableFunctions() {
    HashMap<String, FunctionIfc> functionsToRegister = new HashMap<>();
    functionsToRegister.put("min", new MinFunction());
    functionsToRegister.put("max", new MaxFunction());

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary = MapBasedFunctionDictionary.ofFunctions();
    functionsToRegister.forEach(dictionary::addFunction);

    assertThat(dictionary.getAvailableFunctions())
        .containsExactlyInAnyOrderElementsOf(functionsToRegister.values());
  }

  @Test
  void testGetAvailableFunctionsEmpty() {
    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc functionDictionaryIfc = MapBasedFunctionDictionary.ofFunctions();

    assertThat(functionDictionaryIfc.getAvailableFunctions()).isEmpty();
  }

  @Test
  void testGetAvailableFunctionsReturnsAnImmutableCopy() {
    @SuppressWarnings({"unchecked", "varargs"})
    Set<FunctionIfc> availableFunctions =
        MapBasedFunctionDictionary.ofFunctions().getAvailableFunctions();

    assertThatThrownBy(availableFunctions::clear).isInstanceOf(UnsupportedOperationException.class);
  }
}
