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
package com.ezylang.evalex.operators;

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_UNARY;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The postfix operator annotation */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostfixOperator {

  /** Operator precedence, usually one from the constants in {@link OperatorIfc}. */
  int precedence() default OPERATOR_PRECEDENCE_UNARY;

  /** Operator associativity, defaults to <code>true</code>. */
  boolean leftAssociative() default true;
}
