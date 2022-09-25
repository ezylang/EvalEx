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

import static java.util.Arrays.stream;

import com.ezylang.evalex.operators.OperatorIfc;
import java.util.Map;
import java.util.TreeMap;

/**
 * A default case-insensitive implementation of the operator dictionary that uses a local <code>
 * Map.Entry&lt;String,OperatorIfc&gt;</code> for storage.
 */
public class MapBasedOperatorDictionary implements OperatorDictionaryIfc {

  final Map<String, OperatorIfc> prefixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  final Map<String, OperatorIfc> postfixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  final Map<String, OperatorIfc> infixOperators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  /**
   * Creates a new operator dictionary with the specified list of operators.
   *
   * @param operators variable number of arguments that specify the operator names and definitions
   *     that will initially be added.
   * @return A newly created operator dictionary with the specified operators.
   */
  @SuppressWarnings({"unchecked", "varargs"})
  public static OperatorDictionaryIfc ofOperators(Map.Entry<String, OperatorIfc>... operators) {
    OperatorDictionaryIfc dictionary = new MapBasedOperatorDictionary();
    stream(operators).forEach(entry -> dictionary.addOperator(entry.getKey(), entry.getValue()));
    return dictionary;
  }

  @Override
  public void addOperator(String operatorString, OperatorIfc operator) {
    if (operator.isPrefix()) {
      prefixOperators.put(operatorString, operator);
    } else if (operator.isPostfix()) {
      postfixOperators.put(operatorString, operator);
    } else {
      infixOperators.put(operatorString, operator);
    }
  }

  @Override
  public OperatorIfc getPrefixOperator(String operatorString) {
    return prefixOperators.get(operatorString);
  }

  @Override
  public OperatorIfc getPostfixOperator(String operatorString) {
    return postfixOperators.get(operatorString);
  }

  @Override
  public OperatorIfc getInfixOperator(String operatorString) {
    return infixOperators.get(operatorString);
  }
}
