package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import com.udojava.evalex.Expression.ExpressionInstance;

public class TestVariables {

	@Test
	public void testVars() {
		assertEquals("3.141593", new Expression("PI").instance().eval()
				.toString());
		assertEquals("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679",
				new Expression("PI")
						.instance()
						.setPrecision(MathContext.UNLIMITED
								.getPrecision())
						.eval().toString());
		assertEquals("3.141592653589793238462643383279503",
				new Expression("PI")
						.instance()
						.setPrecision(MathContext.DECIMAL128
								.getPrecision())
						.eval().toString());
		assertEquals("3.141592653589793",
				new Expression("PI")
						.instance()
						.setPrecision(MathContext.DECIMAL64
								.getPrecision())
						.eval().toString());
		assertEquals("3.141593",
				new Expression("PI")
						.instance()
						.setPrecision(MathContext.DECIMAL32
								.getPrecision())
						.eval().toString());
		assertEquals("6.283186", new Expression("PI*2.0").instance()
				.eval().toString());
		assertEquals("21", new Expression("3*x").instance()
				.setVariable("x", new BigDecimal("7")).eval()
				.toString());
		assertEquals("20", new Expression("(a^2)+(b^2)").instance()
				.setVariable("a", new BigDecimal("2"))
				.setVariable("b", new BigDecimal("4")).eval()
				.toPlainString());
		assertEquals("68719480000",
				new Expression("a^(2+b)^2").instance()
						.setVariable("a", "2")
						.setVariable("b", "4").eval()
						.toPlainString());
	}

	@Test
	public void testSubstitution() {
		ExpressionInstance e = new Expression("x+y").instance();
		assertEquals("2", e.with("x", "1").and("y", "1").eval()
				.toPlainString());
		assertEquals("1", e.with("y", "0").eval().toPlainString());
		assertEquals("0", e.with("x", "0").eval().toPlainString());
	}

	@Test
	public void testWith() {
		assertEquals("21",
				new Expression("3*x").instance()
						.with("x", new BigDecimal("7"))
						.eval().toString());
		assertEquals("20",
				new Expression("(a^2)+(b^2)").instance()
						.with("a", new BigDecimal("2"))
						.with("b", new BigDecimal("4"))
						.eval().toPlainString());
		assertEquals("68719480000", new Expression("a^(2+b)^2")
				.instance().with("a", "2").with("b", "4")
				.eval().toPlainString());
	}

	@Test
	public void testNames() {
		assertEquals("21", new Expression("3*longname").instance()
				.with("longname", new BigDecimal("7")).eval()
				.toString());

		assertEquals("21", new Expression("3*longname1").instance()
				.with("longname1", new BigDecimal("7")).eval()
				.toString());
	}

	@Test
	public void testVariableNames() {
		assertEquals("longname2", new Expression(
				"3*(longname1)*45.454 * longname2")
				.getVariableNames().toArray()[1]);
	}

	@Test(expected = RuntimeException.class)
	public void testFailure() {
		// When eval is called without variables set, then Runtime
		// Exception
		// must be thrown.
		new Expression("3*(longname1)*45.454 * longname2").instance()
				.eval();
	}
}
