package com.udojava.evalex;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestVariableCharacters {

	@Test
	public void testBadVarChar() {
		String err = "";
		try {
			//TODO will eventially be able to revert this
			Expression expression = new Expression("a.b/2*PI+MIN(e,b)").with("a", new BigDecimal(10));
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 2", err);
	}

	@Test
	public void testAddedVarChar() {
		String err = "";
		Expression expression;

		try {
			//TODO will eventially be able to revert this
			expression = new Expression("a.b/2*PI+MIN(e,b)").with("a", new BigDecimal(10)).setVariableCharacters("_");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 2", err);

		try {
			expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator or function: a.b", err);

		expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
		assertEquals("5.859875", expression.with("a.b", "2").and("b", "3").eval().toPlainString());

		try {
			expression = new Expression(".a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 1", err);

		expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.").setFirstVariableCharacters(".");
		assertEquals("5.859875", expression.with("a.b", "2").and("b", "3").eval().toPlainString());
	}

	@Test
	public void testFirstVarChar() {
		Expression expression = new Expression("a.b*$PI").setVariableCharacters(".").setFirstVariableCharacters("$");
		assertEquals("6", expression.with("a.b", "2").and("$PI", "3").eval().toPlainString());

	}
}
