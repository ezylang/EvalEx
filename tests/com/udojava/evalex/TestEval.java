package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.math.RoundingMode;

import org.junit.Test;

import com.udojava.evalex.Expression.ExpressionInstance;

public class TestEval {

	@Test(expected = RuntimeException.class)
	public void testUnknow1() {
		assertEquals("", new Expression("7#9").instance().eval()
				.toString());
	}

	@Test(expected = RuntimeException.class)
	public void testUnknow2() {
		assertEquals("", new Expression("123.6*-9.8-7#9").instance()
				.eval().toString());
	}

	@Test
	public void testSimple() {
		assertEquals("3", new Expression("1+2").instance().eval()
				.toString());
		assertEquals("2", new Expression("4/2").instance().eval()
				.toString());
		assertEquals("5", new Expression("3+4/2").instance().eval()
				.toString());
		assertEquals("3.5", new Expression("(3+4)/2").instance().eval()
				.toString());
		assertEquals("7.98", new Expression("4.2*1.9").instance()
				.eval().toString());
		assertEquals("2", new Expression("8%3").instance().eval()
				.toString());
		assertEquals("0", new Expression("8%2").instance().eval()
				.toString());
	}

	@Test
	public void testPow() {
		assertEquals("16", new Expression("2^4").instance().eval()
				.toPlainString());
		assertEquals("256", new Expression("2^8").instance().eval()
				.toPlainString());
		assertEquals("9", new Expression("3^2").instance().eval()
				.toPlainString());
		assertEquals("6.25", new Expression("2.5^2").instance().eval()
				.toPlainString());
		assertEquals("28.34045", new Expression("2.6^3.5").instance()
				.eval().toPlainString());
	}

	@Test
	public void testSqrt() {
		assertEquals("4", new Expression("SQRT(16)").instance().eval()
				.toPlainString());
		assertEquals("1.4142135", new Expression("SQRT(2)").instance()
				.eval().toPlainString());
		assertEquals("1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605",
				new Expression("SQRT(2)").instance()
						.setPrecision(128).eval()
						.toPlainString());
		assertEquals("2.2360679", new Expression("SQRT(5)").instance()
				.eval().toPlainString());
		assertEquals("99.3730345", new Expression("SQRT(9875)")
				.instance().eval().toPlainString());
		assertEquals("2.3558437", new Expression("SQRT(5.55)")
				.instance().eval().toPlainString());
		assertEquals("0", new Expression("SQRT(0)").instance().eval()
				.toPlainString());
	}

	@Test
	public void testFunctions() {
		assertNotSame("1.5", new Expression("Random()").instance()
				.eval().toString());
		assertEquals("0.400349", new Expression("SIN(23.6)").instance()
				.eval().toString());
		assertEquals("8", new Expression("MAX(-7,8)").instance().eval()
				.toString());
		assertEquals("5", new Expression("MAX(3,max(4,5))").instance()
				.eval().toString());
		assertEquals("9.6", new Expression(
				"MAX(3,max(MAX(9.6,-4.2),Min(5,9)))")
				.instance().eval().toString());
		assertEquals("2.302585", new Expression("LOG(10)").instance()
				.eval().toString());
	}

	@Test
	public void testTrigonometry() {
		assertEquals("0.5", new Expression("SIN(30)").instance().eval()
				.toString());
		assertEquals("0.8660254", new Expression("cos(30)").instance()
				.eval().toString());
		assertEquals("0.5773503", new Expression("TAN(30)").instance()
				.eval().toString());
		assertEquals("5343237000000", new Expression("SINH(30)")
				.instance().eval().toPlainString());
		assertEquals("5343237000000", new Expression("COSH(30)")
				.instance().eval().toPlainString());
		assertEquals("1", new Expression("TANH(30)").instance().eval()
				.toPlainString());
		assertEquals("0.5235988", new Expression("RAD(30)").instance()
				.eval().toPlainString());
		assertEquals("1718.873", new Expression("DEG(30)").instance()
				.eval().toPlainString());

	}

	@Test
	public void testMinMaxAbs() {
		assertEquals("3.78787", new Expression("MAX(3.78787,3.78786)")
				.instance().eval().toString());
		assertEquals("3.78787", new Expression("max(3.78786,3.78787)")
				.instance().eval().toString());
		assertEquals("3.78786", new Expression("MIN(3.78787,3.78786)")
				.instance().eval().toString());
		assertEquals("3.78786", new Expression("Min(3.78786,3.78787)")
				.instance().eval().toString());
		assertEquals("2.123", new Expression("aBs(-2.123)").instance()
				.eval().toString());
		assertEquals("2.123", new Expression("abs(2.123)").instance()
				.eval().toString());
	}

	@Test
	public void testRounding() {
		assertEquals("3.8", new Expression("round(3.78787,1)")
				.instance().eval().toString());
		assertEquals("3.788", new Expression("round(3.78787,3)")
				.instance().eval().toString());
		assertEquals("3.734", new Expression("round(3.7345,3)")
				.instance().eval().toString());
		assertEquals("-3.734", new Expression("round(-3.7345,3)")
				.instance().eval().toString());
		assertEquals("-3.79", new Expression("round(-3.78787,2)")
				.instance().eval().toString());
		assertEquals("123.79", new Expression("round(123.78787,2)")
				.instance().eval().toString());
		assertEquals("3", new Expression("floor(3.78787)").instance()
				.eval().toString());
		assertEquals("4", new Expression("ceiling(3.78787)").instance()
				.eval().toString());
		assertEquals("-3", new Expression("floor(-2.1)").instance()
				.eval().toString());
		assertEquals("-2", new Expression("ceiling(-2.1)").instance()
				.eval().toString());
	}

	@Test
	public void testMathContext() {
		ExpressionInstance e = null;
		e = new Expression("2.5/3").instance().setPrecision(2);
		assertEquals("0.83", e.eval().toString());

		e = new Expression("2.5/3").instance().setPrecision(3);
		assertEquals("0.833", e.eval().toString());

		e = new Expression("2.5/3").instance().setPrecision(8);
		assertEquals("0.83333333", e.eval().toString());

		e = new Expression("2.5/3").instance().setRoundingMode(
				RoundingMode.DOWN);
		assertEquals("0.8333333", e.eval().toString());

		e = new Expression("2.5/3").instance().setRoundingMode(
				RoundingMode.UP);
		assertEquals("0.8333334", e.eval().toString());
	}

}
