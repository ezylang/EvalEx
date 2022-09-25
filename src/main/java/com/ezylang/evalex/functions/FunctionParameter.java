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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation to define a function parameter. */
@Documented
@Target(ElementType.TYPE)
@Repeatable(FunctionParameters.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParameter {

  /** The parameter name. */
  String name();

  /** If the parameter is lazily evaluated. Defaults to false. */
  boolean isLazy() default false;

  /** If the parameter is a variable arg type (repeatable). Defaults to false. */
  boolean isVarArg() default false;

  /** If the parameter does not allow zero values. */
  boolean nonZero() default false;

  /** If the parameter does not allow negative values. */
  boolean nonNegative() default false;
}
