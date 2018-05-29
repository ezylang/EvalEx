package com.udojava.evalex;

import org.junit.Test;


import static org.junit.Assert.assertEquals;
public class TestFactorial {


    @Test
    public void testFactorial() throws Exception {

        assertEquals("1", new Expression("FACT(0)").eval().toPlainString());
        assertEquals("1", new Expression("FACT(1)").eval().toPlainString());
        assertEquals("2", new Expression("FACT(2)").eval().toPlainString());
        assertEquals("6", new Expression("FACT(3)").eval().toPlainString());
        assertEquals("24", new Expression("FACT(4)").eval().toPlainString());

    }

}
