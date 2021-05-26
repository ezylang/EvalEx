package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

public class TestContinuousUnary {

  @Test
  public void testContinuousUnary() {

    Expression expression = new Expression("++1");
    Assert.assertEquals(1, expression.eval().intValue());

    expression = new Expression("--1");
    Assert.assertEquals(1, expression.eval().intValue());

    expression = new Expression("+-1");
    Assert.assertEquals(-1, expression.eval().intValue());

    expression = new Expression("-+1");
    Assert.assertEquals(-1, expression.eval().intValue());

    expression = new Expression("1-+1");
    Assert.assertEquals(0, expression.eval().intValue());

    expression = new Expression("-+---+++--++-1");
    Assert.assertEquals(-1, expression.eval().intValue());

    expression = new Expression("1--++++---2+-+----1");
    Assert.assertEquals(-2, expression.eval().intValue());
  }
}
