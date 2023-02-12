/*
  Copyright 2012-2022 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EvalEx2CompatibilityTest {

  @Test
  void testContinuousUnary() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("++1")).isEqualByComparingTo("1");
    assertThat(evaluateToNumber("--1")).isEqualByComparingTo("1");
    assertThat(evaluateToNumber("+-1")).isEqualByComparingTo("-1");
    assertThat(evaluateToNumber("-+1")).isEqualByComparingTo("-1");
    assertThat(evaluateToNumber("1-+1")).isEqualByComparingTo("0");
    assertThat(evaluateToNumber("-+---+++--++-1")).isEqualByComparingTo("-1");
    assertThat(evaluateToNumber("1--++++---2+-+----1")).isEqualByComparingTo("-2");
  }

  @Test
  void testBrackets() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("(1+2)")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("((1+2))")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("(((1+2)))")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("(1+2)*(1+2)")).isEqualByComparingTo("9");
    assertThat(evaluateToNumber("(1+2)*(1+2)+1")).isEqualByComparingTo("10");
    assertThat(evaluateToNumber("(1+2)*((1+2)+1)")).isEqualByComparingTo("12");
  }

  @Test
  void testSimple() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("1+2")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("4/2")).isEqualByComparingTo("2");
    assertThat(evaluateToNumber("3+4/2")).isEqualByComparingTo("5");
    assertThat(evaluateToNumber("(3+4)/2")).isEqualByComparingTo("3.5");
    assertThat(evaluateToNumber("4.2*1.9")).isEqualByComparingTo("7.98");
    assertThat(evaluateToNumber("8%3")).isEqualByComparingTo("2");
    assertThat(evaluateToNumber("8%2")).isEqualByComparingTo("0");
    assertThat(evaluateToNumber("2*.1")).isEqualByComparingTo("0.2");
  }

  @Test
  void testUnaryMinus() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("-3")).isEqualByComparingTo("-3");
    assertThat(evaluateToNumber("-SQRT(4)")).isEqualByComparingTo("-2");
    assertThat(evaluateToNumber("-(5*3+(+10-13))")).isEqualByComparingTo("-12");
    assertThat(evaluateToNumber("-2+3/4*-1")).isEqualByComparingTo("-2.75");
    assertThat(evaluateToNumber("-3^2")).isEqualByComparingTo("9");
    assertThat(evaluateToNumber("4^-0.5")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("-2+3/4")).isEqualByComparingTo("-1.25");
    assertThat(evaluateToNumber("-(3+-4*-1/-2)")).isEqualByComparingTo("-1");
    assertThat(evaluateToNumber("2+-.2")).isEqualByComparingTo("1.8");
  }

  @Test
  void testUnaryPlus() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("+3")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("+(3-1+2)")).isEqualByComparingTo("4");
    assertThat(evaluateToNumber("+(3-(+1)+2)")).isEqualByComparingTo("4");
    assertThat(evaluateToNumber("+3^2")).isEqualByComparingTo("9");
  }

  @Test
  void testPow() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("2^4")).isEqualByComparingTo("16");
    assertThat(evaluateToNumber("2^8")).isEqualByComparingTo("256");
    assertThat(evaluateToNumber("3^2")).isEqualByComparingTo("9");
    assertThat(evaluateToNumber("2.5^2")).isEqualByComparingTo("6.25");
    assertThat(evaluateToNumber("2.6^3.5")).isEqualByComparingTo("28.34045");
  }

  @Test
  void testSqrt() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("SQRT(16)")).isEqualByComparingTo("4");
    assertThat(evaluateToNumber("SQRT(2)")).isEqualByComparingTo("1.4142135");

    Expression expression =
        new Expression(
            "SQRT(2)", ExpressionConfiguration.builder().mathContext(new MathContext(128)).build());
    assertThat(expression.evaluate().getNumberValue())
        .isEqualByComparingTo(
            "1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605");

    assertThat(evaluateToNumber("SQRT(5)")).isEqualByComparingTo("2.2360679");
    assertThat(evaluateToNumber("SQRT(9875)")).isEqualByComparingTo("99.3730345");
    assertThat(evaluateToNumber("SQRT(5.55)")).isEqualByComparingTo("2.3558437");
    assertThat(evaluateToNumber("SQRT(0)")).isEqualByComparingTo("0");
  }

  @Test
  void testTrigonometry() throws EvaluationException, ParseException { // NOSONAR - >=25 assertions
    assertThat(evaluateToNumber("SIN(30)")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("COS(30)")).isEqualByComparingTo("0.8660254");
    assertThat(evaluateToNumber("TAN(30)")).isEqualByComparingTo("0.5773503");
    assertThat(evaluateToNumber("SINH(30)")).isEqualByComparingTo("5343237000000");
    assertThat(evaluateToNumber("COSH(30)")).isEqualByComparingTo("5343237000000");
    assertThat(evaluateToNumber("TANH(30)")).isEqualByComparingTo("1");
    assertThat(evaluateToNumber("RAD(30)")).isEqualByComparingTo("0.5235988");
    assertThat(evaluateToNumber("DEG(30)")).isEqualByComparingTo("1718.873");
    assertThat(evaluateToNumber("ATAN(0.5773503)")).isEqualByComparingTo("30");
    assertThat(evaluateToNumber("ATAN2(0.5773503, 1)")).isEqualByComparingTo("30");
    assertThat(evaluateToNumber("ATAN2(2, 3)")).isEqualByComparingTo("33.69007");
    assertThat(evaluateToNumber("ATAN2(2, -3)")).isEqualByComparingTo("146.3099");
    assertThat(evaluateToNumber("ATAN2(-2, -3)")).isEqualByComparingTo("-146.3099");
    assertThat(evaluateToNumber("ATAN2(-2, 3)")).isEqualByComparingTo("-33.69007");
    assertThat(evaluateToNumber("SEC(30)")).isEqualByComparingTo("1.154701");
    assertThat(evaluateToNumber("SEC(45)")).isEqualByComparingTo("1.414214");
    assertThat(evaluateToNumber("SEC(60)")).isEqualByComparingTo("2");
    assertThat(evaluateToNumber("SEC(75)")).isEqualByComparingTo("3.863703");
    assertThat(evaluateToNumber("CSC(30)")).isEqualByComparingTo("2");
    assertThat(evaluateToNumber("CSC(45)")).isEqualByComparingTo("1.414214");
    assertThat(evaluateToNumber("CSC(60)")).isEqualByComparingTo("1.154701");
    assertThat(evaluateToNumber("CSC(75)")).isEqualByComparingTo("1.035276");
    assertThat(evaluateToNumber("SECH(30)")).isEqualByComparingTo("0.0000000000001871525");
    assertThat(evaluateToNumber("SECH(45)")).isEqualByComparingTo("0.00000000000000000005725037");
    assertThat(evaluateToNumber("SECH(60)"))
        .isEqualByComparingTo("0.00000000000000000000000001751302");
    assertThat(evaluateToNumber("SECH(75)"))
        .isEqualByComparingTo("0.000000000000000000000000000000005357274");
    assertThat(evaluateToNumber("CSCH(30)")).isEqualByComparingTo("0.0000000000001871525");
    assertThat(evaluateToNumber("CSCH(45)")).isEqualByComparingTo("0.00000000000000000005725037");
    assertThat(evaluateToNumber("CSCH(60)"))
        .isEqualByComparingTo("0.00000000000000000000000001751302");
    assertThat(evaluateToNumber("CSCH(75)"))
        .isEqualByComparingTo("0.000000000000000000000000000000005357274");
    assertThat(evaluateToNumber("COT(30)")).isEqualByComparingTo("1.732051");
    assertThat(evaluateToNumber("COT(45)")).isEqualByComparingTo("1");
    assertThat(evaluateToNumber("COT(60)")).isEqualByComparingTo("0.5773503");
    assertThat(evaluateToNumber("COT(75)")).isEqualByComparingTo("0.2679492");
    assertThat(evaluateToNumber("ACOT(30)")).isEqualByComparingTo("1.909152");
    assertThat(evaluateToNumber("ACOT(45)")).isEqualByComparingTo("1.27303");
    assertThat(evaluateToNumber("ACOT(60)")).isEqualByComparingTo("0.9548413");
    assertThat(evaluateToNumber("ACOT(75)")).isEqualByComparingTo("0.7638985");
    assertThat(evaluateToNumber("COTH(30)")).isEqualByComparingTo("1");
    assertThat(evaluateToNumber("COTH(1.2)")).isEqualByComparingTo("1.199538");
    assertThat(evaluateToNumber("COTH(2.4)")).isEqualByComparingTo("1.016596");
    assertThat(evaluateToNumber("ASINH(30)")).isEqualByComparingTo("4.094622");
    assertThat(evaluateToNumber("ASINH(45)")).isEqualByComparingTo("4.499933");
    assertThat(evaluateToNumber("ASINH(60)")).isEqualByComparingTo("4.787561");
    assertThat(evaluateToNumber("ASINH(75)")).isEqualByComparingTo("5.01068");
    assertThat(evaluateToNumber("ACOSH(1)")).isEqualByComparingTo("0");
    assertThat(evaluateToNumber("ACOSH(30)")).isEqualByComparingTo("4.094067");
    assertThat(evaluateToNumber("ACOSH(45)")).isEqualByComparingTo("4.499686");
    assertThat(evaluateToNumber("ACOSH(60)")).isEqualByComparingTo("4.787422");
    assertThat(evaluateToNumber("ACOSH(75)")).isEqualByComparingTo("5.010591");
    assertThat(evaluateToNumber("ATANH(0)")).isEqualByComparingTo("0");
    assertThat(evaluateToNumber("ATANH(0.5)")).isEqualByComparingTo("0.5493061");
    assertThat(evaluateToNumber("ATANH(-0.5)")).isEqualByComparingTo("-0.5493061");
  }

  @Test
  void testMinMaxAbs() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("MAX(3.78787,3.78786)")).isEqualByComparingTo("3.78787");
    assertThat(evaluateToNumber("max(3.78786,3.78787)")).isEqualByComparingTo("3.78787");
    assertThat(evaluateToNumber("MIN(3.78787,3.78786)")).isEqualByComparingTo("3.78786");
    assertThat(evaluateToNumber("Min(3.78786,3.78787)")).isEqualByComparingTo("3.78786");
    assertThat(evaluateToNumber("aBs(-2.123)")).isEqualByComparingTo("2.123");
    assertThat(evaluateToNumber("abs(2.123)")).isEqualByComparingTo("2.123");
  }

  @Test
  void testRounding() throws EvaluationException, ParseException {
    assertThat(evaluateToNumber("round(3.78787,1)")).isEqualByComparingTo("3.8");
    assertThat(evaluateToNumber("round(3.78787,3)")).isEqualByComparingTo("3.788");
    assertThat(evaluateToNumber("round(3.7345,3)")).isEqualByComparingTo("3.734");
    assertThat(evaluateToNumber("round(-3.7345,3)")).isEqualByComparingTo("-3.734");
    assertThat(evaluateToNumber("round(-3.78787,2)")).isEqualByComparingTo("-3.79");
    assertThat(evaluateToNumber("round(123.78787,2)")).isEqualByComparingTo("123.79");
    assertThat(evaluateToNumber("floor(3.78787)")).isEqualByComparingTo("3");
    assertThat(evaluateToNumber("ceiling(3.78787)")).isEqualByComparingTo("4");
    assertThat(evaluateToNumber("floor(-2.1)")).isEqualByComparingTo("-3");
    assertThat(evaluateToNumber("ceiling(-2.1)")).isEqualByComparingTo("-2");
  }

  @Test
  void testSciNotation() throws EvaluationException, ParseException {
    // simple
    assertThat(evaluateToNumber("1e10")).isEqualByComparingTo("10000000000");
    assertThat(evaluateToNumber("1E10")).isEqualByComparingTo("10000000000");
    assertThat(evaluateToNumber("123.456E3")).isEqualByComparingTo("123456");
    assertThat(evaluateToNumber("2.5e0")).isEqualByComparingTo("2.5");

    // negative
    assertThat(evaluateToNumber("1e-10")).isEqualByComparingTo("0.0000000001");
    assertThat(evaluateToNumber("1E-10")).isEqualByComparingTo("0.0000000001");
    assertThat(evaluateToNumber("2135E-4")).isEqualByComparingTo("0.2135");

    // positive
    assertThat(evaluateToNumber("1e+10")).isEqualByComparingTo("10000000000");
    assertThat(evaluateToNumber("1E+10")).isEqualByComparingTo("10000000000");

    // combined
    assertThat(evaluateToNumber("sqrt(152.399025e6)")).isEqualByComparingTo("12344.9989874");
    assertThat(evaluateToNumber("sin(3.e1)")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("sin( 3.e1)")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("sin(3.e1 )")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("sin( 3.e1 )")).isEqualByComparingTo("0.5");
    assertThat(evaluateToNumber("2.2e-16 * 10.2")).isEqualByComparingTo("2.244E-15");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2a*(a+b) : 20",
        "2a*2b : 24",
        "22(3+1) : 88",
        "(1+2)(2-1) : 3",
        "0xA(a+b) : 50",
        "(a+b)(a-b) : -5"
      })
  void testImplicitMultiplication(String expressionString, String expectedResult)
      throws EvaluationException, ParseException {
    Expression expression =
        new Expression(
                expressionString,
                ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).build())
            .with("a", 2)
            .and("b", 3);
    assertThat(expression.evaluate().getStringValue()).isEqualTo(expectedResult);
  }

  private BigDecimal evaluateToNumber(String expression)
      throws EvaluationException, ParseException {

    // Given an expression with EvalEx2 compatible math context
    return new Expression(
            expression,
            ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).build())
        .evaluate()
        .getNumberValue();
  }
}
