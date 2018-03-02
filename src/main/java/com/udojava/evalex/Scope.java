
package com.udojava.evalex;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.udojava.evalex.Expression.LazyNumber;

public class Scope {
	/**
	 * All defined operators with name and implementation.
	 */
	protected Map<String, LazyOperator> operators = new TreeMap<String, LazyOperator>(
			String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * All defined functions with name and implementation.
	 */
	protected Map<String, com.udojava.evalex.LazyFunction> functions = new TreeMap<String, com.udojava.evalex.LazyFunction>(
			String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * All defined variables with name and value.
	 */
	protected Map<String, LazyNumber> variables = new TreeMap<String, LazyNumber>(
			String.CASE_INSENSITIVE_ORDER);
	
	public Scope() {
		super();
	}
	
	public Set<String> getDeclaredFunctionNames() {
		return Collections.unmodifiableSet(functions.keySet());
	}
	
	public Set<String> getDeclaredOperatorNames() {
		return Collections.unmodifiableSet(operators.keySet());
	}
	
	public Set<String> getDeclaredVariableNames() {
		return Collections.unmodifiableSet(variables.keySet());
	}
	
	public boolean containsFunction(String name) {
		return functions.containsKey(name);
	}
	
	public boolean containsOperator(String name) {
		return operators.containsKey(name);
	}
	
	public boolean containsVariable(String name) {
		return variables.containsKey(name);
	}
	
	public LazyFunction getFunction(String name) {
		return functions.get(name);
	}
	
	public LazyOperator getOperator(String name) {
		return operators.get(name);
	}
	
	public LazyNumber getVariable(String name) {
		return variables.get(name);
	}
	
	public Scope addFunction(String name, LazyFunction function) {
		functions.put(name, function);
		
		return this;
	}
	
	public Scope addOperator(String name, LazyOperator operator) {
		operators.put(name, operator);
		
		return this;
	}
	
	public Scope addVariable(String name, BigDecimal value) {
		variables.put(name, new LazyNumber() {
			
			@Override
			public String getString() {
				return value.toPlainString();
			}
			
			@Override
			public BigDecimal eval() {
				return value;
			}
		});
		
		return this;
	}
	
	public Scope addVariable(String name, LazyNumber value) {
		variables.put(name, value);
		
		return this;
	}
	
	public Scope and(String name, BigDecimal value) {
		return addVariable(name, value);
	}
	
	public Scope and(String name, LazyNumber value) {
		return addVariable(name, value);
	}
	
	public Scope with(String name, BigDecimal value) {
		return addVariable(name, value);
	}
	
	public Scope with(String name, LazyNumber value) {
		return addVariable(name, value);
	}
}
