package com.udojava.evalex;

import java.math.MathContext;

/**
 * Expression settings can be used to set certain defaults, when creating a new expression. Settings
 * are read only and can be created using a {@link ExpressionSettings#builder()}.
 *
 * @see Expression#Expression(String, ExpressionSettings)
 */
public class ExpressionSettings {

    /**
     * The math context to use. Default is {@link MathContext#DECIMAL32}.
     */
    private MathContext mathContext;

    /**
     * The precedence of the power (^) operator. Default is 40.
     */
    private int powerOperatorPrecedence;

    private ExpressionSettings() {
        // hide default constructor
    }

    /**
     * Create a new settings object.
     *
     * @param mathContext             The default math context to use.
     * @param powerOperatorPrecedence The default power of operator precedence.
     */
    public ExpressionSettings(MathContext mathContext, int powerOperatorPrecedence) {
        this.mathContext = mathContext;
        this.powerOperatorPrecedence = powerOperatorPrecedence;
    }

    /**
     * Get the current math context.
     *
     * @return The current math context.
     */
    public MathContext getMathContext() {
        return mathContext;
    }

    /**
     * Get the current power precedence.
     *
     * @return The current power precedence.
     */
    public int getPowerOperatorPrecedence() {
        return powerOperatorPrecedence;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder to create a read-only {@link ExpressionSettings} instance.
     */
    public static class Builder {

        private MathContext mathContext = MathContext.DECIMAL32;

        private int powerOperatorPrecedence = Expression.OPERATOR_PRECEDENCE_POWER;

        public Builder mathContext(MathContext mathContext) {
            this.mathContext = mathContext;
            return this;
        }

        public Builder powerOperatorPrecedenceHigher() {
            this.powerOperatorPrecedence = Expression.OPERATOR_PRECEDENCE_POWER_HIGHER;
            return this;
        }

        public Builder powerOperatorPrecedence(int powerOperatorPrecedence) {
            this.powerOperatorPrecedence = powerOperatorPrecedence;
            return this;
        }

        public ExpressionSettings build() {
            return new ExpressionSettings(mathContext, powerOperatorPrecedence);
        }
    }
}
