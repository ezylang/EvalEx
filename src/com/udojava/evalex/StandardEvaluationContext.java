/**
 * 
 */
package com.udojava.evalex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * <p>
 * {@link StandardEvaluationContext} contains all the basic operators and
 * functions.
 * </p>
 * <h2>Following are the supported Operators</h2>
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
 * <p>
 * Boolean operators result always in a BigDecimal value of 1 or 0 (zero). Any
 * non-zero value is treated as a _true_ value. Boolean _not_ is implemented by
 * a function.
 * </p>
 * <h2>Following are the supported Functions</h2>
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
 * <td>MIN(<i>e1</i>,<i>e2</i>)</td>
 * <td>Returns the smaller of both expressions</td>
 * </tr>
 * <tr>
 * <td>MAX(<i>e1</i>,<i>e2</i>)</td>
 * <td>Returns the bigger of both expressions</td>
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
 * Functions names are case insensitive. <br>
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski),Srinivasa Rao Boyina
 * 
 */
public final class StandardEvaluationContext extends EvaluationContext {

	public static final StandardEvaluationContext INSTANCE = new StandardEvaluationContext();

	private StandardEvaluationContext() {
		final StandardEvaluationContext self = this;
		super.addOperator(new Operator("+", 20, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.add(v2, mc);
			}
		});
		super.addOperator(new Operator("-", 20, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.subtract(v2, mc);
			}
		});
		super.addOperator(new Operator("*", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.multiply(v2, mc);
			}
		});
		super.addOperator(new Operator("/", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.divide(v2, mc);
			}
		});
		super.addOperator(new Operator("%", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.remainder(v2, mc);
			}
		});
		super.addOperator(new Operator("^", 40, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				/*- 
				 * Thanks to Gene Marin:
				 * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
				 */
				int signOf2 = v2.signum();
				double dn1 = v1.doubleValue();
				v2 = v2.multiply(new BigDecimal(signOf2)); // n2
									   // is
									   // now
									   // positive
				BigDecimal remainderOf2 = v2
						.remainder(BigDecimal.ONE);
				BigDecimal n2IntPart = v2
						.subtract(remainderOf2);
				BigDecimal intPow = v1.pow(
						n2IntPart.intValueExact(), mc);
				BigDecimal doublePow = new BigDecimal(
						Math.pow(dn1, remainderOf2
								.doubleValue()));

				BigDecimal result = intPow.multiply(doublePow,
						mc);
				if (signOf2 == -1) {
					result = BigDecimal.ONE.divide(result,
							mc.getPrecision(),
							RoundingMode.HALF_UP);
				}
				return result;
			}
		});
		super.addOperator(new Operator("&&", 4, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				boolean b1 = !v1.equals(BigDecimal.ZERO);
				boolean b2 = !v2.equals(BigDecimal.ZERO);
				return b1 && b2 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator("||", 2, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				boolean b1 = !v1.equals(BigDecimal.ZERO);
				boolean b2 = !v2.equals(BigDecimal.ZERO);
				return b1 || b2 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator(">", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) == 1 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator(">=", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) >= 0 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator("<", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) == -1 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator("<=", 10, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) <= 0 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});

		super.addOperator(new Operator("=", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) == 0 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});
		super.addOperator(new Operator("==", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return self.getOperator("=").eval(v1, v2, mc);
			}
		});

		super.addOperator(new Operator("!=", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return v1.compareTo(v2) != 0 ? BigDecimal.ONE
						: BigDecimal.ZERO;
			}
		});
		super.addOperator(new Operator("<>", 7, false) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2,
					MathContext mc) {
				return self.getOperator("!=").eval(v1, v2, mc);
			}
		});

		super.addFunction(new Function("NOT", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				boolean zero = parameters.get(0).compareTo(
						BigDecimal.ZERO) == 0;
				return zero ? BigDecimal.ONE : BigDecimal.ZERO;
			}
		});

		super.addFunction(new Function("IF", 3) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				boolean isTrue = !parameters.get(0).equals(
						BigDecimal.ZERO);
				return isTrue ? parameters.get(1) : parameters
						.get(2);
			}
		});

		super.addFunction(new Function("RANDOM", 0) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.random();
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("SIN", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.sin(Math.toRadians(parameters
						.get(0).doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("COS", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.cos(Math.toRadians(parameters
						.get(0).doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("TAN", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.tan(Math.toRadians(parameters
						.get(0).doubleValue()));
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("SINH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.sinh(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("COSH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.cosh(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("TANH", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.tanh(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("RAD", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.toRadians(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("DEG", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.toDegrees(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("MAX", 2) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				BigDecimal v1 = parameters.get(0);
				BigDecimal v2 = parameters.get(1);
				return v1.compareTo(v2) > 0 ? v1 : v2;
			}
		});
		super.addFunction(new Function("MIN", 2) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				BigDecimal v1 = parameters.get(0);
				BigDecimal v2 = parameters.get(1);
				return v1.compareTo(v2) < 0 ? v1 : v2;
			}
		});
		super.addFunction(new Function("ABS", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				return parameters.get(0).abs(mc);
			}
		});
		super.addFunction(new Function("LOG", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				double d = Math.log(parameters.get(0)
						.doubleValue());
				return new BigDecimal(d, mc);
			}
		});
		super.addFunction(new Function("ROUND", 2) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				BigDecimal toRound = parameters.get(0);
				int precision = parameters.get(1).intValue();
				return toRound.setScale(precision,
						mc.getRoundingMode());
			}
		});
		super.addFunction(new Function("FLOOR", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				BigDecimal toRound = parameters.get(0);
				return toRound.setScale(0, RoundingMode.FLOOR);
			}
		});
		super.addFunction(new Function("CEILING", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				BigDecimal toRound = parameters.get(0);
				return toRound.setScale(0, RoundingMode.CEILING);
			}
		});
		super.addFunction(new Function("SQRT", 1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters,
					MathContext mc) {
				/*
				 * From The Java Programmers Guide To numerical
				 * Computing (Ronald Mak, 2003)
				 */
				BigDecimal x = parameters.get(0);
				if (x.compareTo(BigDecimal.ZERO) == 0) {
					return new BigDecimal(0);
				}
				if (x.signum() < 0) {
					throw new ExpressionException(
							"Argument to SQRT() function must not be negative");
				}
				BigInteger n = x.movePointRight(
						mc.getPrecision() << 1)
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
	}

	@Override
	public Operator addOperator(Operator operator) {
		throw new UnsupportedOperationException(
				"Standard Evaluation Context cannot be modified");
	}

	@Override
	public Function addFunction(Function function) {
		throw new UnsupportedOperationException(
				"Standard Evaluation Context cannot be modified");
	}

}
