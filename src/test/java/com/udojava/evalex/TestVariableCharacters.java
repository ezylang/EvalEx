package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestVariableCharacters {

  @Test
  public void testBadVarChar() {
    String err = "";
    try {
      Expression expression = new Expression("a.b/2*PI+MIN(e,b)");
      expression.eval();
    } catch (Expression.ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown operator . at character position 2", err);
  }

  @Test
  public void testAddedVarChar() {
    String err = "";
    Expression expression;

    try {
      expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_");
      expression.eval();
    } catch (Expression.ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown operator . at character position 2", err);

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
    assertEquals("Unknown unary operator . at character position 1", err);

    expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.")
        .setFirstVariableCharacters(".");
    assertEquals("5.859875", expression.with("a.b", "2").and("b", "3").eval().toPlainString());
  }

  @Test
  public void testFirstVarChar() {
    Expression expression = new Expression("a.b*$PI").setVariableCharacters(".")
        .setFirstVariableCharacters("$");
    assertEquals("6", expression.with("a.b", "2").and("$PI", "3").eval().toPlainString());

  }
}
