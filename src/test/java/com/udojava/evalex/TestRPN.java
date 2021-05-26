package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TestRPN {

  @Test
  public void testSimple() {

    assertEquals("1 2 +", new Expression("1+2").toRPN());
    assertEquals("1 2 4 / +", new Expression("1+2/4").toRPN());
    assertEquals("1 2 + 4 /", new Expression("(1+2)/4").toRPN());
    assertEquals("1.9 2.8 + 4.7 /", new Expression("(1.9+2.8)/4.7").toRPN());
    assertEquals("1.98 2.87 + 4.76 /", new Expression("(1.98+2.87)/4.76").toRPN());
    assertEquals("3 4 2 * 1 5 - 2 3 ^ ^ / +",
        new Expression("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3").toRPN());
  }

  @Test
  public void testFunctions() {

    assertEquals("( 23.6 SIN", new Expression("SIN(23.6)").toRPN());
    assertEquals("( 7 -u 8 MAX", new Expression("MAX(-7,8)").toRPN());
    assertEquals("( ( 3.7 SIN ( 2.6 8.0 -u MAX MAX",
        new Expression("MAX(SIN(3.7),MAX(2.6,-8.0))").toRPN());
  }

  @Test
  public void testOperatorsInFunctions() {

    assertEquals("( 23.6 4 / SIN", new Expression("SIN(23.6/4)").toRPN());
  }

  @Test
  public void testNested() {
    Expression e = new Expression("x+y");
    e.with("x", "a+b");
    assertEquals("a b + y +", e.toRPN());
  }

  @Test
  public void testComplexNested() {
    Expression e = new Expression("x+y");
    e.with("a", "p/q");
    e.with("p", "q*y");
    e.with("x", "p*12"); // x y + = p 12 * y +
    e.with("y", "a"); // p 12 * p q / +
    e.with("p", "15");
    e.with("q", "14");
    String rpn = e.toRPN();
    assertEquals("p 12 * p q / +", rpn);

  }
}
