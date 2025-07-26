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

import com.ezylang.evalex.functions.FunctionIfc;
import java.util.Set;

/**
 * A function dictionary holds all the functions, that can be used in an expression. <br>
 * The default implementation is the {@link MapBasedFunctionDictionary}.
 */
public interface FunctionDictionaryIfc {

  /**
   * Allows to add a function to the dictionary. Implementation is optional, if you have a fixed set
   * of functions, this method can throw an exception.
   *
   * @param functionName The function name.
   * @param function The function implementation.
   */
  void addFunction(String functionName, FunctionIfc function);

  /**
   * Check if the dictionary has a function with that name.
   *
   * @param functionName The function name to look for.
   * @return <code>true</code> if a function was found or <code>false</code> if not.
   */
  default boolean hasFunction(String functionName) {
    return getFunction(functionName) != null;
  }

  /**
   * Get the function definition for a function name.
   *
   * @param functionName The name of the function.
   * @return The function definition or <code>null</code> if no function was found.
   */
  FunctionIfc getFunction(String functionName);

  /**
   * Get all available registered functions by their name.
   *
   * @return An immutable copy of all available function names.
   */
  Set<String> getAvailableFunctions();
}
