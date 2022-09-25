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
package com.ezylang.evalex.data;

/**
 * A data accessor is responsible for accessing data, e.g. variable and constant values during an
 * expression evaluation. The default implementation for setting and reading local data is the
 * {@link MapBasedDataAccessor}.
 */
public interface DataAccessorIfc {

  /**
   * Retrieves a data value.
   *
   * @param variable The variable name, e.g. a variable or constant name.
   * @return The data value, or <code>null</code> if not found.
   */
  EvaluationValue getData(String variable);

  /**
   * Sets a data value.
   *
   * @param variable The variable name, e.g. a variable or constant name.
   * @param value The value to set.
   */
  void setData(String variable, EvaluationValue value);
}
