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
import java.util.List;

/**
 * Abstract definition of a supported expression function. A function is defined
 * by a name, the number of parameters and the actual processing implementation.
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski), Srinivasa Rao
 *         Boyina
 */
public abstract class Function {
	/**
	 * Name of this function.
	 */
	private String name;
	/**
	 * Number of parameters expected for this function.
	 */
	private int numParams;

	/**
	 * Creates a new function with given name and parameter count.
	 * 
	 * @param name
	 *                The name of the function.
	 * @param numParams
	 *                The number of parameters for this function.
	 */
	public Function(String name, int numParams) {
		this.name = name.toUpperCase();
		this.numParams = numParams;
	}

	public String getName() {
		return name;
	}

	public int getNumParams() {
		return numParams;
	}

	/**
	 * Implementation for this function.
	 * 
	 * @param parameters
	 *                Parameters will be passed by the expression evaluator
	 *                as a {@link List} of {@link BigDecimal} values.
	 * @return The function must return a new {@link BigDecimal} value as a
	 *         computing result.
	 */
	public abstract BigDecimal eval(List<BigDecimal> parameters,
			MathContext mc);
}
