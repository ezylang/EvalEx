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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/**
 * <h1>EvalEx - Java Expression Evaluator</h1>
 * 
 * <h2>Introduction</h2> EvalEx is a handy expression evaluator for Java, that
 * allows to evaluate simple mathematical and boolean expressions. <br>
 * Key Features:
 * <ul>
 * <li>Uses BigDecimal for calculation and result</li>
 * <li>Single class implementation, very compact</li>
 * <li>No dependencies to external libraries</li>
 * <li>Precision and rounding mode can be set</li>
 * <li>Supports variables</li>
 * <li>Standard boolean and mathematical operators</li>
 * <li>Standard basic mathematical and boolean functions</li>
 * <li>Custom functions and operators can be added at runtime</li>
 * </ul>
 * <br>
 * <h2>Examples</h2>
 * 
 * <pre>
 *  BigDecimal result = null;
 *  
 *  Expression expression = new Expression("1+1/3");
 *  result = expression.eval():
 *  expression.setPrecision(2);
 *  result = expression.eval():
 *  
 *  result = new Expression("(3.4 + -4.1)/2").eval();
 *  
 *  result = new Expression("SQRT(a^2 + b^2").with("a","2.4").and("b","9.253").eval();
 *  
 *  BigDecimal a = new BigDecimal("2.4");
 *  BigDecimal b = new BigDecimal("9.235");
 *  result = new Expression("SQRT(a^2 + b^2").with("a",a).and("b",b).eval();
 *  
 *  result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();
 *  
 *  result = new Expression("random() > 0.5").eval();
 * 
 *  result = new Expression("not(x<7 || sqrt(max(x,9)) <= 3))").with("x","22.9").eval();
 * </pre>
 * 
 * <h2>Supported Constants</h2>
 * <table>
 * <tr>
 * <th>Constant</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>PI</td>
 * <td>The value of <i>PI</i>, exact to 100 digits</td>
 * </tr>
 * <tr>
 * <td>TRUE</td>
 * <td>The value one</td>
 * </tr>
 * <tr>
 * <td>FALSE</td>
 * <td>The value zero</td>
 * </tr>
 * </table>
 * 
 * 
 * The software is licensed under the MIT Open Source license (see LICENSE
 * file). <br>
 * <ul>
 * <li>The *power of* operator (^) implementation was copied from [Stack
 * Overflow
 * ](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power
 * -on-bigdecimal-in-java) Thanks to Gene Marin</li>
 * <li>The SQRT() function implementation was taken from the book [The Java
 * Programmers Guide To numerical
 * Computing](http://www.amazon.de/Java-Number-Cruncher
 * -Programmers-Numerical/dp/0130460419) (Ronald Mak, 2002)</li>
 * </ul>
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski)
 */
public class Expression {

	/**
	 * Definition of PI as a constant, can be used in expressions as
	 * variable.
	 */
	public static final BigDecimal PI = new BigDecimal(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

	/**
	 * The {@link MathContext} to use for calculations.
	 */
	private MathContext mc = MathContext.DECIMAL32;

	/**
	 * The original infix expression.
	 */
	private String expression = null;

	/**
	 * The cached RPN (Reverse Polish Notation) of the expression.
	 */
	private List<String> rpn = null;

	/**
	 * All defined variables with name and value.
	 */
	private Map<String, BigDecimal> variables = new HashMap<String, BigDecimal>();

	/**
	 * What character to use for decimal separators.
	 */
	private final char decimalSeparator = '.';

	/**
	 * What character to use for minus sign (negative values).
	 */
	private final char minusSign = '-';

	/**
	 * The cached set of variable names that are found in this expression.
	 */
	private Set<String> variableNames;

	/**
	 * context to use when evaluating the expression.
	 */
	private EvaluationContext evaluationContext;

	/**
	 * Expression tokenizer that allows to iterate over a {@link String}
	 * expression token by token. Blank characters will be skipped.
	 */
	private class Tokenizer implements Iterator<String> {

		/**
		 * Actual position in expression string.
		 */
		private int pos = 0;
		/**
		 * The original input expression.
		 */
		private String input;
		/**
		 * The previous token or <code>null</code> if none.
		 */
		private String previousToken;

		/**
		 * Creates a new tokenizer for an expression.
		 * 
		 * @param input
		 *                The expression string.
		 */
		public Tokenizer(String input) {
			this.input = input;
		}

		public boolean hasNext() {
			return (pos < input.length());
		}

		/**
		 * Peek at the next character, without advancing the iterator.
		 * 
		 * @return The next character or character 0, if at end of
		 *         string.
		 */
		private char peekNextChar() {
			if (pos < (input.length() - 1)) {
				return input.charAt(pos + 1);
			} else {
				return 0;
			}
		}

		public String next() {
			StringBuilder token = new StringBuilder();
			if (pos >= input.length()) {
				return previousToken = null;
			}
			char ch = input.charAt(pos);
			while (Character.isWhitespace(ch)
					&& pos < input.length()) {
				ch = input.charAt(++pos);
			}
			if (Character.isDigit(ch)) {
				while ((Character.isDigit(ch) || ch == decimalSeparator)
						&& (pos < input.length())) {
					token.append(input.charAt(pos++));
					ch = pos == input.length() ? 0 : input
							.charAt(pos);
				}
			} else if (ch == minusSign
					&& Character.isDigit(peekNextChar())
					&& ("(".equals(previousToken)
							|| ",".equals(previousToken)
							|| previousToken == null || evaluationContext
								.hasOperator(previousToken))) {
				token.append(minusSign);
				pos++;
				token.append(next());
			} else if (Character.isLetter(ch)) {
				while ((Character.isLetter(ch)
						|| Character.isDigit(ch) || (ch == '_'))
						&& (pos < input.length())) {
					token.append(input.charAt(pos++));
					ch = pos == input.length() ? 0 : input
							.charAt(pos);
				}
			} else if (ch == '(' || ch == ')' || ch == ',') {
				token.append(ch);
				pos++;
			} else {
				while (!Character.isLetter(ch)
						&& !Character.isDigit(ch)
						&& !Character.isWhitespace(ch)
						&& ch != '(' && ch != ')'
						&& ch != ','
						&& (pos < input.length())) {
					token.append(input.charAt(pos));
					pos++;
					ch = pos == input.length() ? 0 : input
							.charAt(pos);
					if (ch == minusSign) {
						break;
					}
				}
				if (!evaluationContext.hasOperator(token
						.toString())) {
					throw new ExpressionException(
							"Unknown operator '"
									+ token
									+ "' at position "
									+ (pos
											- token.length() + 1));
				}
			}
			return previousToken = token.toString();
		}

		public void remove() {
			throw new ExpressionException("remove() not supported");
		}

	}

	/**
	 * Creates a new expression instance from an expression string.
	 * Instances created with this constructor will use
	 * {@link StandardEvaluationContext} to evaluate the expression.
	 * 
	 * @param expression
	 *                The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code>
	 *                or <code>"sin(y)>0 & max(z, 3)>3"</code>
	 */
	public Expression(String expression) {
		this(expression, StandardEvaluationContext.INSTANCE);
	}

	/**
	 * Creates a new expression instance from an expression string.
	 * 
	 * @param expression
	 *                The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code>
	 *                or <code>"sin(y)>0 & max(z, 3)>3"</code>
	 * @param evaluationContext
	 *                {@link EvaluationContext} to use for this expression.
	 */
	public Expression(String expression, EvaluationContext evaluationContext) {
		this.expression = expression;
		variables.put("PI", PI);
		variables.put("TRUE", BigDecimal.ONE);
		variables.put("FALSE", BigDecimal.ZERO);
		this.evaluationContext = evaluationContext;
	}

	/**
	 * Is the string a number?
	 * 
	 * @param st
	 *                The string.
	 * @return <code>true</code>, if the input string is a number.
	 */
	private boolean isNumber(String st) {
		if (st.charAt(0) == minusSign && st.length() == 1)
			return false;
		for (char ch : st.toCharArray()) {
			if (!Character.isDigit(ch) && ch != minusSign
					&& ch != decimalSeparator)
				return false;
		}
		return true;
	}

	/**
	 * Implementation of the <i>Shunting Yard</i> algorithm to transform an
	 * infix expression to a RPN expression.
	 * 
	 * @param expression
	 *                The input expression in infx.
	 * @return A RPN representation of the expression, with each token as a
	 *         list member.
	 */
	private List<String> shuntingYard(String expression) {
		List<String> outputQueue = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();

		Tokenizer tokenizer = new Tokenizer(expression);

		String lastFunction = null;
		while (tokenizer.hasNext()) {
			String token = tokenizer.next();
			if (isNumber(token)) {
				outputQueue.add(token);
			} else if (variables.containsKey(token)) {
				outputQueue.add(token);
			} else if (evaluationContext.hasFunction(token)) {
				stack.push(token);
				lastFunction = token;
			} else if (Character.isLetter(token.charAt(0))) {
				stack.push(token);
			} else if (",".equals(token)) {
				while (!stack.isEmpty()
						&& !"(".equals(stack.peek())) {
					outputQueue.add(stack.pop());
				}
				if (stack.isEmpty()) {
					throw new ExpressionException(
							"Parse error for function '"
									+ lastFunction
									+ "'");
				}
			} else if (evaluationContext.hasOperator(token)) {
				Operator o1 = evaluationContext
						.getOperator(token);
				String token2 = stack.isEmpty() ? null : stack
						.peek();
				Operator o2 = evaluationContext
						.getOperator(token2);
				while (o2 != null
						&& ((o1.isLeftAssoc() && o1
								.getPrecedence() <= o2
								.getPrecedence()) || (o1
								.getPrecedence() < o2
								.getPrecedence()))) {
					outputQueue.add(stack.pop());
					token2 = stack.isEmpty() ? null : stack
							.peek();
					o2 = evaluationContext
							.getOperator(token2);
				}
				stack.push(token);
			} else if ("(".equals(token)) {
				stack.push(token);
			} else if (")".equals(token)) {
				while (!stack.isEmpty()
						&& !"(".equals(stack.peek())) {
					outputQueue.add(stack.pop());
				}
				if (stack.isEmpty()) {
					throw new RuntimeException(
							"Mismatched parentheses");
				}
				stack.pop();
				if (!stack.isEmpty()
						&& evaluationContext
								.hasFunction(stack
										.peek())) {
					outputQueue.add(stack.pop());
				}
			}
		}
		while (!stack.isEmpty()) {
			String element = stack.pop();
			if ("(".equals(element) || ")".equals(element)) {
				throw new RuntimeException(
						"Mismatched parentheses");
			}
			outputQueue.add(element);
		}
		return outputQueue;
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @return The result of the expression.
	 */
	public BigDecimal eval() {
		return eval(this.variables);
	}

	/**
	 * Expression is evaluated with the given variables. This will be useful
	 * in a multi-threaded environment where a single expression object can
	 * be used to evaluate a expression with different set of variables.
	 * 
	 * @param variables
	 *                a key-value pairs of variables to use to evaluate a
	 *                expression.
	 * @return
	 */
	public BigDecimal eval(Map<String, BigDecimal> variables) {
		Stack<BigDecimal> stack = new Stack<BigDecimal>();

		for (String token : getRPN()) {
			if (this.evaluationContext.hasOperator(token)) {
				Operator operator = this.evaluationContext
						.getOperator(token);
				BigDecimal v1 = stack.pop();
				BigDecimal v2 = stack.pop();
				stack.push(operator.eval(v2, v1, this.mc));
			} else if (variables.containsKey(token)) {
				stack.push(variables.get(token).round(this.mc));
			} else if (this.evaluationContext.hasFunction(token)) {
				Function f = this.evaluationContext
						.getFunction(token);
				ArrayList<BigDecimal> p = new ArrayList<BigDecimal>(
						f.getNumParams());
				for (int i = 0; i < f.getNumParams(); i++) {
					p.add(0, stack.pop());
				}
				BigDecimal fResult = f.eval(p, this.mc);
				stack.push(fResult);
			} else {
				try {
					stack.push(new BigDecimal(token,
							this.mc));
				} catch (NumberFormatException nfe) {
					throw new RuntimeException(
							"Not able to resolve: "
									+ token);
				}
			}
		}
		return stack.pop().stripTrailingZeros();
	}

	/**
	 * Sets the precision for expression evaluation.
	 * 
	 * @param precision
	 *                The new precision.
	 * 
	 * @return The expression, allows to chain methods.
	 */
	public Expression setPrecision(int precision) {
		this.mc = new MathContext(precision);
		return this;
	}

	/**
	 * Sets the rounding mode for expression evaluation.
	 * 
	 * @param roundingMode
	 *                The new rounding mode.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setRoundingMode(RoundingMode roundingMode) {
		this.mc = new MathContext(mc.getPrecision(), roundingMode);
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable name.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setVariable(String variable, BigDecimal value) {
		variables.put(variable, value);
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable to set.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setVariable(String variable, String value) {
		variables.put(variable, new BigDecimal(value));
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable to set.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression with(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable to set.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression and(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable to set.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression and(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *                The variable to set.
	 * @param value
	 *                The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression with(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Get an iterator for this expression, allows iterating over an
	 * expression token by token.
	 * 
	 * @return A new iterator instance for this expression.
	 */
	public Iterator<String> getExpressionTokenizer() {
		return new Tokenizer(this.expression);
	}

	/**
	 * Returns the variable names found in the expression.
	 * 
	 * @return A Set of variable names found in the expression.
	 */
	public Set<String> getVariableNames() {
		if (this.variableNames == null) {
			this.variableNames = new TreeSet<String>();
			for (String token : this.getRPN()) {
				if (!(this.evaluationContext.hasOperator(token) || this.evaluationContext
						.hasFunction(token))) {
					try {
						new BigDecimal(token);
					} catch (NumberFormatException nfe) {
						variableNames.add(token);
					}
				}
			}
		}
		return this.variableNames;
	}

	/**
	 * Cached access to the RPN notation of this expression, ensures only
	 * one calculation of the RPN per expression instance. If no cached
	 * instance exists, a new one will be created and put to the cache.
	 * 
	 * @return The cached RPN instance.
	 */
	private List<String> getRPN() {
		if (rpn == null) {
			rpn = shuntingYard(this.expression);
		}
		return rpn;
	}

	/**
	 * Get a string representation of the RPN (Reverse Polish Notation) for
	 * this expression.
	 * 
	 * @return A string with the RPN representation for this expression.
	 */
	public String toRPN() {
		String result = new String();
		for (String st : getRPN()) {
			result = result.isEmpty() ? result : result + " ";
			result += st;
		}
		return result;
	}

}
