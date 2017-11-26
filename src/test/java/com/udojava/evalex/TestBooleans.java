package com.udojava.evalex;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestBooleans {

	private void assertToken(String surface, Expression.TokenType type, Expression.Token actual) {
		assertEquals(surface, actual.surface);
		assertEquals(type, actual.type);
	}
	
	@Test
	public void testIsBoolean() {
		Expression e = new Expression("1==1");
		assertTrue(e.isBoolean());
		
		e = new Expression("a==b").with("a", "1").and("b", "2");
		assertTrue(e.isBoolean());
		
		e = new Expression("(1==1)||(c==a+b)");
		assertTrue(e.isBoolean());
		
		e = new Expression("(z+z==x-y)||(c==a+b)");
		assertTrue(e.isBoolean());
		
		e = new Expression("NOT(a+b)");
		assertTrue(e.isBoolean());
		
		e = new Expression("a+b");
		assertFalse(e.isBoolean());

		e = new Expression("(a==b)+(b==c)");
		assertFalse(e.isBoolean());
		
		e = new Expression("SQRT(2)");
		assertFalse(e.isBoolean());
		
		e = new Expression("SQRT(2) == SQRT(b)");
		assertTrue(e.isBoolean());
		
		e = new Expression("IF(a==b,x+y,x-y)");
		assertFalse(e.isBoolean());
		
		e = new Expression("IF(a==b,x==y,a==b)");
		assertTrue(e.isBoolean());
	}

	@Test
	public void testAndTokenizer() {
		Expression e = new Expression("1&&0");
		Iterator<Expression.Token> i = e.getExpressionTokenizer();

		assertToken("1", Expression.TokenType.LITERAL, i.next());
		assertToken("&&", Expression.TokenType.OPERATOR, i.next());
		assertToken("0", Expression.TokenType.LITERAL, i.next());
	}

	@Test
	public void testAndRPN() {
		assertEquals("1 0 &&", new Expression("1&&0").toRPN());
	}
	
	@Test
	public void testAndEval() {
		assertEquals("0", new Expression("1&&0").eval().toString());
		assertEquals("1", new Expression("1&&1").eval().toString());
		assertEquals("0", new Expression("0&&0").eval().toString());
		assertEquals("0", new Expression("0&&1").eval().toString());
	}
	
	@Test
	public void testOrEval() {
		assertEquals("1", new Expression("1||0").eval().toString());
		assertEquals("1", new Expression("1||1").eval().toString());
		assertEquals("0", new Expression("0||0").eval().toString());
		assertEquals("1", new Expression("0||1").eval().toString());
	}
	
	@Test
	public void testCompare() {
		assertEquals("1", new Expression("2>1").eval().toString());
		assertEquals("0", new Expression("2<1").eval().toString());
		assertEquals("0", new Expression("1>2").eval().toString());
		assertEquals("1", new Expression("1<2").eval().toString());
		assertEquals("0", new Expression("1=2").eval().toString());
		assertEquals("1", new Expression("1=1").eval().toString());
		assertEquals("1", new Expression("1>=1").eval().toString());
		assertEquals("1", new Expression("1.1>=1").eval().toString());
		assertEquals("0", new Expression("1>=2").eval().toString());
		assertEquals("1", new Expression("1<=1").eval().toString());
		assertEquals("0", new Expression("1.1<=1").eval().toString());
		assertEquals("1", new Expression("1<=2").eval().toString());
		assertEquals("0", new Expression("1=2").eval().toString());
		assertEquals("1", new Expression("1=1").eval().toString());
		assertEquals("1", new Expression("1!=2").eval().toString());
		assertEquals("0", new Expression("1!=1").eval().toString());
	}
	
	@Test
	public void testCompareCombined() {
		assertEquals("1", new Expression("(2>1)||(1=0)").eval().toString());
		assertEquals("0", new Expression("(2>3)||(1=0)").eval().toString());
		assertEquals("1", new Expression("(2>3)||(1=0)||(1&&1)").eval().toString());
	}
	
	@Test
	public void testMixed() {
		assertEquals("0", new Expression("1.5 * 7 = 3").eval().toString());
		assertEquals("1", new Expression("1.5 * 7 = 10.5").eval().toString());
	}
	
	@Test
	public void testNot() {
		assertEquals("0", new Expression("not(1)").eval().toString());
		assertEquals("1", new Expression("not(0)").eval().toString());
		assertEquals("1", new Expression("not(1.5 * 7 = 3)").eval().toString());
		assertEquals("0", new Expression("not(1.5 * 7 = 10.5)").eval().toString());
	}

	@Test
	public void testConstants() {
		assertEquals("1", new Expression("TRUE!=FALSE").eval().toString());
		assertEquals("0", new Expression("TRUE==2").eval().toString());
		assertEquals("1", new Expression("NOT(TRUE)==FALSE").eval().toString());
		assertEquals("1", new Expression("NOT(FALSE)==TRUE").eval().toString());
		assertEquals("0", new Expression("TRUE && FALSE").eval().toString());
		assertEquals("1", new Expression("TRUE || FALSE").eval().toString());
	}

	@Test
	public void testIf() {
		assertEquals("5", new Expression("if(TRUE, 5, 3)").eval().toString());
		assertEquals("3", new Expression("IF(FALSE, 5, 3)").eval().toString());
		assertEquals("5.35", new Expression("If(2, 5.35, 3)").eval().toString());
	}
	
	@Test
	public void testDecimals() {
		assertEquals("0", new Expression("if(0.0, 1, 0)").eval().toPlainString());
		assertEquals("0", new Expression("0.0 || 0.0").eval().toPlainString());
		assertEquals("1", new Expression("not(0.0)").eval().toPlainString());
		assertEquals("0", new Expression("0.0 && 0.0").eval().toPlainString());
	}
}
