package com.udojava.evalex;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestCompilation {

    @Test
    public void canCompileExpressionWithoutVariablesPresent() throws Exception {
        Expression e = new Expression("a+b");
        e.compile();
    }

    @Test
    public void canReuseSameExpressionObjectWithMultipleVariableValues() throws Exception {
        Expression e = new Expression("a+b");
        e.compile();

        e.setVariable("a", BigDecimal.ONE);
        e.setVariable("b", BigDecimal.TEN);

        assertEquals("11", e.eval().toPlainString());

        e.setVariable("a", BigDecimal.ZERO);
        e.setVariable("b", BigDecimal.ONE);

        assertEquals("1", e.eval().toPlainString());
    }

    @Test
    public void canProvideMapAtEvalTime() throws Exception {
        Expression e = new Expression("a+b");
        e.compile();

        Map<String, BigDecimal> vars = new HashMap<>();
        vars.put("a", BigDecimal.ONE);
        vars.put("b", BigDecimal.TEN);

        assertEquals("11", e.eval(vars).toPlainString());

        vars.put("b", BigDecimal.ZERO);
        assertEquals("1", e.eval(vars).toPlainString());
    }
}
