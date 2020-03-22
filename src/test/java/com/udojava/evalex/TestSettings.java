package com.udojava.evalex;

import org.junit.Test;

import java.math.MathContext;

import static org.junit.Assert.assertEquals;

public class TestSettings {

    @Test
    public void testDefaults() {
        ExpressionSettings settings = ExpressionSettings.builder().build();
        assertEquals(MathContext.DECIMAL32, settings.getMathContext());
        assertEquals(Expression.OPERATOR_PRECEDENCE_POWER, settings.getPowerOperatorPrecedence());
    }

    @Test
    public void testSet() {
        ExpressionSettings settings = ExpressionSettings.builder()
                .mathContext(MathContext.DECIMAL128)
                .powerOperatorPrecedence(99)
                .build();
        assertEquals(MathContext.DECIMAL128, settings.getMathContext());
        assertEquals(99, settings.getPowerOperatorPrecedence());
    }

    @Test
    public void testSetPowerHigher() {
        ExpressionSettings settings = ExpressionSettings.builder()
                .powerOperatorPrecedenceHigher()
                .build();
        assertEquals(Expression.OPERATOR_PRECEDENCE_POWER_HIGHER,
                settings.getPowerOperatorPrecedence());
    }

    @Test
    public void testDefaultPowerPrecedence() {
        assertEquals("4", new Expression("-2^2").eval().toString());
    }

    @Test
    public void testHigherPowerPrecedence() {
        assertEquals("-4", new Expression("-2^2", ExpressionSettings.builder()
                .powerOperatorPrecedenceHigher()
                .build()).eval().toString());
    }
}
