package com.udojava.evalex;

import com.udojava.evalex.Expression.LazyNumber;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
		Expression e = new Expression("NUMBER_OF_YEARS(date1, date2)");
		e.addLazyFunction(new AbstractLazyFunction("NUMBER_OF_YEARS", 2) {
			private long years;

			private LazyNumber DIFFERENCE = new LazyNumber() {
				public BigDecimal eval() {
					return new BigDecimal(years);
				}

				public String getString() {
					return String.valueOf(years);
				}
			};

			@Override
			public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
				String d1 = lazyParams.get(0).getString();
				String d2 = lazyParams.get(1).getString();

				LocalDate data1 = LocalDate.parse(d1, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				LocalDate data2 = LocalDate.parse(d2, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

				years = ChronoUnit.YEARS.between(data1, data2);

				return DIFFERENCE;

			};
		});

		e.setVariable("date1", "01/01/2020");
		e.setVariable("date2", "01/01/2021");

		assertEquals("1", e.eval().toPlainString());

		e.setVariable("date1", "01/01/2020");
		e.setVariable("date2", "01/12/2020");

		assertEquals("0", e.eval().toPlainString());
	}
}   
