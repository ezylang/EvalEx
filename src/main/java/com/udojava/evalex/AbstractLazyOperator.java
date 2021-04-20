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
	 * Operator is boolean.
	 */
	protected boolean booleanOperator = false;
	
	/**
	 * Operator is unary, i.e. 1 or 2 operands expected for this operator.
	 */
	protected boolean unaryOperator;
	
	
	
	/**
	 * Creates a new unary operator. This constructor allows the use of postfix unary operators.
	 * 
	 * @param oper
	 *            The operator name (pattern).
	 * @param precedence
	 *            The operators precedence.
	 * @param leftAssoc
	 *            <code>true</code> if the operator is left associative,
 	 *            else <code>false</code>.
	 * @param booleanOperator
	 *            Whether this operator is boolean.
	 * @param unaryOperator
	 *            <code>true</code> if the expected number of operands is 1,
 	 *            <code>false</code> if the expected number of operands is 2.
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc, boolean booleanOperator, boolean unaryOperator) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.booleanOperator = booleanOperator;
		this.unaryOperator = unaryOperator;
	}

	
	
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
	 * @param booleanOperator
	 *            Whether this operator is boolean.
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc, boolean booleanOperator) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.booleanOperator = booleanOperator;
		this.unaryOperator = false;
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
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.unaryOperator = false;
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
	 * @return <code>true</code> if this operator evaluates to a boolean
	 *         expression.
	 */
	public boolean isBooleanOperator() {
		return booleanOperator;
	}
	
	
	
	/**
	 * @return <code>true</code> if the number of operands for this operator is 1,
	 * 			or <code>false</code> if the number of operands is 2.
	 */
	public boolean isUnaryOperator() {
		return unaryOperator;
	}
	
}
