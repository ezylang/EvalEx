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

/**
 * Converter interface to be implemented by configurable evaluation value converters. Converts an
 * arbitrary object to an {@link EvaluationValue}, using the specified configuration.
 */
public interface EvaluationValueConverterIfc {

  /**
   * Called whenever an object has to be converted to an {@link EvaluationValue}.
   *
   * @param object The object holding the value.
   * @param configuration The configuration to use.
   * @return The converted {@link EvaluationValue}.
   * @throws IllegalArgumentException if the object can't be converted.
   */
  EvaluationValue convertObject(Object object, ExpressionConfiguration configuration);
}
