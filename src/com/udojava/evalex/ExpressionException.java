/**
 * 
 */
package com.udojava.evalex;

/**
 * The expression evaluators exception class.
 * 
 * @author srinivasab
 */
public class ExpressionException extends RuntimeException {
    private static final long serialVersionUID = 1118142866870779047L;

    public ExpressionException(String message) {
	super(message);
    }
}
