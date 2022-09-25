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

import java.util.Map;
import java.util.TreeMap;

/**
 * A default case-insensitive implementation of the data accessor that uses a local <code>
 * Map.Entry&lt;String, EvaluationValue&gt;</code> for storage.
 */
public class MapBasedDataAccessor implements DataAccessorIfc {

  private final Map<String, EvaluationValue> variables =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  @Override
  public EvaluationValue getData(String variable) {
    return variables.get(variable);
  }

  @Override
  public void setData(String variable, EvaluationValue value) {
    variables.put(variable, value);
  }
}
