package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.udojava.evalex.Expression.LazyNumber;

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
		e.addFunction(new AbstractFunction("average", 3) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
				return sum.divide(new BigDecimal(3));
			}
		});
		
		assertEquals("16", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunctionInstanceClass() {
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
		e.addFunction(new AbstractFunction("average", -1) {
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
	
	@Test
	public void testCustomFunctionVariableParametersInstanceClass() {
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
	
	@Test
	public void testCustomFunctionStringParameters() {
        Expression e = new Expression("STREQ(\"test\", \"test2\")");
        e.addLazyFunction(e.new LazyFunction("STREQ", 2) {
            private LazyNumber ZERO = new LazyNumber() {
                public BigDecimal eval() {
                    return BigDecimal.ZERO;
                }
                
                public String getString() {
                    return "0";
                }
             };
           
            private LazyNumber ONE = new LazyNumber() {
                public BigDecimal eval() {
                    return BigDecimal.ONE;
                }
                 
                public String getString() {
                    return null;
                }
            };
          
            @Override
            public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
                if (lazyParams.get(0).getString().equals(lazyParams.get(1).getString())) {
                    return ZERO;
                }
                return ONE;
            }
        });
       
        assertEquals("1", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunctionStringParametersInstanceClass() {
        Expression e = new Expression("STREQ(\"test\", \"test2\")");
        e.addLazyFunction(e.new LazyFunction("STREQ", 2) {
            private LazyNumber ZERO = new LazyNumber() {
                public BigDecimal eval() {
                    return BigDecimal.ZERO;
                }
                
                public String getString() {
                    return "0";
                }
             };
           
            private LazyNumber ONE = new LazyNumber() {
                public BigDecimal eval() {
                    return BigDecimal.ONE;
                }
                 
                public String getString() {
                    return null;
                }
            };
          
            @Override
            public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
                if (lazyParams.get(0).getString().equals(lazyParams.get(1).getString())) {
                    return ZERO;
                }
                return ONE;
            }
        });
       
        assertEquals("1", e.eval().toPlainString());
	}

}
