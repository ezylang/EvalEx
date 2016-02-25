package com.udojava.evalex;

import org.junit.Test;

/**
 * Created by showdown on 2/25/2016 at 12:27 AM.
 * Project EvalEx
 */
public class TestExposedComponents {


    @Test
    public void testGetters() {
        Expression expression = new Expression("a+b");
        System.out.println(expression.getDeclardFunctions());
        System.out.println(expression.getDeclaredVariables());
        System.out.println(expression.getDeclaredOperators());
    }

}
