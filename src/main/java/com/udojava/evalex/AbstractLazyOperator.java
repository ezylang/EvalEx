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

/**
 * Abstract implementation of an operator.
 */
public abstract class AbstractLazyOperator implements LazyOperator {
	
	
	
	/**
	 * This operators name (pattern).
	 */
	protected String oper;
	
	/**
	 * Operators precedence.
	 */
	protected int precedence;
	
	/**
	 * Operator is left associative.
	 */
	protected boolean leftAssoc;
	
	/**
	 * Number of operands expected for this operator.
	 */
	protected int numberOperands;
	
	/**
	 * Number of operands expected for this operator.
	 */
	protected boolean booleanOperator = false;

	
	
	/**
	 * Creates a new boolean operator.
	 * 
	 * @param oper
	 *            The operator name (pattern).
	 * @param precedence
	 *            The operators precedence.
	 * @param leftAssoc
	 *            <code>true</code> if the operator is left associative,
 	 *            else <code>false</code>.
 	 * @param numberOperands
	 *            The number of operands that are expected for this operator.
	 * @param booleanOperator
	 *            Whether this operator is boolean.
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc, int numberOperands, boolean booleanOperator) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.numberOperands = numberOperands;
		this.booleanOperator = booleanOperator;
	}
	
	

	/**
	 * Creates a new operator.
	 * 
	 * @param oper
	 *            The operator name (pattern).
	 * @param precedence
	 *            The operators precedence.
	 * @param leftAssoc
	 *            <code>true</code> if the operator is left associative,
	 *            else <code>false</code>.
	 * @param numberOperands
	 *            The number of operands that are expected for this operator.
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc, int numberOperands) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.numberOperands = numberOperands;
	}

	
	
	/**
	 * @return The String that is used to denote the operator in the expression.
	 */
	public String getOper() {
		return oper;
	}

	
	
	/**
	 * @return the precedence value of this operator.
	 */
	public int getPrecedence() {
		return precedence;
	}

	
	
	/**
	 * @return <code>true</code> if this operator is left associative.
	 */
	public boolean isLeftAssoc() {
		return leftAssoc;
	}
	
	
	
	/**
	 * @return the number of operands for this operator.
	 */
	public int getNumberOperands() {
		return numberOperands;
	}

	
	
	/**
	 * @return <code>true</code> if this operator evaluates to a boolean
	 *         expression.
	 */
	public boolean isBooleanOperator() {
		return booleanOperator;
	}
}
