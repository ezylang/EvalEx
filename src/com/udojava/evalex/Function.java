/*
    Created By : iamsubhranil
    Date : 4/3/17
    Time : 10:50 PM
    Package : com.udojava.evalex
    Project : GraphPlotter
*/
package com.udojava.evalex;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract definition of a supported expression function. A function is
 * defined by a name, the number of parameters and the actual processing
 * implementation.
 */
public abstract class Function extends LazyFunction {

    public Function(String name, int numParams) {
        super(name, numParams);
    }

    public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
        final List<BigDecimal> params = new ArrayList<BigDecimal>();
        for (LazyNumber lazyParam : lazyParams) {
            params.add(lazyParam.eval());
        }
        return new LazyNumber() {
            public BigDecimal eval() {
                return Function.this.eval(params);
            }
        };
    }

    /**
     * Implementation for this function.
     *
     * @param parameters
     *            Parameters will be passed by the expression evaluator as a
     *            {@link List} of {@link BigDecimal} values.
     * @return The function must return a new {@link BigDecimal} value as a
     *         computing result.
     */
    public abstract BigDecimal eval(List<BigDecimal> parameters);
}
