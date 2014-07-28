package com.udojava.evalex;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;


public class TestTokenizer {
	
	@Test
	public void testNumbers() {
		Expression e;
		Iterator<String> i;
		
		e = new Expression("1");
		i = e.getExpressionTokenizer();
		assertEquals("1", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
		
		e = new Expression("-1");
		i = e.getExpressionTokenizer();
		assertEquals("-1", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
		
		e = new Expression("123");
		i = e.getExpressionTokenizer();
		assertEquals("123", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
		
		e = new Expression("-123");
		i = e.getExpressionTokenizer();
		assertEquals("-123", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
		
		e = new Expression("123.4");
		i = e.getExpressionTokenizer();
		assertEquals("123.4", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
		
		e = new Expression("-123.456");
		i = e.getExpressionTokenizer();
		assertEquals("-123.456", i.next());
		assertFalse(i.hasNext());
		assertNull(i.next());
	}
	
	@Test
	public void testTokenizer1() {
		Expression e = new Expression("1+2");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("+", i.next());
		assertEquals("2", i.next());
	}

	@Test
	public void testTokenizer2() {
		Expression e = new Expression("1 + 2");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("+", i.next());
		assertEquals("2", i.next());
	}
	
	@Test
	public void testTokenizer3() {
		Expression e = new Expression(" 1 + 2 ");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("+", i.next());
		assertEquals("2", i.next());
	}
	
	@Test
	public void testTokenizer4() {
		Expression e = new Expression("1+2-3/4*5");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("+", i.next());
		assertEquals("2", i.next());
		assertEquals("-", i.next());
		assertEquals("3", i.next());
		assertEquals("/", i.next());
		assertEquals("4", i.next());
		assertEquals("*", i.next());
		assertEquals("5", i.next());
	}
		
	@Test
	public void testTokenizer5() {
		Expression e = new Expression("1+2.1-3.45/4.982*5.0");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("+", i.next());
		assertEquals("2.1", i.next());
		assertEquals("-", i.next());
		assertEquals("3.45", i.next());
		assertEquals("/", i.next());
		assertEquals("4.982", i.next());
		assertEquals("*", i.next());
		assertEquals("5.0", i.next());
	}
	
	@Test
	public void testTokenizer6() {
		Expression e = new Expression("-3+4*-1");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("-3", i.next());
		assertEquals("+", i.next());
		assertEquals("4", i.next());
		assertEquals("*", i.next());
		assertEquals("-1", i.next());
	}
	
	@Test
	public void testTokenizer7() {
		Expression e = new Expression("(-3+4)*-1/(7-(5*-8))");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("(", i.next());
		assertEquals("-3", i.next());
		assertEquals("+", i.next());
		assertEquals("4", i.next());
		assertEquals(")", i.next());
		assertEquals("*", i.next());
		assertEquals("-1", i.next());
		assertEquals("/", i.next());
		assertEquals("(", i.next());
		assertEquals("7", i.next());
		assertEquals("-", i.next());
		assertEquals("(", i.next());
		assertEquals("5", i.next());
		assertEquals("*", i.next());
		assertEquals("-8", i.next());
		assertEquals(")", i.next());
		assertEquals(")", i.next());
	}
	
	public void testTokenizer8() {
		Expression e = new Expression("(1.9+2.8)/4.7");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("(", i.next());
		assertEquals("1.9", i.next());
		assertEquals("+", i.next());
		assertEquals("2.8", i.next());
		assertEquals(")", i.next());
		assertEquals("/", i.next());
		assertEquals("4.7", i.next());		
	}
	
	@Test
	public void testTokenizerFunction1() {
		Expression e = new Expression("ABS(3.5)");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("ABS", i.next());
		assertEquals("(", i.next());
		assertEquals("3.5", i.next());
		assertEquals(")", i.next());
	}
	
	@Test
	public void testTokenizerFunction2() {
		Expression e = new Expression("3-ABS(3.5)/9");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("3", i.next());
		assertEquals("-", i.next());
		assertEquals("ABS", i.next());
		assertEquals("(", i.next());
		assertEquals("3.5", i.next());
		assertEquals(")", i.next());
		assertEquals("/", i.next());
		assertEquals("9", i.next());
	}
	@Test
	
	public void testTokenizerFunction3() {
		Expression e = new Expression("MAX(3.5,5.2)");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("MAX", i.next());
		assertEquals("(", i.next());
		assertEquals("3.5", i.next());
		assertEquals(",", i.next());
		assertEquals("5.2", i.next());
		assertEquals(")", i.next());
	}
	
	@Test
	public void testTokenizerFunction4() {
		Expression e = new Expression("3-MAX(3.5,5.2)/9");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("3", i.next());
		assertEquals("-", i.next());
		assertEquals("MAX", i.next());
		assertEquals("(", i.next());
		assertEquals("3.5", i.next());
		assertEquals(",", i.next());
		assertEquals("5.2", i.next());
		assertEquals(")", i.next());
		assertEquals("/", i.next());
		assertEquals("9", i.next());
	}
	
	@Test
	public void testTokenizerFunction5() {
		Expression e = new Expression("3/MAX(-3.5,-5.2)/9");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("3", i.next());
		assertEquals("/", i.next());
		assertEquals("MAX", i.next());
		assertEquals("(", i.next());
		assertEquals("-3.5", i.next());
		assertEquals(",", i.next());
		assertEquals("-5.2", i.next());
		assertEquals(")", i.next());
		assertEquals("/", i.next());
		assertEquals("9", i.next());
	}
}
