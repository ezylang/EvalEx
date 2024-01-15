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
package com.ezylang.evalex.operators.booleans;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InfixNotEqualsOperatorTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1!=1 : false",
        "1<>1 : false",
        "0!=0 : false",
        "1!=0 : true",
        "0!=1 : true",
        "21.678!=21.678 : false",
        "\"abc\"!=\"abc\" : false",
        "\"abc\"!=\"xyz\" : true",
        "1+2!=4-1 : false",
        "-5.2!=-5.2 :false",
        "DT_DATE_NEW(2022,10,30)!=DT_DATE_NEW(2022,10,30) : false",
        "DT_DATE_NEW(2022,10,30)!=DT_DATE_NEW(2022,10,28) : true",
        "DT_DURATION_PARSE(\"P2D\")!=DT_DURATION_PARSE(\"PT24H\") : true"
      })
  void testInfixNotEqualsLiterals(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1!=\"1\" : true",
        "\"1\"!=1 : true",
        "true!=\"1\" : true",
        "\"1\"!=true : true",
        "false!=\"1\" : true",
        "\"1\"!=false : true",
        "DT_DATE_NEW(2022,10,30)!=1 : true",
        "1!=DT_DATE_NEW(2022,10,30) : true",
        "DT_DURATION_PARSE(\"PT24H\")!=1 : true",
        "1!=DT_DURATION_PARSE(\"PT24H\") : true",
      })
  void testInfixNotEqualsTypesDiffer(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testInfixNotEqualsVariables() throws EvaluationException, ParseException {
    Expression expression = new Expression("a!=b");

    assertThat(
            expression
                .with("a", new BigDecimal("1.4"))
                .and("b", new BigDecimal("1.4"))
                .evaluate()
                .getBooleanValue())
        .isFalse();

    assertThat(expression.with("a", "Hello").and("b", "Hello").evaluate().getBooleanValue())
        .isFalse();

    assertThat(expression.with("a", "Hello").and("b", "Goodbye").evaluate().getBooleanValue())
        .isTrue();

    assertThat(expression.with("a", true).and("b", true).evaluate().getBooleanValue()).isFalse();

    assertThat(expression.with("a", false).and("b", true).evaluate().getBooleanValue()).isTrue();
  }

  @Test
  void testInfixNotEqualsArrays() throws EvaluationException, ParseException {
    Expression expression = new Expression("a!=b");

    assertThat(
            expression
                .with("a", Arrays.asList("a", "b", "c"))
                .and("b", Arrays.asList("a", "b", "c"))
                .evaluate()
                .getBooleanValue())
        .isFalse();

    assertThat(
            expression
                .with("a", Arrays.asList("a", "b", "c"))
                .and("b", Arrays.asList("c", "b", "a"))
                .evaluate()
                .getBooleanValue())
        .isTrue();
  }

  @Test
  void testInfixNotEqualsStructures() throws EvaluationException, ParseException {
    Expression expression = new Expression("a!=b");

    Map<String, BigDecimal> structure1 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(35));
            put("b", new BigDecimal(99));
          }
        };

    Map<String, BigDecimal> structure2 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(35));
            put("b", new BigDecimal(99));
          }
        };

    Map<String, BigDecimal> structure3 =
        new HashMap<>() {
          {
            put("a", new BigDecimal(45));
            put("b", new BigDecimal(99));
          }
        };

    assertThat(expression.with("a", structure1).and("b", structure2).evaluate().getBooleanValue())
        .isFalse();

    assertThat(expression.with("a", structure1).and("b", structure3).evaluate().getBooleanValue())
        .isTrue();
  }
}
