/*
  Copyright 2012-2023 Udo Klimaschewski

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
package com.ezylang.evalex.data.conversion;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;

/** Converter interface used by the {@link DefaultEvaluationValueConverter}. */
public interface ConverterIfc {

  /**
   * Called to convert a previously checked data type.
   *
   * @param object The object to convert.
   * @param configuration The current expression configuration.
   * @return The converted value.
   */
  EvaluationValue convert(Object object, ExpressionConfiguration configuration);

  /**
   * Checks, if a given object can be converted by this converter.
   *
   * @param object The object to convert.
   * @return <code>true</code> if the object can be converted, false otherwise.
   */
  boolean canConvert(Object object);

  default IllegalArgumentException illegalArgument(Object object) {
    return new IllegalArgumentException(
        "Unsupported data type '" + object.getClass().getName() + "'");
  }
}
