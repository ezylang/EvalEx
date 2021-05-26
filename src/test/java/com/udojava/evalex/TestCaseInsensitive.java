package com.udojava.evalex;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by showdown on 2/28/2016 at 3:29 PM. Project EvalEx
 */
public class TestCaseInsensitive {

  @Test
  public void testVariableIsCaseInsensitive() {

    Expression expression = new Expression("a");
    expression.setVariable("A", new BigDecimal(20));
    Assert.assertEquals(20, expression.eval().intValue());

    expression = new Expression("a + B");
    expression.setVariable("A", new BigDecimal(10));
    expression.setVariable("b", new BigDecimal(10));
    Assert.assertEquals(20, expression.eval().intValue());

    expression = new Expression("a+B");
    expression.setVariable("A", "c+d");
    expression.setVariable("b", new BigDecimal(10));
    expression.setVariable("C", new BigDecimal(5));
    expression.setVariable("d", new BigDecimal(5));
    Assert.assertEquals(20, expression.eval().intValue());
  }

  @Test
  public void testFunctionCaseInsensitive() {

    Expression expression = new Expression("a+testsum(1,3)");
    expression.setVariable("A", new BigDecimal(1));
    expression.addFunction(expression.new Function("testSum", -1) {
      @Override
      public BigDecimal eval(List<BigDecimal> parameters) {
        BigDecimal value = null;
        for (BigDecimal d : parameters) {
          value = value == null ? d : value.add(d);
        }
        return value;
      }
    });

    Assert.assertEquals(expression.eval(), new BigDecimal(5));

  }
}
