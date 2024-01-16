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

import static com.ezylang.evalex.operators.OperatorIfc.OperatorType.PREFIX_OPERATOR;

import com.ezylang.evalex.config.ExpressionConfiguration;
import lombok.Getter;

/**
 * Abstract implementation of the {@link OperatorIfc}, used as base class for operator
 * implementations.
 */
public abstract class AbstractOperator implements OperatorIfc {

  @Getter private final int precedence;

  private final boolean leftAssociative;

  private final boolean operandsLazy;

  OperatorType type;

  /**
   * Creates a new operator and uses the {@link InfixOperator} annotation to create the operator
   * definition.
   */
  protected AbstractOperator() {
    InfixOperator infixAnnotation = getClass().getAnnotation(InfixOperator.class);
    PrefixOperator prefixAnnotation = getClass().getAnnotation(PrefixOperator.class);
    PostfixOperator postfixAnnotation = getClass().getAnnotation(PostfixOperator.class);
    if (infixAnnotation != null) {
      this.type = OperatorType.INFIX_OPERATOR;
      this.precedence = infixAnnotation.precedence();
      this.leftAssociative = infixAnnotation.leftAssociative();
      this.operandsLazy = infixAnnotation.isLazy();
    } else if (prefixAnnotation != null) {
      this.type = PREFIX_OPERATOR;
      this.precedence = prefixAnnotation.precedence();
      this.leftAssociative = prefixAnnotation.leftAssociative();
      this.operandsLazy = false;
    } else if (postfixAnnotation != null) {
      this.type = OperatorType.POSTFIX_OPERATOR;
      this.precedence = postfixAnnotation.precedence();
      this.leftAssociative = postfixAnnotation.leftAssociative();
      this.operandsLazy = false;
    } else {
      throw new OperatorAnnotationNotFoundException(this.getClass().getName());
    }
  }

  @Override
  public int getPrecedence(ExpressionConfiguration configuration) {
    return getPrecedence();
  }

  @Override
  public boolean isLeftAssociative() {
    return leftAssociative;
  }

  @Override
  public boolean isOperandLazy() {
    return operandsLazy;
  }

  @Override
  public boolean isPrefix() {
    return type == PREFIX_OPERATOR;
  }

  @Override
  public boolean isPostfix() {
    return type == OperatorType.POSTFIX_OPERATOR;
  }

  @Override
  public boolean isInfix() {
    return type == OperatorType.INFIX_OPERATOR;
  }
}
