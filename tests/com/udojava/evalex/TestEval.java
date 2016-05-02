package com.udojava.evalex;

import com.udojava.evalex.Expression.ExpressionException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


public class TestEval {
	
	@Test
	public void testInvalidExpressions1() {
		String err = "";
		try {
			Expression expression = new Expression("12 18 2");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions2() {
		String err = "";
		try {
			Expression expression = new Expression("(12)(18)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions3() {
		String err = "";
		try {
			Expression expression = new Expression("12 + * 18");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many operators or functions at: +", err);
	}

	@Test
	public void testInvalidExpressions4() {
		String err = "";
		try {
			Expression expression = new Expression("");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Empty expression", err);
	}
	
	@Test
	public void testWrongBrackets1() {
		String err = "";
		try {
			Expression expression = new Expression("2*3(5*3)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 4", err);
	}
	
	@Test
	public void testWrongBrackets2() {
		String err = "";
		try {
			Expression expression = new Expression("2*(3((5*3)))");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 5", err);
	}
	
	@Test
	public void testBrackets() {
		assertEquals("3", new Expression("(1+2)").eval().toPlainString());
		assertEquals("3", new Expression("((1+2))").eval().toPlainString());
		assertEquals("3", new Expression("(((1+2)))").eval().toPlainString());
		assertEquals("9", new Expression("(1+2)*(1+2)").eval().toPlainString());
		assertEquals("10", new Expression("(1+2)*(1+2)+1").eval().toPlainString());
		assertEquals("12", new Expression("(1+2)*((1+2)+1)").eval().toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow1() {
		assertEquals("", new Expression("7#9").eval().toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow2() {
		assertEquals("", new Expression("123.6*-9.8-7#9").eval().toPlainString());
	}
	
	@Test
	public void testSimple() {
		assertEquals("3", new Expression("1+2").eval().toPlainString());
		assertEquals("2", new Expression("4/2").eval().toPlainString());
		assertEquals("5", new Expression("3+4/2").eval().toPlainString());
		assertEquals("3.5", new Expression("(3+4)/2").eval().toPlainString());
		assertEquals("7.98", new Expression("4.2*1.9").eval().toPlainString());
		assertEquals("2", new Expression("8%3").eval().toPlainString());
		assertEquals("0", new Expression("8%2").eval().toPlainString());
	}
	
	@Test
	public void testPow() {
		assertEquals("16", new Expression("2^4").eval().toPlainString());
		assertEquals("256", new Expression("2^8").eval().toPlainString());
		assertEquals("9", new Expression("3^2").eval().toPlainString());
		assertEquals("6.25", new Expression("2.5^2").eval().toPlainString());
		assertEquals("28.34045", new Expression("2.6^3.5").eval().toPlainString());
	}
	
	@Test
	public void testSqrt() {
		assertEquals("4", new Expression("SQRT(16)").eval().toPlainString());
		assertEquals("1.4142135", new Expression("SQRT(2)").eval().toPlainString());
		assertEquals("1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605", new Expression("SQRT(2)").setPrecision(128).eval().toPlainString());
		assertEquals("2.2360679", new Expression("SQRT(5)").eval().toPlainString());
		assertEquals("99.3730345", new Expression("SQRT(9875)").eval().toPlainString());
		assertEquals("2.3558437", new Expression("SQRT(5.55)").eval().toPlainString());	
		assertEquals("0", new Expression("SQRT(0)").eval().toPlainString());
	}
	
	@Test
	public void testFunctions() {
		assertNotSame("1.5", new Expression("Random()").eval().toPlainString());
		assertEquals("0.400349", new Expression("SIN(23.6)").eval().toPlainString());
		assertEquals("8", new Expression("MAX(-7,8)").eval().toPlainString());
		assertEquals("5", new Expression("MAX(3,max(4,5))").eval().toPlainString());
		assertEquals("9.6", new Expression("MAX(3,max(MAX(9.6,-4.2),Min(5,9)))").eval().toPlainString());
		assertEquals("2.302585", new Expression("LOG(10)").eval().toPlainString());
	}

	@Test
	public void testExpectedParameterNumbers() {
		String err = "";
		try {
			Expression expression = new Expression("Random(1)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function Random expected 0 parameters, got 1", err);

		try {
			Expression expression = new Expression("SIN(1, 6)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function SIN expected 1 parameters, got 2", err);
	}


	@Test
	public void testAtLarge(){

			final Expression exp = new Expression("porc(DR1BC3,sum(DR1BC3,DR1BC2,DR1BC1))");

			// Custom defined sum
			exp.addFunction(exp.new Function("SUM", -1) {
				@Override
				public BigDecimal eval(List<BigDecimal> parameters) {
					BigDecimal sum = BigDecimal.ZERO;
					for (BigDecimal parameter : parameters) {
						sum = sum.add(parameter);
					}
					return sum;
				}
			});
			// Custom defined percentage.
			exp.addFunction(exp.new Function("PORC", 2) {
				@Override
				public BigDecimal eval(List<BigDecimal> parameters) {
					if (parameters.size() != 2) {
						throw new ExpressionException("La función \"porc\" requiere dos parámetros");
					}

					if (parameters.get(1).floatValue() == 0) {
						return BigDecimal.ZERO;
					}

					return parameters.get(0).
							multiply(new BigDecimal("100")).
							divide(parameters.get(1), 10, RoundingMode.HALF_UP);

				}
			});

		exp.with("DR1BC3","10");
		exp.with("DR1BC3","10");
		exp.with("DR1BC2","50");
		exp.with("DR1BC1","40");

		assertEquals(10,exp.eval().intValue());
	}


	@Test
	public void testVariableParameterNumbers() {
		String err = "";
		try {
			Expression expression = new Expression("min()");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("MIN requires at least one parameter", err);

		assertEquals("1", new Expression("min(1)").eval().toPlainString());
		assertEquals("1", new Expression("min(1, 2)").eval().toPlainString());
		assertEquals("1", new Expression("min(1, 2, 3)").eval().toPlainString());
		assertEquals("3", new Expression("max(3, 2, 1)").eval().toPlainString());
		assertEquals("9", new Expression("max(3, 2, 1, 4, 5, 6, 7, 8, 9, 0)").eval().toPlainString());
	}

	@Test
	public void testExtremeFunctionNesting() {
		assertNotSame("1.5", new Expression("Random()").eval().toPlainString());
		assertEquals("0.0002791281", new Expression("SIN(SIN(COS(23.6)))").eval().toPlainString());
		assertEquals("-4", new Expression("MIN(0, SIN(SIN(COS(23.6))), 0-MAX(3,4,MAX(0,SIN(1))), 10)").eval().toPlainString());
	}

	@Test
	public void testTrigonometry() {
		assertEquals("0.5", new Expression("SIN(30)").eval().toPlainString());
		assertEquals("0.8660254", new Expression("cos(30)").eval().toPlainString());
		assertEquals("0.5773503", new Expression("TAN(30)").eval().toPlainString());
		assertEquals("5343237000000", new Expression("SINH(30)").eval().toPlainString());
		assertEquals("5343237000000", new Expression("COSH(30)").eval().toPlainString());
		assertEquals("1", new Expression("TANH(30)").eval().toPlainString());
		assertEquals("0.5235988", new Expression("RAD(30)").eval().toPlainString());
		assertEquals("1718.873", new Expression("DEG(30)").eval().toPlainString());
		
	}
	
	@Test
	public void testMinMaxAbs() {
		assertEquals("3.78787", new Expression("MAX(3.78787,3.78786)").eval().toPlainString());
		assertEquals("3.78787", new Expression("max(3.78786,3.78787)").eval().toPlainString());
		assertEquals("3.78786", new Expression("MIN(3.78787,3.78786)").eval().toPlainString());
		assertEquals("3.78786", new Expression("Min(3.78786,3.78787)").eval().toPlainString());
		assertEquals("2.123", new Expression("aBs(-2.123)").eval().toPlainString());
		assertEquals("2.123", new Expression("abs(2.123)").eval().toPlainString());
	}
	
	@Test
	public void testRounding() {
		assertEquals("3.8", new Expression("round(3.78787,1)").eval().toPlainString());
		assertEquals("3.788", new Expression("round(3.78787,3)").eval().toPlainString());
		assertEquals("3.734", new Expression("round(3.7345,3)").eval().toPlainString());
		assertEquals("-3.734", new Expression("round(-3.7345,3)").eval().toPlainString());
		assertEquals("-3.79", new Expression("round(-3.78787,2)").eval().toPlainString());
		assertEquals("123.79", new Expression("round(123.78787,2)").eval().toPlainString());
		assertEquals("3", new Expression("floor(3.78787)").eval().toPlainString());
		assertEquals("4", new Expression("ceiling(3.78787)").eval().toPlainString());
		assertEquals("-3", new Expression("floor(-2.1)").eval().toPlainString());
		assertEquals("-2", new Expression("ceiling(-2.1)").eval().toPlainString());
	}
	
	@Test
	public void testMathContext() {
		Expression e;
		e = new Expression("2.5/3").setPrecision(2);
		assertEquals("0.83", e.eval().toPlainString());
		
		e = new Expression("2.5/3").setPrecision(3);
		assertEquals("0.833", e.eval().toPlainString());
		
		e = new Expression("2.5/3").setPrecision(8);
		assertEquals("0.83333333", e.eval().toPlainString());
		
		e = new Expression("2.5/3").setRoundingMode(RoundingMode.DOWN);
		assertEquals("0.8333333", e.eval().toPlainString());
		
		e = new Expression("2.5/3").setRoundingMode(RoundingMode.UP);
		assertEquals("0.8333334", e.eval().toPlainString());
	}
	
}
