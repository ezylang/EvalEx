package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by fobbyal on 2/28/2016 at 3:48 PM.
 * Project EvalEx
 */
public class TestExposedComponents {

    @Test
    public void testDeclaredOperators() {
        Expression expression = new Expression("c+d");
        int originalOperator = expression.getDeclaredOperators().size();
        expression.addOperator(expression.new Operator("$$", -1, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                return null;
            }
        });
        Assert.assertTrue("Operator List should have the new $$ operator", expression.getDeclaredOperators().contains("$$"));
        Assert.assertEquals("Should have an extra operators", expression.getDeclaredOperators().size(), originalOperator + 1);
    }

    @Test
    public void testDeclaredVariables() {
        Expression expression = new Expression("c+d");
        int originalVarCounts = expression.getDeclaredVariables().size();
        expression.setVariable("var1", new BigDecimal(12));

        Assert.assertTrue("Variable list should have new var1 variable declared", expression.getDeclaredVariables().contains("var1"));
        Assert.assertEquals("Variable list should have q more declared variable", expression.getDeclaredVariables().size(), originalVarCounts + 1);
    }

    @Test
    public void testDeclaredFunctionGetter() {
        Expression expression = new Expression("a+b");
        int originalFunctionCount = expression.getDeclaredFunctions().size();
        expression.addFunction(new Expression.Function("func1", 3) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                return null;
            }
        });

        Assert.assertTrue("Function list should have new func1 function declared", expression.getDeclaredFunctions().contains("FUNC1"));
        Assert.assertEquals("Function list should have one more function declared", expression.getDeclaredFunctions().size(), originalFunctionCount + 1);
    }


    @Test(expected = RuntimeException.class)
    public void testUnmodifiableFunctionList() {
        Expression expression = new Expression("c+d");
        expression.getDeclaredFunctions().add("$$$");
    }

    @Test(expected = RuntimeException.class)
    public void testUnmodifiableOperatorList() {
        Expression expression = new Expression("c+d");
        expression.getDeclaredOperators().add("$$$");
    }
    @Test(expected = RuntimeException.class)
    public void testUnmodifiableVariableList() {
        Expression expression = new Expression("c+d");
        expression.getDeclaredVariables().add("$$$");
    }
}

