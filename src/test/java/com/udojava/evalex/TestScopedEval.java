package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class TestScopedEval {
	@Test
	public void testScopes() {
		Expression expression = new Expression("a+b+c")
				.with("a", "10")
				.with("b", "10");
		
		Scope scope = new Scope()
				.with("b", new BigDecimal("100"))
				.with("c", new BigDecimal("1000"));
		
		assertEquals("1110", expression.eval(scope).toPlainString());
	}
}
