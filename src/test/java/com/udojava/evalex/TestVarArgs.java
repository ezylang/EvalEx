package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import com.udojava.evalex.Expression.ExpressionException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import org.junit.Test;

public class TestVarArgs {

  @Test
  public void testSimple() {
    Expression e = new Expression("max(1)");
    assertEquals("1", e.eval().toPlainString());

    e = new Expression("max(4,8)");
    assertEquals("8", e.eval().toPlainString());

    e = new Expression("max(12,4,8)");
    assertEquals("12", e.eval().toPlainString());

    e = new Expression("max(12,4,8,16,32)");
    assertEquals("32", e.eval().toPlainString());
  }

  @Test
  public void testNested() {
    Expression e = new Expression("max(1,2,max(3,4,5,max(9,10,3,4,5),8),7)");
    assertEquals("10", e.eval().toPlainString());
  }

  @Test
  public void testZero() {
    Expression e = new Expression("max(0)");
    assertEquals("0", e.eval().toPlainString());

    e = new Expression("max(0,3)");
    assertEquals("3", e.eval().toPlainString());

    e = new Expression("max(2,0,-3)");
    assertEquals("2", e.eval().toPlainString());

    e = new Expression("max(-2,0,-3)");
    assertEquals("0", e.eval().toPlainString());

    e = new Expression("max(0,0,0,0)");
    assertEquals("0", e.eval().toPlainString());
  }

  @Test
  public void testError() {
    String err = "";
    Expression e = new Expression("max()");
    try {
      e.eval();
    } catch (ExpressionException ex) {
      err = ex.getMessage();
    }
    assertEquals("MAX requires at least one parameter", err);
  }

  @Test
  public void testCustomFunction1() {
    Expression e = new Expression("3 * AVG(2,4)");
    e.addFunction(e.new Function("AVG", -1) {
      @Override
      public BigDecimal eval(List<BigDecimal> parameters) {
        if (parameters.size() == 0) {
          throw new ExpressionException("AVG requires at least one parameter");
        }
        BigDecimal avg = new BigDecimal(0);
        for (BigDecimal parameter : parameters) {
          avg = avg.add(parameter);
        }
        return avg.divide(new BigDecimal(parameters.size()), MathContext.DECIMAL32);
      }
    });

    assertEquals("9", e.eval().toPlainString());
  }

  @Test
  public void testCustomFunction2() {
    Expression e = new Expression("4 * AVG(2,4,6,8,10,12)");
    e.addFunction(e.new Function("AVG", -1) {
      @Override
      public BigDecimal eval(List<BigDecimal> parameters) {
        if (parameters.size() == 0) {
          throw new ExpressionException("AVG requires at least one parameter");
        }
        BigDecimal avg = new BigDecimal(0);
        for (BigDecimal parameter : parameters) {
          avg = avg.add(parameter);
        }
        return avg.divide(new BigDecimal(parameters.size()), MathContext.DECIMAL32);
      }
    });

    assertEquals("28", e.eval().toPlainString());
  }
}
