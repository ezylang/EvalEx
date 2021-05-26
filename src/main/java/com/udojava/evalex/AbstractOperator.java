/*
 * Copyright 2018 Udo Klimaschewski
 *
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.udojava.evalex;

import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;

/**
 * Abstract implementation of an operator.
 */
public abstract class AbstractOperator extends AbstractLazyOperator implements Operator {


  /**
   * Creates a new unary operator.
   *
   * @param oper            The operator name (pattern).
   * @param precedence      The operators precedence.
   * @param leftAssoc       <code>true</code> if the operator is left associative,
   *                        else <code>false</code>.
   * @param booleanOperator Whether this operator is boolean.
   * @param unaryOperator   Whether the operator is unary (<code>true</code>) or not
   *                        (<code>false</code>).
   */
  protected AbstractOperator(String oper, int precedence, boolean leftAssoc,
      boolean booleanOperator, boolean unaryOperator) {
    super(oper, precedence, leftAssoc, booleanOperator, unaryOperator);
  }


  /**
   * Creates a new boolean operator.
   *
   * @param oper            The operator name (pattern).
   * @param precedence      The operators precedence.
   * @param leftAssoc       <code>true</code> if the operator is left associative,
   *                        else <code>false</code>.
   * @param booleanOperator Whether this operator is boolean.
   */
  protected AbstractOperator(String oper, int precedence, boolean leftAssoc,
      boolean booleanOperator) {
    super(oper, precedence, leftAssoc, booleanOperator);
  }


  /**
   * Creates a new operator.
   *
   * @param oper       The operator name (pattern).
   * @param precedence The operators precedence.
   * @param leftAssoc  <code>true</code> if the operator is left associative,
   *                   else <code>false</code>.
   */
  protected AbstractOperator(String oper, int precedence, boolean leftAssoc) {
    super(oper, precedence, leftAssoc);
  }


  /**
   * Implementation of this operator supporting either 1 or 2 operands.
   *
   * @param v1 The first operand expected for the operation.
   * @param v2 The second operand expected for the operation. For postfix unary operators, v2=null
   *           condition was added.
   * @return LazyNumber object. The result of the operation.
   */
  public LazyNumber eval(final LazyNumber v1, final LazyNumber v2) {
    if (v2 == null) {  // Condition to accept postfix unary operators (e.g. factorial operator '!')
      return new LazyNumber() {
        public BigDecimal eval() {
          return AbstractOperator.this.eval(v1.eval(), null);
        }

        public String getString() {
          return String.valueOf(AbstractOperator.this.eval(v1.eval(), null));
        }
      };
    } else {
      return new LazyNumber() {
        public BigDecimal eval() {
          return AbstractOperator.this.eval(v1.eval(), v2.eval());
        }

        public String getString() {
          return String.valueOf(AbstractOperator.this.eval(v1.eval(), v2.eval()));
        }
      };
    }
  }

}
