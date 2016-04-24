package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by showdown on 4/5/2016 at 12:19 AM.
 * Project EvalEx
 */
public class TestLazyIf {

    @Test
    public void testLazyIf() {
        Expression expression = new Expression("if(a=0,0,12/a)");
        expression.setVariable("a", new BigDecimal(0));
        Assert.assertEquals(expression.eval(),new BigDecimal(0));

    }
}
