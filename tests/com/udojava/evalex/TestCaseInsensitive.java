package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by showdown on 2/24/2016 at 11:57 PM.
 * Project EvalEx
 */
public class TestCaseInsensitive {
    @Test
    public void testVariableIsCaseInsensitive() {

        Expression expression = new Expression("a");
        expression.setVariable("A", new BigDecimal(20));
        Assert.assertEquals(expression.eval().intValue(),20);

        expression = new Expression("a + B");
        expression.setVariable("A", new BigDecimal(10));
        expression.setVariable("b", new BigDecimal(10));
        Assert.assertEquals(expression.eval().intValue(),20);

        expression = new Expression("a+B");
        expression.setVariable("A", "c+d");
        expression.setVariable("b", new BigDecimal(10));
        expression.setVariable("C", new BigDecimal(5));
        expression.setVariable("d", new BigDecimal(5));
        Assert.assertEquals(expression.eval().intValue(),20);
    }
}
