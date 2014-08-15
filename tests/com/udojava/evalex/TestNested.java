package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestNested {

    @Test
    public void testNestedVars() {
        String x = "1";
        String y = "2";
        String z = "2*x + 3*y";
        String a = "2*x + 4*z";

        Expression e = new Expression(a);
        e.with("x", x);
        e.with("y", y);
        e.with("z", z);

        assertEquals("34", e.eval().toString());
    }

    @Test
    public void testNestedVars2() {
        String exp = "3 * z";
        String b = "c";
        String z = "a + b";
        String a = "10";
        String c = "5";

        Expression e = new Expression(exp);
        e.with("b", b);
        e.with("z", z);
        e.with("a", a);
        e.with("c", c);

        assertEquals("45", e.eval().toString());
    }
    
    
    /**
     * Expected Exception rule
     */
    @Rule 
    public ExpectedException thrownEx = ExpectedException.none();

    @Test
    public void testCircular(){
        thrownEx.expect(RuntimeException.class);
        thrownEx.expectMessage("Circular expression detected.  Cannot evaluate.");
        
        String exp = "3 * z";
        String z = "2a + b";
        String a = "c + d";
        String d = "15";
        String b = "5";
        String c = "2z";

        Expression e = new Expression(exp);
        e.with("z", z);
        e.with("a", a);
        e.with("d", d);
        e.with("b", b);
        e.with("c", c);

        e.eval();
    }
    

    @Test
    public void testReplacements() {
        Expression e = new Expression("3+a+aa+aaa").with("a", "1*x").with("aa", "2*x").with("aaa", "3*x")
                .with("x", "2");
        assertEquals("15", e.eval().toString());
    }
}
