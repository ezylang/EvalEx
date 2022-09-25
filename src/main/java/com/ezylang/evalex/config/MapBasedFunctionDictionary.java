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

import com.ezylang.evalex.functions.FunctionIfc;
import java.util.Map;
import java.util.TreeMap;

/**
 * A default case-insensitive implementation of the function dictionary that uses a local <code>
 * Map.Entry&lt;String, FunctionIfc&gt;</code> for storage.
 */
public class MapBasedFunctionDictionary implements FunctionDictionaryIfc {

  private final Map<String, FunctionIfc> functions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  /**
   * Creates a new function dictionary with the specified list of functions.
   *
   * @param functions variable number of arguments that specify the function names and definitions
   *     that will initially be added.
   * @return A newly created function dictionary with the specified functions.
   */
  @SuppressWarnings({"unchecked", "varargs"})
  public static FunctionDictionaryIfc ofFunctions(Map.Entry<String, FunctionIfc>... functions) {
    FunctionDictionaryIfc dictionary = new MapBasedFunctionDictionary();
    stream(functions).forEach(entry -> dictionary.addFunction(entry.getKey(), entry.getValue()));
    return dictionary;
  }

  @Override
  public FunctionIfc getFunction(String functionName) {
    return functions.get(functionName);
  }

  @Override
  public void addFunction(String functionName, FunctionIfc function) {
    functions.put(functionName, function);
  }
}
