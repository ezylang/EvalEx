package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.junit.Test;

public class TestCustoms {

    @Test
    public void testCustomOperator() {
	EvaluationContext customContext = new EvaluationContext(
		StandardEvaluationContext.INSTANCE);
	customContext.addOperator(new Operator(">>", 30, true) {
	    @Override
	    public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
		return v1.movePointRight(v2.toBigInteger().intValue());
	    }
	});
	Expression e = new Expression("2.1234 >> 2", customContext);

	assertEquals("212.34", e.eval().toPlainString());
    }

    @Test
    public void testCustomFunction() {
	EvaluationContext customContext = new EvaluationContext(
		StandardEvaluationContext.INSTANCE);
	customContext.addFunction(new Function("average", 3) {
	    @Override
	    public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
		BigDecimal sum = parameters.get(0).add(parameters.get(1))
			.add(parameters.get(2));
		return sum.divide(new BigDecimal(3));
	    }
	});
	Expression e = new Expression("2 * average(12,4,8)", customContext);
	assertEquals("16", e.eval().toPlainString());
    }

}
