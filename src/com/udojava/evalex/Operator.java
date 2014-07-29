/*
 * Copyright 2012 Udo Klimaschewski
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

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Abstract definition of a supported operator. An operator is defined by its
 * name (pattern), precedence and if it is left- or right associative.
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski),Srinivasa Rao
 *         Boyina
 */
public abstract class Operator {
	/**
	 * This operators name (pattern).
	 */
	private String oper;
	/**
	 * Operators precedence.
	 */
	private int precedence;
	/**
	 * Operator is left associative.
	 */
	private boolean leftAssoc;

	/**
	 * Creates a new operator.
	 * 
	 * @param oper
	 *                The operator name (pattern).
	 * @param precedence
	 *                The operators precedence.
	 * @param leftAssoc
	 *                <code>true</code> if the operator is left associative,
	 *                else <code>false</code>.
	 */
	public Operator(String oper, int precedence, boolean leftAssoc) {
		this.oper = oper.toLowerCase();
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
	}

	public String getOper() {
		return oper;
	}

	public int getPrecedence() {
		return precedence;
	}

	public boolean isLeftAssoc() {
		return leftAssoc;
	}

	/**
	 * Implementation for this operator.
	 * 
	 * @param v1
	 *                Operand 1.
	 * @param v2
	 *                Operand 2.
	 * @return The result of the operation.
	 */
	public abstract BigDecimal eval(BigDecimal v1, BigDecimal v2,
			MathContext mc);
}
