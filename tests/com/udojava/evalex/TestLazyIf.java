package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    
    @Test
    public void testLazyIfWithNull() {
		String err = "";
		try {
        	new Expression("if(a,0,12/a)").setVariable("a", "null").eval();
		} catch (ArithmeticException e) {
			err = e.getMessage();
		}
		assertEquals("Operand may not be null",err);
    }

}
