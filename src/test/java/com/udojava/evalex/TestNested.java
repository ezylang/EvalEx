package com.udojava.evalex;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import com.udojava.evalex.Expression.ExpressionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestNested {

	@Test
	public void testNestedVars() {
		String x = "1";
		String y = "2";
		String z = "2*x + 3*y";
		String a = "2*x + 4*z";

		Expression e = new Expression(a);
		e.with("x", x);
		e.with("y", y);
		e.with("z", z);

		assertEquals("34", e.eval().toString());
	}

	@Test
	public void testvarDefValidation() {
		String err = "";
		try {
			String a = "2*x + 4*z";
			Expression e = new Expression(a);
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("unknown var : x", err);

		try {
			String a = "2*x + 4*z";
			Expression e = new Expression(a);
			e.with("x", "1");
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("unknown var : z", err);

		try {
			String a = "2*x + 4*z";
			Expression e = new Expression(a);
			e.with("x", "1");
			e.with("z", "2 * x + z");
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("circular reference var : z", err);

		try {
			String a = "2*x + 4*z + y";
			Expression e = new Expression(a);
			e.with("x", "1");
			e.with("y", "x * z");
			e.with("z", "2 * x + y");
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("circular reference var : y", err);

		try {
			String a = "2*x + 4*z + y";
			Expression e = new Expression(a);
			e.with("x", "1");
			e.with("y", "x * 1");
			e.with("z", "2 * x + y");
			assertEquals("15", e.eval().toString());
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		try {
			String a = "a + b + c + d + e + f";
			Expression e = new Expression(a);
			e.with("a", "b");
			e.with("b", "c");
			e.with("c", "d");
			e.with("d", "e");
			e.with("e", "f");
			e.with("f", "1");
			assertEquals("6", e.eval().toString());
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		try {
			String a = "a + b + c + d + e + f";
			Expression e = new Expression(a);
			e.with("a", "b");
			e.with("b", "c");
			e.with("c", "d");
			e.with("d", "e");
			e.with("e", "f");
			e.with("f", "a");
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("circular reference var : a", err);

		try {
			String a = "a + b";
			Expression e = new Expression(a);
			e.with("a", "a + b");
			e.with("b", "0");
			e.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("circular reference var : a", err);

	}

	@Test
	public void testReplacements() {
		Expression e = new Expression("3+a+aa+aaa").with("a", "1*x")
				.with("aa", "2*x").with("aaa", "3*x").with("x", "2");
		assertEquals("15", e.eval().toString());
	}

	@Test
	public void testNestedOutOfOrderVars(){
		String x = "7";
		String y = "5";
		String z = "a+b";
		String a = "p+q";
		String p = "b+q";
		String q = "x+y";
		String b = "x*2+y";
		Expression e = new Expression(z);
		e.with("q", q);
		e.with("p", p);
		e.with("a", a);
		e.with("b", b);
		e.with("x", x);
		e.with("y", y);
		// a+b = p+q+b = b+q+q+b = 2b+2q = 2(x*2 + y) + 2(x+y) = 2(7*2+5) + 2(7+5) = 
		String res = e.eval().toString();
		assertEquals("62", res);
	}

	@Test
	public void testNestedOutOfOrderVarsWithFunc(){
		String x = "7";
		String y = "5";
		String z = "a+b";
		String a = "p+q";
		String p = "b+q";
		String q = "myAvg(x,b)";
		String b = "x*2+y";
		Expression e = new Expression(z);
		e.with("q", q);
		e.with("p", p);
		e.with("a", a);
		e.with("b", b);
		e.with("x", x);
		e.with("y", y);
		e.addFunction(e.new Function("myAvg", 2){
		
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				assertEquals(2, parameters.size());
				BigDecimal two = new BigDecimal(2);
				BigDecimal res = (parameters.get(0).add(parameters.get(1)) ).divide(two);
				return res;
			}
		});
		// a+b = p+q+b = b+q+q+b = 2b+2q = 2(x*2 + y) + 2(myAvg(x+b)) = 2(7*2+5) + 2((7+(7*2+5))/2 = 38 + 26 = 64 
		String res = e.eval().toPlainString();
		assertEquals("64", res);
	}
	
	@Test
	public void testNestedOutOfOrderVarsWithOperators(){
		MathContext mc = new MathContext(12, RoundingMode.HALF_UP);
		Expression e = new Expression("a+y", mc);
		e.addOperator(e.new Operator(">>", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
				BigDecimal res = v1.movePointRight(v2.toBigInteger().intValue());
				return res;
			}
		});
		e.with("b", "123.45678"); // e=((123.45678 >> 2))+2+y
		e.with("x", "b >> 2"); // e=((b >> 2)+2)+y
		e.with("a", "X+2"); // e=(x+2)+y
		e.with("y","5"); // e=((123.45678>>2)+2)+5 = 12345.678+2+5 = 12352.678
		BigDecimal res = e.eval();
		String resStr = res.toPlainString();
		assertEquals("12352.678", resStr);
	}
}
