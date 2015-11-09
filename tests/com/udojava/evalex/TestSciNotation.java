package com.udojava.evalex;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;
import org.junit.Ignore;

public class TestSciNotation {

	@Test
	public void testSimple() {
		Expression e = new Expression("1e10");
		assertEquals("10000000000", e.eval().toPlainString());
		
		e = new Expression("1E10");
		assertEquals("10000000000", e.eval().toPlainString());
		
		e = new Expression("123.456E3");
		assertEquals("123456", e.eval().toPlainString());
		
		e = new Expression("2.5e0");
		assertEquals("2.5", e.eval().toPlainString());
	}

	@Test
	public void testNegative() {
		Expression e = new Expression("1e-10");
		assertEquals("0.0000000001", e.eval().toPlainString());
		
		e = new Expression("1E-10");
		assertEquals("0.0000000001", e.eval().toPlainString());
		
		e = new Expression("2135E-4");
		assertEquals("0.2135", e.eval().toPlainString());
	}
		
	@Test //@Ignore("Expected Failures: not implemented yet")
	public void testPositive() {
		Expression e = new Expression("1e+10");
		assertEquals("10000000000", e.eval().toPlainString());
		
		e = new Expression("1E+10");
		assertEquals("10000000000", e.eval().toPlainString());
	}

	@Test
	public void testCombined() {
		Expression e = new Expression("sqrt(152.399025e6)", MathContext.DECIMAL64);
		assertEquals("12345", e.eval().toPlainString());
		
		e = new Expression("sin(3.e1)");
		assertEquals("0.5", e.eval().toPlainString());
		
		e = new Expression("sin( 3.e1)");
		assertEquals("0.5", e.eval().toPlainString());
		
		e = new Expression("sin(3.e1 )");
		assertEquals("0.5", e.eval().toPlainString());
		
		e = new Expression("sin( 3.e1 )");
		assertEquals("0.5", e.eval().toPlainString());
		
		e = new Expression("2.2e-16 * 10.2");;
		assertEquals("2.244E-15", e.eval().toString());
	}
	
	@Test(expected=NumberFormatException.class)
	public void testError1() {
		Expression e = new Expression("1234e-2.3");
		e.eval();
	}
	
	@Test(expected=NumberFormatException.class)
	public void testError2() {
		Expression e = new Expression("1234e2.3");
		e.eval();
	}
	
	@Test
	public void testError3() {
		String err = "";
		Expression e = new Expression("e2");
		try {
			e.eval();
		} catch (RuntimeException ex) {
			err = ex.getMessage();
		}
		assertEquals("Unknown operator or function: e2", err);
	}
}
