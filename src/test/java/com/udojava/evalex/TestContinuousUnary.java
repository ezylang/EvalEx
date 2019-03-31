package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by samo on 2019/3/29.
 *
 * @author samo
 * @date 2019/03/29
 */
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
