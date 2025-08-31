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

import com.ezylang.evalex.config.TestConfigurationProvider.PostfixQuestionOperator;
import com.ezylang.evalex.config.TestConfigurationProvider.PrefixPlusPlusOperator;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.operators.arithmetic.InfixModuloOperator;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MapBasedOperatorDictionaryTest {

  @Test
  void testCreationOfOperators() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();
    OperatorIfc postfix = new PostfixQuestionOperator();
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(
            Map.entry("++", prefix), Map.entry("?", postfix), Map.entry("%", infix));

    assertThat(dictionary.hasPrefixOperator("++")).isTrue();
    assertThat(dictionary.hasPostfixOperator("?")).isTrue();
    assertThat(dictionary.hasInfixOperator("%")).isTrue();

    assertThat(dictionary.getPrefixOperator("++")).isEqualTo(prefix);
    assertThat(dictionary.getPostfixOperator("?")).isEqualTo(postfix);
    assertThat(dictionary.getInfixOperator("%")).isEqualTo(infix);

    assertThat(dictionary.hasPrefixOperator("A")).isFalse();
    assertThat(dictionary.hasPostfixOperator("B")).isFalse();
    assertThat(dictionary.hasInfixOperator("C")).isFalse();
  }

  @Test
  void testCaseInsensitivity() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();
    OperatorIfc postfix = new PostfixQuestionOperator();
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(
            Map.entry("PlusPlus", prefix),
            Map.entry("Question", postfix),
            Map.entry("Percent", infix));

    assertThat(dictionary.hasPrefixOperator("PlusPlus")).isTrue();
    assertThat(dictionary.hasPrefixOperator("plusplus")).isTrue();
    assertThat(dictionary.hasPrefixOperator("PLUSPLUS")).isTrue();

    assertThat(dictionary.hasPostfixOperator("Question")).isTrue();
    assertThat(dictionary.hasPostfixOperator("question")).isTrue();
    assertThat(dictionary.hasPostfixOperator("QUESTION")).isTrue();

    assertThat(dictionary.hasInfixOperator("Percent")).isTrue();
    assertThat(dictionary.hasInfixOperator("percent")).isTrue();
    assertThat(dictionary.hasInfixOperator("PERCENT")).isTrue();
  }

  @Test
  void testGetAvailablePrefixOperatorNames() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("++", prefix));

    assertThat(dictionary.getAvailablePrefixOperatorNames()).containsExactly("++");
  }

  @Test
  void testGetAvailablePostfixOperatorNames() {
    OperatorIfc postfix = new PostfixQuestionOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("?", postfix));

    assertThat(dictionary.getAvailablePostfixOperatorNames()).containsExactly("?");
  }

  @Test
  void testGetAvailableInfixOperatorNames() {
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("%", infix));

    assertThat(dictionary.getAvailableInfixOperatorNames()).containsExactly("%");
  }

  @Test
  void testGetAvailablePrefixOperators() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("++", prefix));

    assertThat(dictionary.getAvailablePrefixOperators()).containsExactly(prefix);
  }

  @Test
  void testGetAvailablePostfixOperators() {
    OperatorIfc postfix = new PostfixQuestionOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("?", postfix));

    assertThat(dictionary.getAvailablePostfixOperators()).containsExactly(postfix);
  }

  @Test
  void testGetAvailableInfixOperators() {
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(Map.entry("%", infix));

    assertThat(dictionary.getAvailableInfixOperators()).containsExactly(infix);
  }
}
