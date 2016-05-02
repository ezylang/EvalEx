package com.udojava.evalex;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;


public class TestBooleans {

	@Test
	public void testAndTokenizer() {
		Expression e = new Expression("1&&0");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("&&", i.next());
		assertEquals("0", i.next());
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
}
