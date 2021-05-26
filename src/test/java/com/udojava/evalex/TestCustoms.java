package com.udojava.evalex;

import com.udojava.evalex.Expression.LazyNumber;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testCustomFunctionBoolean() {
        Expression e = new Expression("STREQ(\"test\", \"test2\")");
        e.addLazyFunction(e.new LazyFunction("STREQ", 2, true) {
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
        assertTrue(e.isBoolean());
    }

    @Test
    public void testUnary() {
        Expression exp = new Expression("~23");

        exp.addOperator(new AbstractUnaryOperator("~", 60, false) {
            @Override
            public BigDecimal evalUnary(BigDecimal bigDecimal) {
                return bigDecimal;
            }
        });
        assertEquals("23", exp.eval().toPlainString());
    }
    
    @Test
    public void testUnaryPostfix() {
        Expression exp = new Expression("1+4!");

        exp.addOperator(new AbstractOperator("!", 61, true, false, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
		        if(v1.remainder(BigDecimal.ONE) != BigDecimal.ZERO) {
		            throw new ArithmeticException("Operand must be an integer");
		        }
		        BigDecimal factorial = v1;
		        v1 = v1.subtract(BigDecimal.ONE);
		        if (factorial.compareTo(BigDecimal.ZERO) == 0 || factorial.compareTo(BigDecimal.ONE) == 0) {
		            return BigDecimal.ONE;
		        } else {
		            while (v1.compareTo(BigDecimal.ONE) > 0) {
		                factorial = factorial.multiply(v1);
		                v1 = v1.subtract(BigDecimal.ONE);
		            }
		            return factorial;
		        }
		    }
        });
        assertEquals("25", exp.eval().toPlainString());
    }

    @Test
    public void testCustomOperatorAnd() {
        Expression e = new Expression("1 == 1 AND 2 == 2");

        e.addOperator(e.new Operator("AND", Expression.OPERATOR_PRECEDENCE_AND, false, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                boolean b1 = v1.compareTo(BigDecimal.ZERO) != 0;

                if (!b1) {
                    return BigDecimal.ZERO;
                }

                boolean b2 = v2.compareTo(BigDecimal.ZERO) != 0;
                return b2 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        assertEquals("1", e.eval().toPlainString());
    }

    @Test
    public void testCustomFunctionWithStringParams() {
        Expression e = new Expression("INDEXOF(STRING1, STRING2)");
        e.addLazyFunction(new AbstractLazyFunction("INDEXOF", 2) {
            @Override
            public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
                String st1 = lazyParams.get(0).getString();
                String st2 = lazyParams.get(1).getString();
                final int index = st1.indexOf(st2);

                return new LazyNumber() {
                    @Override
                    public BigDecimal eval() {
                        return new BigDecimal(index);
                    }

                    @Override
                    public String getString() {
                        return Integer.toString(index);
                    }
                };
            }
        });

        e.setVariable("STRING1", "The quick brown fox");
        e.setVariable("STRING2", "The");

        assertEquals("0", e.eval().toPlainString());

        e.setVariable("STRING1", "The quick brown fox");
        e.setVariable("STRING2", "brown");

        assertEquals("10", e.eval().toPlainString());
    }
}   
