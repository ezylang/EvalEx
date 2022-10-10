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
package com.ezylang.evalex.functions.trigonometric;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class TrigonometricFunctionsTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "DEG(0) : 0",
        "DEG(1) : 57.29577951308232",
        "DEG(90) : 5156.620156177409",
        "DEG(-90) : -5156.620156177409"
      })
  void testDeg(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "RAD(0) : 0",
        "RAD(1) : 0.017453292519943295",
        "RAD(45) : 0.7853981633974483",
        "RAD(50) : 0.8726646259971648",
        "RAD(90) : 1.5707963267948966",
        "RAD(-90) : -1.5707963267948966"
      })
  void testRad(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"SIN(0) : 0", "SIN(1) : 0.01745240643728351", "SIN(90) : 1", "SIN(-90) : -1"})
  void testSin(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "TAN(0) : 0",
        "TAN(1) : 0.017455064928217585",
        "TAN(19) : 0.34432761328966527",
        "TAN(-19) : -0.34432761328966527"
      })
  void testTan(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SINR(0) : 0",
        "SINR(1) : 0.8414709848078965",
        "SINR(90) : 0.8939966636005579",
        "SINR(-90) : -0.8939966636005579"
      })
  void testSinR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COS(0) : 1",
        "COS(1) : 0.9998476951563913",
        "COS(19) : 0.9455185755993168",
        "COS(-19) : 0.9455185755993168"
      })
  void testCos(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COSH(0) : 1",
        "COSH(1) : 1.543080634815244",
        "COSH(-1) : 1.543080634815244",
      })
  void testCosH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COSR(0) : 1",
        "COSR(1) : 0.5403023058681398",
        "COSR(19) : 0.9887046181866692",
        "COSR(-19) : 0.9887046181866692"
      })
  void testCosR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "TANR(0) : 0",
        "TANR(1) : 1.5574077246549023",
        "TANR(19) : 0.15158947061240008",
        "TANR(-19) : -0.15158947061240008"
      })
  void testTanR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COT(1) : 57.28996163075943",
        "COT(19) : 2.9042108776758226",
        "COT(-19) : -2.9042108776758226"
      })
  void testCoTan(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCoTanThrowsException() {
    assertThatThrownBy(() -> new Expression("COT(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COTH(1) : 1.3130352854993315",
        "COTH(5) : 1.0000908039820193",
        "COTH(-5) : -1.0000908039820193"
      })
  void testCotH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCotHThrowsException() {
    assertThatThrownBy(() -> new Expression("COTH(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COTR(1) : 0.6420926159343306",
        "COTR(19) : 6.596764247280111",
        "COTR(-19) : -6.596764247280111"
      })
  void testCoTanR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCoTanRThrowsException() {
    assertThatThrownBy(() -> new Expression("COTR(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SEC(1) : 1.0001523280439077",
        "SEC(19) : 1.0576206811866706",
        "SEC(-19) : 1.0576206811866706"
      })
  void testSec(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testSecThrowsException() {
    assertThatThrownBy(() -> new Expression("SEC(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SECH(1) : 0.6480542736638853",
        "SECH(19) : 0.000000011205592875074534",
        "SECH(-19) : 0.000000011205592875074534"
      })
  void testSecH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testSecHThrowsException() {
    assertThatThrownBy(() -> new Expression("SECH(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SECR(1) : 1.8508157176809255",
        "SECR(19) : 1.01142442505634",
        "SECR(-19) : 1.01142442505634"
      })
  void testSecR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testSecRThrowsException() {
    assertThatThrownBy(() -> new Expression("SECR(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "CSC(1) : 57.298688498550185",
        "CSC(19) : 3.0715534867572423",
        "CSC(-19) : -3.0715534867572423"
      })
  void testCSC(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCscThrowsException() {
    assertThatThrownBy(() -> new Expression("CSC(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "CSCH(1) : 0.8509181282393216",
        "CSCH(19) : 0.000000011205592875074534",
        "CSCH(-19) : -0.000000011205592875074534"
      })
  void testCSCH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCscHThrowsException() {
    assertThatThrownBy(() -> new Expression("CSCH(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "CSCR(1) : 1.1883951057781212",
        "CSCR(19) : 6.672128486037505",
        "CSCR(-19) : -6.672128486037505"
      })
  void testCSCR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testCscRThrowsException() {
    assertThatThrownBy(() -> new Expression("CSCR(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ACOS(0) : 90",
        "ACOS(1) : 0",
        "ACOS(-1) : 180",
      })
  void testAcos(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ACOSH(1) : 0",
        "ACOSH(2) : 1.3169578969248166",
        "ACOSH(3) : 1.762747174039086",
      })
  void testAcosH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-1, -0.5, 0, 0.5, 0.9})
  void testAcosHThrowsException(double d) {
    assertThatThrownBy(() -> new Expression("ACOSH(x)").with("x", d).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Value must be greater or equal to one");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ACOSR(0) : 1.5707963267948966",
        "ACOSR(1) : 0",
        "ACOSR(-1) : 3.141592653589793",
      })
  void testAcosR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ACOT(1) : 45",
        "ACOT(-1) : 135",
      })
  void testAcot(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testAcotThrowsException() {
    assertThatThrownBy(() -> new Expression("ACOT(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"ACOTH(-1.5) : -0.8047189562170501", "ACOTH(1.5) : 0.8047189562170501"})
  void testAcotH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ACOTR(1) : 0.7853981633974483",
        "ACOTR(-1) : -0.7853981633974483",
      })
  void testAcotR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testAcotRThrowsException() {
    assertThatThrownBy(() -> new Expression("ACOTR(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter must not be zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ASIN(0) : 0",
        "ASIN(1) : 90",
        "ASIN(-1) : -90",
      })
  void testAsin(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ASINH(0) : 0",
        "ASINH(1) : 0.8813735870195429",
        "ASINH(-1) : -0.8813735870195428",
      })
  void testAsinH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ASINR(0) : 0",
        "ASINR(1) : 1.5707963267948966",
        "ASINR(-1) : -1.5707963267948966",
      })
  void testAsinR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ATAN(0) : 0",
        "ATAN(1) : 45",
        "ATAN(-1) : -45",
      })
  void testAtan(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ATANH(0) : 0",
        "ATANH(0.9) : 1.4722194895832204",
        "ATANH(-0.9) : -1.4722194895832204",
      })
  void testAtanH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-1.1, -1.0, 1.0, 1.1})
  void testAtanHThrowsException(double d) {
    assertThatThrownBy(() -> new Expression("ATANH(x)").with("x", d).evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Absolute value must be less than 1");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ATANR(0) : 0",
        "ATANR(1) : 0.7853981633974483",
        "ATANR(-1) : -0.7853981633974483",
      })
  void testAtanR(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ATAN2(0,0) : 0",
        "ATAN2(0,1) : 0",
        "ATAN2(0,-1) : 180",
        "ATAN2(1,0) : 90",
        "ATAN2(1,1) : 45",
        "ATAN2(1,-1) : 135",
        "ATAN2(-1,0) : -90",
        "ATAN2(-1,1) : -45",
        "ATAN2(-1,-1) : -135",
      })
  void testAtan2(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ATAN2R(0,0) : 0",
        "ATAN2R(0,1) : 0",
        "ATAN2R(0,-1) : 3.141592653589793",
        "ATAN2R(1,0) : 1.5707963267948966",
        "ATAN2R(1,1) : 0.7853981633974483",
        "ATAN2R(1,-1) : 2.356194490192345",
        "ATAN2R(-1,0) : -1.5707963267948966",
        "ATAN2R(-1,1) : -0.7853981633974483",
        "ATAN2R(-1,-1) : -2.356194490192345",
      })
  void testAtan2R(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SINH(0) : 0",
        "SINH(1) : 1.1752011936438014",
        "SINH(-1) : -1.1752011936438014",
      })
  void testSinH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "TANH(0) : 0",
        "TANH(1) : 0.7615941559557649",
        "TANH(-1) : -0.7615941559557649",
      })
  void testTanH(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
