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

import com.ezylang.evalex.operators.OperatorIfc;
import java.util.Set;

/**
 * An operator dictionary holds all the operators, that can be used in an expression. <br>
 * The default implementation is the {@link MapBasedOperatorDictionary}.
 */
public interface OperatorDictionaryIfc {

  /**
   * Allows to add an operator to the dictionary. Implementation is optional, if you have a fixed
   * set of operators, this method can throw an exception.
   *
   * @param operatorString The operator name.
   * @param operator The operator implementation.
   */
  void addOperator(String operatorString, OperatorIfc operator);

  /**
   * Check if the dictionary has a prefix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasPrefixOperator(String operatorString) {
    return getPrefixOperator(operatorString) != null;
  }

  /**
   * Check if the dictionary has a postfix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasPostfixOperator(String operatorString) {
    return getPostfixOperator(operatorString) != null;
  }

  /**
   * Check if the dictionary has an infix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasInfixOperator(String operatorString) {
    return getInfixOperator(operatorString) != null;
  }

  /**
   * Get the operator definition for a prefix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getPrefixOperator(String operatorString);

  /**
   * Get the operator definition for a postfix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getPostfixOperator(String operatorString);

  /**
   * Get the operator definition for an infix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getInfixOperator(String operatorString);

  /**
   * Get all prefix operator names in current configuration.
   *
   * @return A set of all defined prefix operator names.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<String> getAvailablePrefixOperatorNames() {
    throw new UnsupportedOperationException("Operation not supported");
  }

  /**
   * Get all postfix operator names in current configuration.
   *
   * @return A set of all defined postfix operator names.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<String> getAvailablePostfixOperatorNames() {
    throw new UnsupportedOperationException("Operation not supported");
  }

  /**
   * Get all infix operator names in current configuration.
   *
   * @return A set of all defined infix operator names.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<String> getAvailableInfixOperatorNames() {
    throw new UnsupportedOperationException("Operation not supported");
  }

  /**
   * Get all prefix operators in current configuration.
   *
   * @return A set of all defined prefix operators.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<OperatorIfc> getAvailablePrefixOperators() {
    throw new UnsupportedOperationException("Operation not supported");
  }

  /**
   * Get all postfix operators in current configuration.
   *
   * @return A set of all defined postfix operators.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<OperatorIfc> getAvailablePostfixOperators() {
    throw new UnsupportedOperationException("Operation not supported");
  }

  /**
   * Get all infix operators in current configuration.
   *
   * @return A set of all defined infix operators.
   * @throws UnsupportedOperationException when this operation is not supported by the
   *     implementation.
   */
  default Set<OperatorIfc> getAvailableInfixOperators() {
    throw new UnsupportedOperationException("Operation not supported");
  }
}
