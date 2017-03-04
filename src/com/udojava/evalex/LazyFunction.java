/*
    Created By : uklimaschewski
    Modified By : iamsubhranil
    Date : 4/3/17
    Time : 10:49 PM
    Package : com.udojava.evalex
*/
package com.udojava.evalex;

import java.util.List;
import java.util.Locale;

public abstract class LazyFunction {
    /**
     * Name of this function.
     */
    private String name;
    /**
     * Number of parameters expected for this function.
     * <code>-1</code> denotes a variable number of parameters.
     */
    private int numParams;

    /**
     * Creates a new function with given name and parameter count.
     *
     * @param name
     *            The name of the function.
     * @param numParams
     *            The number of parameters for this function.
     *            <code>-1</code> denotes a variable number of parameters.
     */
    public LazyFunction(String name, int numParams) {
        this.name = name.toUpperCase(Locale.ROOT);
        this.numParams = numParams;
    }

    public String getName() {
        return name;
    }

    public int getNumParams() {
        return numParams;
    }

    public boolean numParamsVaries() {
        return numParams < 0;
    }
    public abstract LazyNumber lazyEval(List<LazyNumber> lazyParams);
}
