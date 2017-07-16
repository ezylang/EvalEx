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
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

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
 * <br>
 * <h2>Supported Operators</h2>
 * <table>
 * <tr>
 * <th>Mathematical Operators</th>
 * </tr>
 * <tr>
 * <th>Operator</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>+</td>
 * <td>Additive operator</td>
 * </tr>
 * <tr>
 * <td>-</td>
 * <td>Subtraction operator</td>
 * </tr>
 * <tr>
 * <td>*</td>
 * <td>Multiplication operator</td>
 * </tr>
 * <tr>
 * <td>/</td>
 * <td>Division operator</td>
 * </tr>
 * <tr>
 * <td>%</td>
 * <td>Remainder operator (Modulo)</td>
 * </tr>
 * <tr>
 * <td>^</td>
 * <td>Power operator</td>
 * </tr>
 * </table>
 * <br>
 * <table>
 * <tr>
 * <th>Boolean Operators<sup>*</sup></th>
 * </tr>
 * <tr>
 * <th>Operator</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>=</td>
 * <td>Equals</td>
 * </tr>
 * <tr>
 * <td>==</td>
 * <td>Equals</td>
 * </tr>
 * <tr>
 * <td>!=</td>
 * <td>Not equals</td>
 * </tr>
 * <tr>
 * <td>&lt;&gt;</td>
 * <td>Not equals</td>
 * </tr>
 * <tr>
 * <td>&lt;</td>
 * <td>Less than</td>
 * </tr>
 * <tr>
 * <td>&lt;=</td>
 * <td>Less than or equal to</td>
 * </tr>
 * <tr>
 * <td>&gt;</td>
 * <td>Greater than</td>
 * </tr>
 * <tr>
 * <td>&gt;=</td>
 * <td>Greater than or equal to</td>
 * </tr>
 * <tr>
 * <td>&amp;&amp;</td>
 * <td>Boolean and</td>
 * </tr>
 * <tr>
 * <td>||</td>
 * <td>Boolean or</td>
 * </tr>
 * </table>
 * *Boolean operators result always in a BigDecimal value of 1 or 0 (zero). Any
 * non-zero value is treated as a _true_ value. Boolean _not_ is implemented by
 * a function. <br>
 * <h2>Supported Functions</h2>
 * <table>
 * <tr>
 * <th>Function<sup>*</sup></th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>NOT(<i>expression</i>)</td>
 * <td>Boolean negation, 1 (means true) if the expression is not zero</td>
 * </tr>
 * <tr>
 * <td>IF(<i>condition</i>,<i>value_if_true</i>,<i>value_if_false</i>)</td>
 * <td>Returns one value if the condition evaluates to true or the other if it
 * evaluates to false</td>
 * </tr>
 * <tr>
 * <td>RANDOM()</td>
 * <td>Produces a random number between 0 and 1</td>
 * </tr>
 * <tr>
 * <td>MIN(<i>e1</i>,<i>e2</i>, <i>...</i>)</td>
 * <td>Returns the smallest of the given expressions</td>
 * </tr>
 * <tr>
 * <td>MAX(<i>e1</i>,<i>e2</i>, <i>...</i>)</td>
 * <td>Returns the biggest of the given expressions</td>
 * </tr>
 * <tr>
 * <td>ABS(<i>expression</i>)</td>
 * <td>Returns the absolute (non-negative) value of the expression</td>
 * </tr>
 * <tr>
 * <td>ROUND(<i>expression</i>,precision)</td>
 * <td>Rounds a value to a certain number of digits, uses the current rounding
 * mode</td>
 * </tr>
 * <tr>
 * <td>FLOOR(<i>expression</i>)</td>
 * <td>Rounds the value down to the nearest integer</td>
 * </tr>
 * <tr>
 * <td>CEILING(<i>expression</i>)</td>
 * <td>Rounds the value up to the nearest integer</td>
 * </tr>
 * <tr>
 * <td>LOG(<i>expression</i>)</td>
 * <td>Returns the natural logarithm (base e) of an expression</td>
 * </tr>
 * <tr>
 * <td>LOG10(<i>expression</i>)</td>
 * <td>Returns the common logarithm (base 10) of an expression</td>
 * </tr>
 * <tr>
 * <td>SQRT(<i>expression</i>)</td>
 * <td>Returns the square root of an expression</td>
 * </tr>
 * <tr>
 * <td>SIN(<i>expression</i>)</td>
 * <td>Returns the trigonometric sine of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>COS(<i>expression</i>)</td>
 * <td>Returns the trigonometric cosine of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>TAN(<i>expression</i>)</td>
 * <td>Returns the trigonometric tangens of an angle (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ASIN(<i>expression</i>)</td>
 * <td>Returns the angle of asin (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ACOS(<i>expression</i>)</td>
 * <td>Returns the angle of acos (in degrees)</td>
 * </tr>
 * <tr>
 * <td>ATAN(<i>expression</i>)</td>
 * <td>Returns the angle of atan (in degrees)</td>
 * </tr>
 * <tr>
 * <td>SINH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic sine of a value</td>
 * </tr>
 * <tr>
 * <td>COSH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic cosine of a value</td>
 * </tr>
 * <tr>
 * <td>TANH(<i>expression</i>)</td>
 * <td>Returns the hyperbolic tangens of a value</td>
 * </tr>
 * <tr>
 * <td>RAD(<i>expression</i>)</td>
 * <td>Converts an angle measured in degrees to an approximately equivalent
 * angle measured in radians</td>
 * </tr>
 * <tr>
 * <td>DEG(<i>expression</i>)</td>
 * <td>Converts an angle measured in radians to an approximately equivalent
 * angle measured in degrees</td>
 * </tr>
 * </table>
 * *Functions names are case insensitive. <br>
 * <h2>Supported Constants</h2>
 * <table>
 * <tr>
 * <th>Constant</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>e</td>
 * <td>The value of <i>e</i>, exact to 70 digits</td>
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
 * <h2>Add Custom Operators</h2>
 * 
 * Custom operators can be added easily, simply create an instance of
 * `Expression.Operator` and add it to the expression. Parameters are the
 * operator string, its precedence and if it is left associative. The operators
 * `eval()` method will be called with the BigDecimal values of the operands.
 * All existing operators can also be overridden. <br>
 * For example, add an operator `x >> n`, that moves the decimal point of _x_
 * _n_ digits to the right:
 * 
 * <pre>
 * Expression e = new Expression("2.1234 >> 2");
 * 
 * e.addOperator(e.new Operator(">>", 30, true) {
 *     {@literal @}Override
 *     public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
 *         return v1.movePointRight(v2.toBigInteger().intValue());
 *     }
 * });
 * 
 * e.eval(); // returns 212.34
 * </pre>
 * 
 * <br>
 * <h2>Add Custom Functions</h2>
 * 
 * Adding custom functions is as easy as adding custom operators. Create an
 * instance of `Expression.Function`and add it to the expression. Parameters are
 * the function name and the count of required parameters. The functions
 * `eval()` method will be called with a list of the BigDecimal parameters. All
 * existing functions can also be overridden. <br>
 * A <code>-1</code> as the number of parameters denotes a variable number of arguments.<br>
 * For example, add a function `average(a,b,c)`, that will calculate the average
 * value of a, b and c: <br>
 * 
 * <pre>
 * Expression e = new Expression("2 * average(12,4,8)");
 * 
 * e.addFunction(e.new Function("average", 3) {
 *     {@literal @}Override
 *     public BigDecimal eval(List<BigDecimal> parameters) {
 *         BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
 *         return sum.divide(new BigDecimal(3));
 *     }
 * });
 * 
 * e.eval(); // returns 16
 * </pre>
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
	 * Definition of PI as a constant, can be used in expressions as variable.
	 */
	public static final BigDecimal PI = new BigDecimal(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
			
         /**
         * Definition of e: "Euler's number" as a constant, can be used in expressions as variable.
         */
         public static final BigDecimal e = new BigDecimal(
                        "2.71828182845904523536028747135266249775724709369995957496696762772407663");


	/**
	 * The {@link MathContext} to use for calculations.
	 */
	private MathContext mc = null;

	/**
	 * The characters (other than letters and digits) allowed as the first character in a variable.
	 */
	private String firstVarChars = "_";

	/**
	 * The characters (other than letters and digits) allowed as the second or subsequent characters in a variable.
	 */
	private String varChars = "_";

	/**
	 * The original infix expression.
	 */
	private final String originalExpression;

	/**
	 * The current infix expression, with optional variable substitutions.
	 */
	private String expression = null;

	/**
	 * The cached RPN (Reverse Polish Notation) of the expression.
	 */
	private List<Token> rpn = null;

	/**
	 * All defined operators with name and implementation.
	 */
	private Map<String, Operator> operators = new TreeMap<String, Operator>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * All defined functions with name and implementation.
	 */
	private Map<String,LazyFunction> functions = new TreeMap<String, LazyFunction>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * All defined variables with name and value.
	 */
	private Map<String, BigDecimal> variables = new TreeMap<String, BigDecimal>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * What character to use for decimal separators.
	 */
	private static final char decimalSeparator = '.';

	/**
	 * What character to use for minus sign (negative values).
	 */
	private static final char minusSign = '-';

	/**
	 * The BigDecimal representation of the left parenthesis, 
	 * used for parsing varying numbers of function parameters.
	 */
	private static final LazyNumber PARAMS_START = new LazyNumber() {
		public BigDecimal eval() {
			return null;
		}

		public String getString() {
			return null;
		}
	};

	/**
	 * The expression evaluators exception class.
	 */
	public static class ExpressionException extends RuntimeException {
		private static final long serialVersionUID = 1118142866870779047L;

		public ExpressionException(String message) {
			super(message);
		}
	}


	/**
	 * LazyNumber interface created for lazily evaluated functions
	 */
	public interface LazyNumber {
		BigDecimal eval();
		String getString();
	}

	public abstract class LazyFunction {
		/**
		 * Name of this function.
		 */
		private String name;
		/**
		 * Number of parameters expected for this function.
		 * <code>-1</code> denotes a variable number of parameters.
		 */
		private int numParams;

		/**
		 * Creates a new function with given name and parameter count.
		 *
		 * @param name
		 *            The name of the function.
		 * @param numParams
		 *            The number of parameters for this function.
		 *            <code>-1</code> denotes a variable number of parameters.
		 */
		public LazyFunction(String name, int numParams) {
			this.name = name.toUpperCase(Locale.ROOT);
			this.numParams = numParams;
		}

		public String getName() {
			return name;
		}

		public int getNumParams() {
			return numParams;
		}

		public boolean numParamsVaries() {
			return numParams < 0;
		}
		public abstract LazyNumber lazyEval(List<LazyNumber> lazyParams);
	}



	/**
	 * Abstract definition of a supported expression function. A function is
	 * defined by a name, the number of parameters and the actual processing
	 * implementation.
	 */
	public abstract class Function extends LazyFunction {

		public Function(String name, int numParams) {
			super(name, numParams);
		}

		public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
			final List<BigDecimal> params = new ArrayList<BigDecimal>();
			for (LazyNumber lazyParam : lazyParams) {
				params.add(lazyParam.eval());
			}
			return new LazyNumber() {
				public BigDecimal eval() {
					return Function.this.eval(params);
				}

				public String getString() {
					return String.valueOf(Function.this.eval(params));
				}
			};
		}

		/**
		 * Implementation for this function.
		 *
		 * @param parameters
		 *            Parameters will be passed by the expression evaluator as a
		 *            {@link List} of {@link BigDecimal} values.
		 * @return The function must return a new {@link BigDecimal} value as a
		 *         computing result.
		 */
		public abstract BigDecimal eval(List<BigDecimal> parameters);
	}
	/**
	 * Abstract definition of a supported operator. An operator is defined by
	 * its name (pattern), precedence and if it is left- or right associative.
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
		 *            The operator name (pattern).
		 * @param precedence
		 *            The operators precedence.
		 * @param leftAssoc
		 *            <code>true</code> if the operator is left associative,
		 *            else <code>false</code>.
		 */
		public Operator(String oper, int precedence, boolean leftAssoc) {
			this.oper = oper;
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
		 *            Operand 1.
		 * @param v2
		 *            Operand 2.
		 * @return The result of the operation.
		 */
		public abstract BigDecimal eval(BigDecimal v1, BigDecimal v2);
	}

	enum TokenType {
		VARIABLE, FUNCTION, LITERAL, OPERATOR, OPEN_PAREN, COMMA, CLOSE_PAREN, HEX_LITERAL, STRINGPARAM
	}

	class Token {
		public String surface = "";
		public TokenType type;
		public int pos;

		public void append(char c) {
			surface += c;
		}

		public void append(String s) {
			surface += s;
		}

		public char charAt(int pos) {
			return surface.charAt(pos);
		}

		public int length() {
			return surface.length();
		}

		@Override
		public String toString() {
			return surface;
		}
	}

	/**
	 * Expression tokenizer that allows to iterate over a {@link String}
	 * expression token by token. Blank characters will be skipped.
	 */
	private class Tokenizer implements Iterator<Token> {

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
		private Token previousToken;

		/**
		 * Creates a new tokenizer for an expression.
		 * 
		 * @param input
		 *            The expression string.
		 */
		public Tokenizer(String input) {
			this.input = input.trim();
		}

		@Override
		public boolean hasNext() {
			return (pos < input.length());
		}

		/**
		 * Peek at the next character, without advancing the iterator.
		 * 
		 * @return The next character or character 0, if at end of string.
		 */
		private char peekNextChar() {
			if (pos < (input.length() - 1)) {
				return input.charAt(pos + 1);
			} else {
				return 0;
			}
		}

		private boolean isHexDigit(char ch) {
			return ch == 'x' || ch == 'X' || (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
		}

		@Override
		public Token next() {
			Token token = new Token();

			if (pos >= input.length()) {
				return previousToken = null;
			}
			char ch = input.charAt(pos);
			while (Character.isWhitespace(ch) && pos < input.length()) {
				ch = input.charAt(++pos);
			}
			token.pos = pos;

			boolean isHex = false;

			if (Character.isDigit(ch)) {
				if(ch == '0' && (peekNextChar() == 'x' || peekNextChar() == 'X')) isHex = true;
				while ((isHex && isHexDigit(ch)) || (Character.isDigit(ch) || ch == decimalSeparator
                                                || ch == 'e' || ch == 'E'
                                                || (ch == minusSign && token.length() > 0 
                                                    && ('e'==token.charAt(token.length()-1) || 'E'==token.charAt(token.length()-1)))
                                                || (ch == '+' && token.length() > 0 
                                                    && ('e'==token.charAt(token.length()-1) || 'E'==token.charAt(token.length()-1)))
                                                ) && (pos < input.length())) {
					token.append(input.charAt(pos++));
					ch = pos == input.length() ? 0 : input.charAt(pos);
				}
				token.type = isHex ? TokenType.HEX_LITERAL : TokenType.LITERAL;
			} else if (ch == minusSign
					&& Character.isDigit(peekNextChar())
					&& (previousToken == null || previousToken.type == TokenType.OPEN_PAREN || previousToken.type == TokenType.COMMA
							 || previousToken.type == TokenType.OPERATOR)) {
				token.append(minusSign);
				pos++;
				token.append(next().toString());
				token.type = TokenType.LITERAL;
			} else if (ch == '"') {
				pos++;
				if (previousToken.type != TokenType.STRINGPARAM) {
					ch = input.charAt(pos);
					while (ch != '"') {
						token.append(input.charAt(pos++));
						ch = pos == input.length() ? 0 : input.charAt(pos);
					}
					token.type = TokenType.STRINGPARAM;
				} else {
					return next();
				}
			} else if (Character.isLetter(ch) || firstVarChars.indexOf(ch) >= 0) {
				while ((Character.isLetter(ch) || Character.isDigit(ch)
						|| varChars.indexOf(ch) >= 0 || token.length() == 0 && firstVarChars.indexOf(ch) >= 0)
						&& (pos < input.length())) {
					token.append(input.charAt(pos++));
					ch = pos == input.length() ? 0 : input.charAt(pos);
				}
				token.type = ch == '(' ? TokenType.FUNCTION : TokenType.VARIABLE;
			} else if (ch == '(' || ch == ')' || ch == ',') {
				if(ch == '(') {
					token.type = TokenType.OPEN_PAREN;
				} else if (ch == ')') {
					token.type = TokenType.CLOSE_PAREN;
				} else {
					token.type = TokenType.COMMA;
				}
				token.append(ch);
				pos++;
			} else {
				while (!Character.isLetter(ch) && !Character.isDigit(ch)
						&& firstVarChars.indexOf(ch) < 0 && !Character.isWhitespace(ch)
						&& ch != '(' && ch != ')' && ch != ','
						&& (pos < input.length())) {
					token.append(input.charAt(pos));
					pos++;
					ch = pos == input.length() ? 0 : input.charAt(pos);
					if (ch == minusSign) {
						break;
					}
				}
				token.type = TokenType.OPERATOR;
			}
			return previousToken = token;
		}

		@Override
		public void remove() {
			throw new ExpressionException("remove() not supported");
		}

	}

	/**
	 * Creates a new expression instance from an expression string with a given
	 * default match context of {@link MathContext#DECIMAL32}.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or
	 *            <code>"sin(y)>0 & max(z, 3)>3"</code>
	 */
	public Expression(String expression) {
		this(expression, MathContext.DECIMAL32);
	}

	/**
	 * Creates a new expression instance from an expression string with a given
	 * default match context.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or
	 *            <code>"sin(y)>0 & max(z, 3)>3"</code>
	 * @param defaultMathContext
	 *            The {@link MathContext} to use by default.
	 */
	public Expression(String expression, MathContext defaultMathContext) {
		this.mc = defaultMathContext;
		this.expression = expression;
		this.originalExpression = expression;
		addOperator(new Operator("+", 20, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.add(v2, mc);
			}
		});
		addOperator(new Operator("-", 20, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.subtract(v2, mc);
			}
		});
		addOperator(new Operator("*", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.multiply(v2, mc);
			}
		});
		addOperator(new Operator("/", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.divide(v2, mc);
			}
		});
		addOperator(new Operator("%", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.remainder(v2, mc);
			}
		});
		addOperator(new Operator("^", 40, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				/*- 
				 * Thanks to Gene Marin:
				 * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
				 */
				int signOf2 = v2.signum();
				double dn1 = v1.doubleValue();
				v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
				BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
				BigDecimal n2IntPart = v2.subtract(remainderOf2);
				BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mc);
				BigDecimal doublePow = new BigDecimal(Math.pow(dn1,
						remainderOf2.doubleValue()));

				BigDecimal result = intPow.multiply(doublePow, mc);
				if (signOf2 == -1) {
					result = BigDecimal.ONE.divide(result, mc.getPrecision(),
							RoundingMode.HALF_UP);
				}
				return result;
			}
		});
		addOperator(new Operator("&&", 4, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				boolean b1 = !v1.equals(BigDecimal.ZERO);
				boolean b2 = !v2.equals(BigDecimal.ZERO);
				return b1 && b2 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addOperator(new Operator("||", 2, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				boolean b1 = !v1.equals(BigDecimal.ZERO);
				boolean b2 = !v2.equals(BigDecimal.ZERO);
				return b1 || b2 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addOperator(new Operator(">", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) == 1 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addOperator(new Operator(">=", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addOperator(new Operator("<", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) == -1 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		addOperator(new Operator("<=", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return v1.compareTo(v2) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addOperator(new Operator("=", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				if (v1 == v2) {
					return BigDecimal.ONE;
				}
				if (v1 == null  || v2 == null) {
					return BigDecimal.ZERO;
				}
				return v1.compareTo(v2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});
		addOperator(new Operator("==", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				return operators.get("=").eval(v1, v2);
			}
		});

		addOperator(new Operator("!=", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				if (v1 == v2) {
					return BigDecimal.ZERO;
				}
				if (v1 == null  || v2 == null) {
					return BigDecimal.ONE;
				}
				return v1.compareTo(v2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});
		addOperator(new Operator("<>", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				assertNotNull(v1, v2);
				return operators.get("!=").eval(v1, v2);
			}
		});

		addFunction(new Function("NOT", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				boolean zero = parameters.get(0).compareTo(BigDecimal.ZERO) == 0;
				return zero ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		addLazyFunction(new LazyFunction("IF", 3) {
			@Override
			public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
				BigDecimal result = lazyParams.get(0).eval();
				assertNotNull(result);
				boolean isTrue = !result.equals(BigDecimal.ZERO);
				return isTrue ? lazyParams.get(1) : lazyParams.get(2);
			}
		});

		addFunction(new Function("RANDOM", 0) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				double d = Math.random();
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("SIN", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.sin(Math.toRadians(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("COS", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.cos(Math.toRadians(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("TAN", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.tan(Math.toRadians(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("ASIN", 1) { // added by av
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.asin(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("ACOS", 1) { // added by av
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.acos(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("ATAN", 1) { // added by av
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(Math.atan(parameters.get(0)
						.doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("SINH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.sinh(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("COSH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.cosh(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("TANH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.tanh(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("RAD", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toRadians(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("DEG", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.toDegrees(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("MAX", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				if (parameters.size() == 0) {
					throw new ExpressionException("MAX requires at least one parameter");
				}
				BigDecimal max = null;
				for (BigDecimal parameter : parameters) {
					assertNotNull(parameter);
					if (max == null || parameter.compareTo(max) > 0) {
						max = parameter;
					}
				}
				return max;
			}
		});
		addFunction(new Function("MIN", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				if (parameters.size() == 0) {
					throw new ExpressionException("MIN requires at least one parameter");
				}
				BigDecimal min = null;
				for (BigDecimal parameter : parameters) {
					assertNotNull(parameter);
					if (min == null || parameter.compareTo(min) < 0) {
						min = parameter;
					}
				}
				return min;
			}
		});
		addFunction(new Function("ABS", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				return parameters.get(0).abs(mc);
			}
		});
		addFunction(new Function("LOG", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.log(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("LOG10", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				double d = Math.log10(parameters.get(0).doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		addFunction(new Function("ROUND", 2) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0), parameters.get(1));
				BigDecimal toRound = parameters.get(0);
				int precision = parameters.get(1).intValue();
				return toRound.setScale(precision, mc.getRoundingMode());
			}
		});
		addFunction(new Function("FLOOR", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				BigDecimal toRound = parameters.get(0);
				return toRound.setScale(0, RoundingMode.FLOOR);
			}
		});
		addFunction(new Function("CEILING", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				BigDecimal toRound = parameters.get(0);
				return toRound.setScale(0, RoundingMode.CEILING);
			}
		});
		addFunction(new Function("SQRT", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertNotNull(parameters.get(0));
				/*
				 * From The Java Programmers Guide To numerical Computing
				 * (Ronald Mak, 2003)
				 */
				BigDecimal x = parameters.get(0);
				if (x.compareTo(BigDecimal.ZERO) == 0) {
					return new BigDecimal(0);
				}
				if (x.signum() < 0) {
					throw new ExpressionException(
							"Argument to SQRT() function must not be negative");
				}
				BigInteger n = x.movePointRight(mc.getPrecision() << 1)
						.toBigInteger();

				int bits = (n.bitLength() + 1) >> 1;
				BigInteger ix = n.shiftRight(bits);
				BigInteger ixPrev;

				do {
					ixPrev = ix;
					ix = ix.add(n.divide(ix)).shiftRight(1);
					// Give other threads a chance to work;
					Thread.yield();
				} while (ix.compareTo(ixPrev) != 0);

				return new BigDecimal(ix, mc.getPrecision());
			}
		});

		variables.put("e", e);
		variables.put("PI", PI);
		variables.put("NULL", null);
		variables.put("TRUE", BigDecimal.ONE);
		variables.put("FALSE", BigDecimal.ZERO);

	}
	
	private void assertNotNull(BigDecimal v1) {
		if (v1 == null) {
			throw new ArithmeticException("Operand may not be null");
		}
	}
	
	private void assertNotNull(BigDecimal v1, BigDecimal v2) {
		if (v1 == null) {
			throw new ArithmeticException("First operand may not be null");
		}
		if (v2 == null) {
			throw new ArithmeticException("Second operand may not be null");
		}		
	}

	/**
	 * Is the string a number?
	 * 
	 * @param st
	 *            The string.
	 * @return <code>true</code>, if the input string is a number.
	 */
	private boolean isNumber(String st) {
		if (st.charAt(0) == minusSign && st.length() == 1) return false;
		if (st.charAt(0) == '+' && st.length() == 1) return false;
		if (st.charAt(0) == 'e' ||  st.charAt(0) == 'E') return false;
		for (char ch : st.toCharArray()) {
			if (!Character.isDigit(ch) && ch != minusSign
					&& ch != decimalSeparator
                                        && ch != 'e' && ch != 'E' && ch != '+')
				return false;
		}
		return true;
	}

	/**
	 * Implementation of the <i>Shunting Yard</i> algorithm to transform an
	 * infix expression to a RPN expression.
	 * 
	 * @param expression
	 *            The input expression in infx.
	 * @return A RPN representation of the expression, with each token as a list
	 *         member.
	 */
	private List<Token> shuntingYard(String expression) {
		List<Token> outputQueue = new ArrayList<Token>();
		Stack<Token> stack = new Stack<Token>();

		Tokenizer tokenizer = new Tokenizer(expression);

		Token lastFunction = null;
		Token previousToken = null;
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			switch(token.type) {
				case STRINGPARAM:
					stack.push(token);
					break;
				case LITERAL:
				case HEX_LITERAL:
					outputQueue.add(token);
					break;
				case VARIABLE:
					outputQueue.add(token);
					break;
				case FUNCTION:
					stack.push(token);
					lastFunction = token;
					break;
				case COMMA:
					if (previousToken != null && previousToken.type == TokenType.OPERATOR) {
						throw new ExpressionException("Missing parameter(s) for operator " + previousToken +
								" at character position " + previousToken.pos);
					}
					while (!stack.isEmpty() && stack.peek().type != TokenType.OPEN_PAREN) {
						outputQueue.add(stack.pop());
					}
					if (stack.isEmpty()) {
						throw new ExpressionException("Parse error for function '"
								+ lastFunction + "'");
					}
					break;
				case OPERATOR:
					if (previousToken != null && (previousToken.type == TokenType.COMMA || previousToken.type == TokenType.OPEN_PAREN)) {
						throw new ExpressionException("Missing parameter(s) for operator " + token +
								" at character position " + token.pos);
					}
					Operator o1 = operators.get(token.surface);
					if(o1 == null) {
						throw new ExpressionException("Unknown operator '" + token
								+ "' at position " + (token.pos + 1));
					}

					String token2 = stack.isEmpty() ? null : stack.peek().toString();
					while (token2!=null &&
							operators.containsKey(token2)
							&& ((o1.isLeftAssoc() && o1.getPrecedence() <= operators
							.get(token2).getPrecedence()) || (o1
							.getPrecedence() < operators.get(token2)
							.getPrecedence()))) {
						outputQueue.add(stack.pop());
						token2 = stack.isEmpty() ? null : stack.peek().toString();
					}
					stack.push(token);
					break;
				case OPEN_PAREN:
					if (previousToken != null) {
						if (previousToken.type == TokenType.LITERAL) {
							throw new ExpressionException(
									"Missing operator at character position " + (token.pos + 1));
						}
						// if the ( is preceded by a valid function, then it
						// denotes the start of a parameter list
						if (previousToken.type == TokenType.FUNCTION) {
							outputQueue.add(token);
						}
					}
					stack.push(token);
					break;
				case CLOSE_PAREN:
					if (previousToken != null && previousToken.type == TokenType.OPERATOR) {
						throw new ExpressionException("Missing parameter(s) for operator " + previousToken +
								" at character position " + previousToken.pos);
					}
					while (!stack.isEmpty() && stack.peek().type != TokenType.OPEN_PAREN) {
						outputQueue.add(stack.pop());
					}
					if (stack.isEmpty()) {
						throw new ExpressionException("Mismatched parentheses");
					}
					stack.pop();
					if (!stack.isEmpty() && stack.peek().type == TokenType.FUNCTION) {
						outputQueue.add(stack.pop());
					}
			}
			previousToken = token;
		}

		while (!stack.isEmpty()) {
			Token element = stack.pop();
			if (element.type == TokenType.OPEN_PAREN || element.type == TokenType.CLOSE_PAREN) {
				throw new ExpressionException("Mismatched parentheses");
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

		Stack<LazyNumber> stack = new Stack<LazyNumber>();

		for (final Token token : getRPN()) {
			switch(token.type) {
				case OPERATOR:
					final LazyNumber v1 = stack.pop();
					final LazyNumber v2 = stack.pop();
					LazyNumber number = new LazyNumber() {
						public BigDecimal eval() {
							return operators.get(token.surface).eval(v2.eval(), v1.eval());
						}
						
						public String getString() {
						    return String.valueOf(operators.get(token.surface).eval(v2.eval(), v1.eval()));
						}
					};
					stack.push(number);
					break;
				case VARIABLE:
					if (!variables.containsKey(token.surface)) {
						throw new ExpressionException("Unknown operator or function: " + token);
					}

					stack.push(new LazyNumber() {
						public BigDecimal eval() {
							BigDecimal value = variables.get(token.surface);
							return value == null ? null : value.round(mc);
						}

						public String getString() {
							return token.surface;
						}
					});
					break;
				case FUNCTION:
					LazyFunction f = functions.get(token.surface.toUpperCase(Locale.ROOT));
					ArrayList<LazyNumber> p = new ArrayList<LazyNumber>(
							!f.numParamsVaries() ? f.getNumParams() : 0);
					// pop parameters off the stack until we hit the start of
					// this function's parameter list
					while (!stack.isEmpty() && stack.peek() != PARAMS_START) {
						p.add(0, stack.pop());
					}
					
					if (stack.peek() == PARAMS_START) {
						stack.pop();
					}
					
					LazyNumber fResult = f.lazyEval(p);
					stack.push(fResult);
					break;
				case OPEN_PAREN:
					stack.push(PARAMS_START);
					break;
				case LITERAL:
					stack.push(new LazyNumber() {
						public BigDecimal eval() {
							if (token.surface.equalsIgnoreCase("NULL")) {
								return null;
							}

							return new BigDecimal(token.surface, mc);
						}

						public String getString() {
							return String.valueOf(new BigDecimal(token.surface, mc));
						}
					});
					break;
				case STRINGPARAM:
					stack.push(new LazyNumber() {
						public BigDecimal eval() {
							return null;
						}

						public String getString() {
							return token.surface;
						} 
					});
					break;
				case HEX_LITERAL:
					stack.push(new LazyNumber() {
						public BigDecimal eval() {
							return new BigDecimal(new BigInteger(token.surface.substring(2), 16), mc);
						}
						public String getString() {
							return new BigInteger(token.surface.substring(2), 16).toString();
						} 
					});
					break;
			}
		}
		BigDecimal result = stack.pop().eval();
		return result == null ? null : result.stripTrailingZeros();
	}

	/**
	 * Sets the precision for expression evaluation.
	 * 
	 * @param precision
	 *            The new precision.
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
	 *            The new rounding mode.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setRoundingMode(RoundingMode roundingMode) {
		this.mc = new MathContext(mc.getPrecision(), roundingMode);
		return this;
	}

	/**
	 * Sets the characters other than letters and digits that are valid as the
	 * first character of a variable.
	 *
	 * @param chars
	 *            The new set of variable characters.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setFirstVariableCharacters(String chars) {
		this.firstVarChars = chars;
		return this;
	}

	/**
	 * Sets the characters other than letters and digits that are valid as the
	 * second and subsequent characters of a variable.
	 *
	 * @param chars
	 *            The new set of variable characters.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setVariableCharacters(String chars) {
		this.varChars = chars;
		return this;
	}

	/**
	 * Adds an operator to the list of supported operators.
	 * 
	 * @param operator
	 *            The operator to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	public Operator addOperator(Operator operator) {
		return operators.put(operator.getOper(), operator);
	}

	/**
	 * Adds a function to the list of supported functions
	 * 
	 * @param function
	 *            The function to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	public Function addFunction(Function function) {
		return (Function) functions.put(function.getName(), function);
	}

	/**
	 * Adds a lazy function function to the list of supported functions
	 *
	 * @param function
	 *            The function to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	public LazyFunction addLazyFunction(LazyFunction function) {
		return  functions.put(function.getName(), function);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable name.
	 * @param value
	 *            The variable value.
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
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression setVariable(String variable, String value) {
		if (isNumber(value))
			variables.put(variable, new BigDecimal(value));
		else if (value.equalsIgnoreCase("null")) {
			variables.put(variable, null);
		}
		else {
			expression = expression.replaceAll("(?i)\\b" + variable + "\\b", "("
					+ value + ")");
			rpn = null;
		}
		return this;
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression with(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression and(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression and(String variable, BigDecimal value) {
		return setVariable(variable, value);
	}

	/**
	 * Sets a variable value.
	 * 
	 * @param variable
	 *            The variable to set.
	 * @param value
	 *            The variable value.
	 * @return The expression, allows to chain methods.
	 */
	public Expression with(String variable, String value) {
		return setVariable(variable, value);
	}

	/**
	 * Get an iterator for this expression, allows iterating over an expression
	 * token by token.
	 * 
	 * @return A new iterator instance for this expression.
	 */
	public Iterator<Token> getExpressionTokenizer() {
		final String expression = this.expression;

		return new Tokenizer(expression);
//		return new Iterator<String>() {
//
//			Tokenizer tokenizer = new Tokenizer(expression);
//
//			@Override
//			public boolean hasNext() {
//				return tokenizer.hasNext();
//			}
//
//			@Override
//			public String next() {
//				Token nextToken = tokenizer.next();
//				return nextToken == null ? null : nextToken.toString();
//			}
//
//			@Override
//			public void remove() {
//				tokenizer.remove();
//			}
//		};
	}

	/**
	 * Cached access to the RPN notation of this expression, ensures only one
	 * calculation of the RPN per expression instance. If no cached instance
	 * exists, a new one will be created and put to the cache.
	 * 
	 * @return The cached RPN instance.
	 */
	private List<Token> getRPN() {
		if (rpn == null) {
			rpn = shuntingYard(this.expression);
			validate(rpn);
		}
		return rpn;
	}

	/**
	 * Check that the expression has enough numbers and variables to fit the
	 * requirements of the operators and functions, also check 
	 * for only 1 result stored at the end of the evaluation.
	 */
	private void validate(List<Token> rpn) {
		/*-
		* Thanks to Norman Ramsey:
		* http://http://stackoverflow.com/questions/789847/postfix-notation-validation
		*/
		// each push on to this stack is a new function scope, with the value of each
		// layer on the stack being the count of the number of parameters in that scope
		Stack<Integer> stack = new Stack<Integer>();

		// push the 'global' scope
		stack.push(0);

		for (final Token token : rpn) {
			switch(token.type) {
				case OPERATOR:
					if (stack.peek() < 2) {
						throw new ExpressionException("Missing parameter(s) for operator " + token);
					}
					// pop the operator's 2 parameters and add the result
					stack.set(stack.size() - 1, stack.peek() - 2 + 1);
					break;
				case VARIABLE:
					stack.set(stack.size() - 1, stack.peek() + 1);
					break;
				case FUNCTION:
					LazyFunction f = functions.get(token.surface.toUpperCase(Locale.ROOT));
					if(f == null) {
						throw new ExpressionException("Unknown function '" + token
								+ "' at position " + (token.pos + 1));
					}

					int numParams = stack.pop();
					if (!f.numParamsVaries() && numParams != f.getNumParams()) {
						throw new ExpressionException("Function " + token + " expected " + f.getNumParams() + " parameters, got " + numParams);
					}
					if (stack.size() <= 0) {
						throw new ExpressionException("Too many function calls, maximum scope exceeded");
					}
					// push the result of the function
					stack.set(stack.size() - 1, stack.peek() + 1);
					break;
				case OPEN_PAREN:
					stack.push(0);
					break;
				default:
					stack.set(stack.size() - 1, stack.peek() + 1);
			}
		}

		if (stack.size() > 1) {
			throw new ExpressionException("Too many unhandled function parameter lists");
		} else if (stack.peek() > 1) {
			throw new ExpressionException("Too many numbers or variables");
		} else if (stack.peek() < 1) {
			throw new ExpressionException("Empty expression");
		}
	}

	/**
	 * Get a string representation of the RPN (Reverse Polish Notation) for this
	 * expression.
	 * 
	 * @return A string with the RPN representation for this expression.
	 */
	public String toRPN() {
		StringBuilder result = new StringBuilder();
		for (Token t : getRPN()) {
			if (result.length() != 0)
				result.append(" ");
			result.append(t.toString());
		}
		return result.toString();
	}

	/**
	 * Exposing declared variables in the expression.
	 * @return All declared variables.
     */
	public Set<String> getDeclaredVariables() {
		return Collections.unmodifiableSet(variables.keySet());
	}

	/**
	 * Exposing declared operators in the expression.
	 * @return All declared operators.
     */
	public Set<String> getDeclaredOperators() {
		return Collections.unmodifiableSet(operators.keySet());
	}

	/**
	 * Exposing declared functions.
	 * @return All declared functions.
     */
	public Set<String> getDeclaredFunctions() {
		return Collections.unmodifiableSet(functions.keySet());
	}

	/**
	 * @return The original expression string
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Returns a list of the variables in the expression.
	 * 
	 * @return A list of the variable names in this expression.
	 */
	public List<String> getUsedVariables() {
		List<String> result = new ArrayList<String>();
		Tokenizer tokenizer = new Tokenizer(expression);
		while (tokenizer.hasNext()) {
			Token nextToken = tokenizer.next();
			String token = nextToken.toString();
			if (nextToken.type != TokenType.VARIABLE
					|| token.equals("PI") || token.equals("e")
					|| token.equals("TRUE") || token.equals("FALSE")) {
				continue;
			}
			result.add(token);
		}
		return result;
	}
  

  /**
   * The original expression used to construct this expression, without
   * variables substituted.
   */
  public String getOriginalExpression() {
    return this.originalExpression;
  }


  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Expression that = (Expression) o;
    if (this.expression == null) {
      return that.expression == null;
    } else {
      return this.expression.equals(that.expression);
    }
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return this.expression == null ? 0 : this.expression.hashCode();
  }
  
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.expression;
  }

}
