package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.udojava.evalex.Expression.ExpressionException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.Test;


public class TestEval {

  @Test
  public void testsinAB() {
    String err = "";
    try {
      Expression expression = new Expression("sin(a+x)");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Unknown operator or function: a", err);
  }

  @Test
  public void testInvalidExpressions1() {
    String err = "";
    try {
      Expression expression = new Expression("12 18 2");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Missing operator at character position 3", err);
  }

  @Test
  public void testInvalidExpressions3() {
    String err = "";
    try {
      Expression expression = new Expression("12 + * 18");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Unknown unary operator * at character position 6", err);
  }

  @Test
  public void testInvalidExpressions4() {
    String err = "";
    try {
      Expression expression = new Expression("");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Empty expression", err);
  }

  @Test
  public void testInvalidExpressions5() {
    String err = "";
    try {
      Expression expression = new Expression("1 1+2/");
      expression.toRPN();
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Missing operator at character position 2", err);
  }

  @Test
  public void testBrackets() {
    assertEquals("3", new Expression("(1+2)").eval().toPlainString());
    assertEquals("3", new Expression("((1+2))").eval().toPlainString());
    assertEquals("3", new Expression("(((1+2)))").eval().toPlainString());
    assertEquals("9", new Expression("(1+2)*(1+2)").eval().toPlainString());
    assertEquals("10", new Expression("(1+2)*(1+2)+1").eval().toPlainString());
    assertEquals("12", new Expression("(1+2)*((1+2)+1)").eval().toPlainString());
  }

  @Test(expected = RuntimeException.class)
  public void testUnknow1() {
    assertEquals("", new Expression("7#9").eval().toPlainString());
  }

  @Test(expected = RuntimeException.class)
  public void testUnknow2() {
    assertEquals("", new Expression("123.6*-9.8-7#9").eval().toPlainString());
  }

  @Test
  public void testSimple() {
    assertEquals("3", new Expression("1+2").eval().toPlainString());
    assertEquals("2", new Expression("4/2").eval().toPlainString());
    assertEquals("5", new Expression("3+4/2").eval().toPlainString());
    assertEquals("3.5", new Expression("(3+4)/2").eval().toPlainString());
    assertEquals("7.98", new Expression("4.2*1.9").eval().toPlainString());
    assertEquals("2", new Expression("8%3").eval().toPlainString());
    assertEquals("0", new Expression("8%2").eval().toPlainString());
    assertEquals("0.2", new Expression("2*.1").eval().toPlainString());
  }

  @Test
  public void testUnaryMinus() {
    assertEquals("-3", new Expression("-3").eval().toPlainString());
    assertEquals("-2", new Expression("-SQRT(4)").eval().toPlainString());
    assertEquals("-12", new Expression("-(5*3+(+10-13))").eval().toPlainString());
    assertEquals("-2.75", new Expression("-2+3/4*-1").eval().toPlainString());
    assertEquals("9", new Expression("-3^2").eval().toPlainString());
    assertEquals("0.5", new Expression("4^-0.5").eval().toPlainString());
    assertEquals("-1.25", new Expression("-2+3/4").eval().toPlainString());
    assertEquals("-1", new Expression("-(3+-4*-1/-2)").eval().toPlainString());
    assertEquals("1.8", new Expression("2+-.2").eval().toPlainString());
  }

  @Test
  public void testUnaryPlus() {
    assertEquals("3", new Expression("+3").eval().toPlainString());
    assertEquals("4", new Expression("+(3-1+2)").eval().toPlainString());
    assertEquals("4", new Expression("+(3-(+1)+2)").eval().toPlainString());
    assertEquals("9", new Expression("+3^2").eval().toPlainString());
  }

  @Test
  public void testPow() {
    assertEquals("16", new Expression("2^4").eval().toPlainString());
    assertEquals("256", new Expression("2^8").eval().toPlainString());
    assertEquals("9", new Expression("3^2").eval().toPlainString());
    assertEquals("6.25", new Expression("2.5^2").eval().toPlainString());
    assertEquals("28.34045", new Expression("2.6^3.5").eval().toPlainString());
  }

  @Test
  public void testSqrt() {
    assertEquals("4", new Expression("SQRT(16)").eval().toPlainString());
    assertEquals("1.4142135", new Expression("SQRT(2)").eval().toPlainString());
    assertEquals(
        "1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605",
        new Expression("SQRT(2)").setPrecision(128).eval().toPlainString());
    assertEquals("2.2360679", new Expression("SQRT(5)").eval().toPlainString());
    assertEquals("99.3730345", new Expression("SQRT(9875)").eval().toPlainString());
    assertEquals("2.3558437", new Expression("SQRT(5.55)").eval().toPlainString());
    assertEquals("0", new Expression("SQRT(0)").eval().toPlainString());
  }

  @Test
  public void testFunctions() {
    assertNotSame("1.5", new Expression("Random()").eval().toPlainString());
    assertEquals("0.400349", new Expression("SIN(23.6)").eval().toPlainString());
    assertEquals("8", new Expression("MAX(-7,8)").eval().toPlainString());
    assertEquals("5", new Expression("MAX(3,max(4,5))").eval().toPlainString());
    assertEquals("9.6",
        new Expression("MAX(3,max(MAX(9.6,-4.2),Min(5,9)))").eval().toPlainString());
    assertEquals("2.302585", new Expression("LOG(10)").eval().toPlainString());
  }

  @Test
  public void testExpectedParameterNumbers() {
    String err = "";
    try {
      Expression expression = new Expression("Random(1)");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Function Random expected 0 parameters, got 1", err);

    try {
      Expression expression = new Expression("SIN(1, 6)");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Function SIN expected 1 parameters, got 2", err);
  }

  @Test
  public void testVariableParameterNumbers() {
    String err = "";
    try {
      Expression expression = new Expression("min()");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("MIN requires at least one parameter", err);

    assertEquals("1", new Expression("min(1)").eval().toPlainString());
    assertEquals("1", new Expression("min(1, 2)").eval().toPlainString());
    assertEquals("1", new Expression("min(1, 2, 3)").eval().toPlainString());
    assertEquals("3", new Expression("max(3, 2, 1)").eval().toPlainString());
    assertEquals("9", new Expression("max(3, 2, 1, 4, 5, 6, 7, 8, 9, 0)").eval().toPlainString());
  }

  @Test
  public void testOrphanedOperators() {
    String err = "";
    try {
      new Expression("/").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator / at character position 1", err);

    err = "";
    try {
      new Expression("3/").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Missing parameter(s) for operator /", err);

    err = "";
    try {
      new Expression("/3").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator / at character position 1", err);

    err = "";
    try {
      new Expression("SIN(MAX(23,45,12))/").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Missing parameter(s) for operator /", err);

  }

  @Test(expected = ExpressionException.class)
  public void closeParenAtStartCausesExpressionException() {
    new Expression("(").eval();
  }

  @Test
  public void testOrphanedOperatorsInFunctionParameters() {
    String err = "";
    try {
      new Expression("min(/)").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator / at character position 5", err);

    err = "";
    try {
      new Expression("min(3/)").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Missing parameter(s) for operator / at character position 5", err);

    err = "";
    try {
      new Expression("min(/3)").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator / at character position 5", err);

    err = "";
    try {
      new Expression("SIN(MAX(23,45,12,23.6/))").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Missing parameter(s) for operator / at character position 21", err);

    err = "";
    try {
      new Expression("SIN(MAX(23,45,12/,23.6))").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Missing parameter(s) for operator / at character position 16", err);

    err = "";
    try {
      new Expression("SIN(MAX(23,45,>=12,23.6))").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator >= at character position 15", err);

    err = "";
    try {
      new Expression("SIN(MAX(>=23,45,12,23.6))").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unknown unary operator >= at character position 9", err);
  }

  @Test
  public void testExtremeFunctionNesting() {
    assertNotSame("1.5", new Expression("Random()").eval().toPlainString());
    assertEquals("0.0002791281", new Expression("SIN(SIN(COS(23.6)))").eval().toPlainString());
    assertEquals("-4",
        new Expression("MIN(0, SIN(SIN(COS(23.6))), 0-MAX(3,4,MAX(0,SIN(1))), 10)").eval()
            .toPlainString());
  }

  @Test
  public void testTrigonometry() {
    assertEquals("0.5", new Expression("SIN(30)").eval().toPlainString());
    assertEquals("0.8660254", new Expression("cos(30)").eval().toPlainString());
    assertEquals("0.5773503", new Expression("TAN(30)").eval().toPlainString());
    assertEquals("5343237000000", new Expression("SINH(30)").eval().toPlainString());
    assertEquals("5343237000000", new Expression("COSH(30)").eval().toPlainString());
    assertEquals("1", new Expression("TANH(30)").eval().toPlainString());
    assertEquals("0.5235988", new Expression("RAD(30)").eval().toPlainString());
    assertEquals("1718.873", new Expression("DEG(30)").eval().toPlainString());
    assertEquals("30", new Expression("atan(0.5773503)").eval().toPlainString());
    assertEquals("30", new Expression("atan2(0.5773503, 1)").eval().toPlainString());
    assertEquals("33.69007", new Expression("atan2(2, 3)").eval().toPlainString());
    assertEquals("146.3099", new Expression("atan2(2, -3)").eval().toPlainString());
    assertEquals("-146.3099", new Expression("atan2(-2, -3)").eval().toPlainString());
    assertEquals("-33.69007", new Expression("atan2(-2, 3)").eval().toPlainString());
    assertEquals("1.154701", new Expression("SEC(30)").eval().toPlainString());
    assertEquals("1.414214", new Expression("SEC(45)").eval().toPlainString());
    assertEquals("2", new Expression("SEC(60)").eval().toPlainString());
    assertEquals("3.863703", new Expression("SEC(75)").eval().toPlainString());
    assertEquals("2", new Expression("CSC(30)").eval().toPlainString());
    assertEquals("1.414214", new Expression("CSC(45)").eval().toPlainString());
    assertEquals("1.154701", new Expression("CSC(60)").eval().toPlainString());
    assertEquals("1.035276", new Expression("CSC(75)").eval().toPlainString());
    assertEquals("0.0000000000001871525", new Expression("SECH(30)").eval().toPlainString());
    assertEquals("0.00000000000000000005725037", new Expression("SECH(45)").eval().toPlainString());
    assertEquals("0.00000000000000000000000001751302",
        new Expression("SECH(60)").eval().toPlainString());
    assertEquals("0.000000000000000000000000000000005357274",
        new Expression("SECH(75)").eval().toPlainString());
    assertEquals("0.0000000000001871525", new Expression("CSCH(30)").eval().toPlainString());
    assertEquals("0.00000000000000000005725037", new Expression("CSCH(45)").eval().toPlainString());
    assertEquals("0.00000000000000000000000001751302",
        new Expression("CSCH(60)").eval().toPlainString());
    assertEquals("0.000000000000000000000000000000005357274",
        new Expression("CSCH(75)").eval().toPlainString());
    assertEquals("1.732051", new Expression("COT(30)").eval().toPlainString());
    assertEquals("1", new Expression("COT(45)").eval().toPlainString());
    assertEquals("0.5773503", new Expression("COT(60)").eval().toPlainString());
    assertEquals("0.2679492", new Expression("COT(75)").eval().toPlainString());
    assertEquals("1.909152", new Expression("ACOT(30)").eval().toPlainString());
    assertEquals("1.27303", new Expression("ACOT(45)").eval().toPlainString());
    assertEquals("0.9548413", new Expression("ACOT(60)").eval().toPlainString());
    assertEquals("0.7638985", new Expression("ACOT(75)").eval().toPlainString());
    assertEquals("1", new Expression("COTH(30)").eval().toPlainString());
    assertEquals("1.199538", new Expression("COTH(1.2)").eval().toPlainString());
    assertEquals("1.016596", new Expression("COTH(2.4)").eval().toPlainString());
    assertEquals("4.094622", new Expression("ASINH(30)").eval().toPlainString());
    assertEquals("4.499933", new Expression("ASINH(45)").eval().toPlainString());
    assertEquals("4.787561", new Expression("ASINH(60)").eval().toPlainString());
    assertEquals("5.01068", new Expression("ASINH(75)").eval().toPlainString());
    assertEquals("0", new Expression("ACOSH(1)").eval().toPlainString());
    assertEquals("4.094067", new Expression("ACOSH(30)").eval().toPlainString());
    assertEquals("4.499686", new Expression("ACOSH(45)").eval().toPlainString());
    assertEquals("4.787422", new Expression("ACOSH(60)").eval().toPlainString());
    assertEquals("5.010591", new Expression("ACOSH(75)").eval().toPlainString());
    assertEquals("0", new Expression("ATANH(0)").eval().toPlainString());
    assertEquals("0.5493061", new Expression("ATANH(0.5)").eval().toPlainString());
    assertEquals("-0.5493061", new Expression("ATANH(-0.5)").eval().toPlainString());
  }

  @Test
  public void testMinMaxAbs() {
    assertEquals("3.78787", new Expression("MAX(3.78787,3.78786)").eval().toPlainString());
    assertEquals("3.78787", new Expression("max(3.78786,3.78787)").eval().toPlainString());
    assertEquals("3.78786", new Expression("MIN(3.78787,3.78786)").eval().toPlainString());
    assertEquals("3.78786", new Expression("Min(3.78786,3.78787)").eval().toPlainString());
    assertEquals("2.123", new Expression("aBs(-2.123)").eval().toPlainString());
    assertEquals("2.123", new Expression("abs(2.123)").eval().toPlainString());
  }

  @Test
  public void testRounding() {
    assertEquals("3.8", new Expression("round(3.78787,1)").eval().toPlainString());
    assertEquals("3.788", new Expression("round(3.78787,3)").eval().toPlainString());
    assertEquals("3.734", new Expression("round(3.7345,3)").eval().toPlainString());
    assertEquals("-3.734", new Expression("round(-3.7345,3)").eval().toPlainString());
    assertEquals("-3.79", new Expression("round(-3.78787,2)").eval().toPlainString());
    assertEquals("123.79", new Expression("round(123.78787,2)").eval().toPlainString());
    assertEquals("3", new Expression("floor(3.78787)").eval().toPlainString());
    assertEquals("4", new Expression("ceiling(3.78787)").eval().toPlainString());
    assertEquals("-3", new Expression("floor(-2.1)").eval().toPlainString());
    assertEquals("-2", new Expression("ceiling(-2.1)").eval().toPlainString());
  }

  @Test
  public void testMathContext() {
    Expression e = new Expression("2.5/3").setPrecision(2);
    assertEquals("0.83", e.eval().toPlainString());

    e = new Expression("2.5/3").setPrecision(3);
    assertEquals("0.833", e.eval().toPlainString());

    e = new Expression("2.5/3").setPrecision(8);
    assertEquals("0.83333333", e.eval().toPlainString());

    e = new Expression("2.5/3").setRoundingMode(RoundingMode.DOWN);
    assertEquals("0.8333333", e.eval().toPlainString());

    e = new Expression("2.5/3").setRoundingMode(RoundingMode.UP);
    assertEquals("0.8333334", e.eval().toPlainString());
  }

  @Test
  public void unknownFunctionsFailGracefully() {
    String err = "";
    try {
      new Expression("unk(1,2,3)").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Unknown function unk at character position 1", err);
  }

  @Test
  public void unknownOperatorsFailGracefully() {
    String err = "";
    try {
      new Expression("a |*| b").eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }

    assertEquals("Unknown operator |*| at character position 3", err);
  }

  @Test
  public void testNull() {
    Expression e = new Expression("null");
    assertNull(e.eval());
  }

  @Test
  public void testCalculationWithNull() {
    String err = "";
    try {
      new Expression("null+1").eval();
    } catch (ArithmeticException e) {
      err = e.getMessage();
    }
    assertEquals("First operand may not be null", err);

    err = "";
    try {
      new Expression("1 + NULL").eval();
    } catch (ArithmeticException e) {
      err = e.getMessage();
    }
    assertEquals("Second operand may not be null", err);

    err = "";
    try {
      new Expression("round(Null, 1)").eval();
    } catch (ArithmeticException e) {
      err = e.getMessage();
    }
    assertEquals("First operand may not be null", err);

    err = "";
    try {
      new Expression("round(1, NulL)").eval();
    } catch (ArithmeticException e) {
      err = e.getMessage();
    }
    assertEquals("Second operand may not be null", err);
  }

  @Test
  public void canEvalHexExpression() {
    BigDecimal result = new Expression("0xcafe").eval();
    assertEquals("51966", result.toPlainString());
  }

  @Test
  public void hexExpressionCanUseUpperCaseCharacters() {
    BigDecimal result = new Expression("0XCAFE").eval();
    assertEquals("51966", result.toPlainString());
  }

  @Test
  public void hexMinus() {
    BigDecimal result = new Expression("-0XCAFE").eval();
    assertEquals("-51966", result.toPlainString());
  }

  @Test
  public void hexMinusBlanks() {
    BigDecimal result = new Expression("  0xa + -0XCAFE  ").eval();
    assertEquals("-51956", result.toPlainString());
  }

  @Test
  public void longHexExpressionWorks() {
    BigDecimal result = new Expression("0xcafebabe", MathContext.DECIMAL128).eval();
    assertEquals("3405691582", result.toPlainString());
  }

  @Test(expected = ExpressionException.class)
  public void hexExpressionDoesNotAllowNonHexCharacters() {
    new Expression("0xbaby").eval();
  }

  @Test(expected = NumberFormatException.class)
  public void throwsExceptionIfDoesNotContainHexDigits() {
    new Expression("0x").eval();
  }

  @Test
  public void hexExpressionsEvaluatedAsExpected() {
    BigDecimal result = new Expression("0xcafe + 0xbabe").eval();
    assertEquals("99772", result.toPlainString());
  }

  @Test
  public void testResultZeroStripping() {
    Expression expression = new Expression("200.40000 / 2");
    assertEquals("100.2", expression.eval().toPlainString());
    assertEquals("100.2000", expression.eval(false).toPlainString());
  }

  @Test
  public void testImplicitMultiplication() {
    Expression expression = new Expression("22(3+1)");
    assertEquals("88", expression.eval().toPlainString());

    expression = new Expression("(a+b)(a-b)");
    assertEquals("-3", expression.with("a", "1").and("b", "2").eval().toPlainString());

    expression = new Expression("0xA(a+b)");
    assertEquals("30", expression.with("a", "1").and("b", "2").eval().toPlainString());
  }

  @Test
  public void testNoLeadingZero() {
    Expression e = new Expression("0.1 + .1");
    assertEquals("0.2", e.eval().toPlainString());

    e = new Expression(".2*.3");
    assertEquals("0.06", e.eval().toPlainString());

    e = new Expression(".2*.3+.1");
    assertEquals("0.16", e.eval().toPlainString());
  }

  @Test
  public void testUnexpectedComma() {
    String err = "";
    try {
      Expression expression = new Expression("2+3,8");
      expression.eval();
    } catch (ExpressionException e) {
      err = e.getMessage();
    }
    assertEquals("Unexpected comma at character position 3", err);
  }
}

