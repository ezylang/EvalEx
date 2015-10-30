package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

public class TestCustoms {

	@Test
	public void testCustomOperator() {
		Expression e = new Expression("2.1234 >> 2");
		
		e.addOperator(e.new Operator(">>", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				return v1.movePointRight(v2.toBigInteger().intValue());
			}
		});
		
		assertEquals("212.34", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunction() {
		Expression e = new Expression("2 * average(12,4,8)");
		e.addFunction(e.new Function("average", 3) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
				return sum.divide(new BigDecimal(3));
			}
		});
		
		assertEquals("16", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunctionVariableParameters() {
		Expression e = new Expression("2 * average(12,4,8,2,9)");
		e.addFunction(e.new Function("average", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				BigDecimal sum = new BigDecimal(0);
				for (BigDecimal parameter : parameters) {
					sum = sum.add(parameter);
				}
				return sum.divide(new BigDecimal(parameters.size()));
			}
		});
		
		assertEquals("14", e.eval().toPlainString());
	}

}
