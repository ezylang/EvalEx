/**
 * 
 */
package com.udojava.evalex;

import java.util.HashMap;
import java.util.Map;

/**
 * * <h2>Add Custom Operators</h2>
 * 
 * Custom operators can be added easily, simply create an instance of
 * `Expression.Operator` and add it to the expression. Parameters are the
 * operator string, its precedence and if it is left associative. The operators
 * `eval()` method will be called with the BigDecimal values of the operands.
 * All existing operators can also be overridden. <br>
 * For example, add an operator `x >> n`, that moves the decimal point of _x_
 * _n_ digits to the right:
 * 
 * <pre>
 * 
 * EvaluationContext customContext = new EvaluationContext(
 * 		StandardEvaluationContext.INSTANCE);
 * customContext.addOperator(new Operator(&quot;&gt;&gt;&quot;, 30, true) {
 * 	&#064;Override
 * 	public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
 * 		return v1.movePointRight(v2.toBigInteger().intValue());
 * 	}
 * });
 * Expression e = new Expression(&quot;2.1234 &gt;&gt; 2&quot;, customContext)//returns 212.34;
 * </pre>
 * 
 * <br>
 * <h2>Add Custom Functions</h2>
 * 
 * Adding custom functions is as easy as adding custom operators. Create an
 * instance of `Expression.Function`and add it to the expression. Parameters are
 * the function name and the count of required parameters. The functions
 * `eval()` method will be called with a list of the BigDecimal parameters. All
 * existing functions can also be overridden. <br>
 * For example, add a function `average(a,b,c)`, that will calculate the average
 * value of a, b and c: <br>
 * 
 * <pre>
 * EvaluationContext customContext = new EvaluationContext(
 * 		StandardEvaluationContext.INSTANCE);
 * customContext.addFunction(new Function(&quot;average&quot;, 3) {
 * 	&#064;Override
 * 	public BigDecimal eval(List&lt;BigDecimal&gt; parameters, MathContext mc) {
 * 		BigDecimal sum = parameters.get(0).add(parameters.get(1))
 * 				.add(parameters.get(2));
 * 		return sum.divide(new BigDecimal(3));
 * 	}
 * });
 * Expression e = new Expression(&quot;2 * average(12,4,8)&quot;, customContext);
 * 
 * e.eval(); // returns 16
 * </pre>
 * 
 * @author Udo Klimaschewski (http://about.me/udo.klimaschewski),Srinivasa Rao
 *         Boyina
 * 
 */
public class EvaluationContext {
	/**
	 * All defined operators with name and implementation.
	 */
	private Map<String, Operator> operators = new HashMap<String, Operator>();

	/**
	 * All defined functions with name and implementation.
	 */
	private Map<String, Function> functions = new HashMap<String, Function>();

	public EvaluationContext() {

	}

	public EvaluationContext(EvaluationContext ec) {
		if (ec != null) {
			ec = StandardEvaluationContext.INSTANCE;
			this.operators = new HashMap<String, Operator>(
					ec.operators);
			this.functions = new HashMap<String, Function>(
					ec.functions);
		}
	}

	public boolean hasOperator(String operatorName) {
		if (operatorName != null) {
			return this.operators.containsKey(operatorName);
		}
		return false;
	}

	/**
	 * Adds an operator to the list of supported operators.
	 * 
	 * @param operator
	 *                The operator to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	public Operator addOperator(Operator operator) {
		return operators.put(operator.getOper(), operator);
	}

	public Operator getOperator(String operatorName) {
		if (operatorName != null) {
			return this.operators.get(operatorName.toLowerCase());
		}
		return null;
	}

	/**
	 * Adds a function to the list of supported functions
	 * 
	 * @param function
	 *                The function to add.
	 * @return The previous operator with that name, or <code>null</code> if
	 *         there was none.
	 */
	public Function addFunction(Function function) {
		return functions.put(function.getName(), function);
	}

	public boolean hasFunction(String functionName) {
		if (functionName != null) {
			return this.functions.containsKey(functionName
					.toUpperCase());
		}
		return false;
	}

	public Function getFunction(String functionName) {
		if (functionName != null) {
			return this.functions.get(functionName.toUpperCase());
		}
		return null;
	}
}
