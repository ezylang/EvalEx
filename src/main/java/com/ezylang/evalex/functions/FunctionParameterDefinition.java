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
package com.ezylang.evalex.functions;

import lombok.Builder;
import lombok.Value;

/** Definition of a function parameter. */
@Value
@Builder
public class FunctionParameterDefinition {

  /** Name of the parameter, useful for error messages etc. */
  String name;

  /**
   * Whether this parameter is a variable argument parameter (can be repeated).
   *
   * @see com.ezylang.evalex.functions.basic.MinFunction for an example.
   */
  boolean isVarArg;

  /**
   * Set to true, the parameter will not be evaluated in advance, but the corresponding {@link
   * com.ezylang.evalex.parser.ASTNode} will be passed as a parameter value.
   *
   * @see com.ezylang.evalex.functions.basic.IfFunction for an example.
   */
  boolean isLazy;

  /** If the parameter does not allow zero values. */
  boolean nonZero;

  /** If the parameter does not allow negative values. */
  boolean nonNegative;
}
