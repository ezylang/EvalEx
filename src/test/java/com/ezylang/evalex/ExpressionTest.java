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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionTest {

  @Test
  void testExpressionDefaults() {
    Expression expression = new Expression("a+b");

    assertThat(expression.getExpressionString()).isEqualTo("a+b");
    assertThat(expression.getConfiguration().getMathContext())
        .isEqualTo(ExpressionConfiguration.DEFAULT_MATH_CONTEXT);
    assertThat(expression.getConfiguration().getFunctionDictionary().hasFunction("SUM")).isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasInfixOperator("+"))
        .isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasPrefixOperator("+"))
        .isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasPostfixOperator("+"))
        .isFalse();
  }

  @Test
  void testValidateOK() throws ParseException {
    new Expression("1+1").validate();
  }

  @Test
  void testValidateFail() {
    assertThatThrownBy(() -> new Expression("2#3").validate())
        .isInstanceOf(ParseException.class)
        .hasMessage("Undefined operator '#'");
  }

  @Test
  void testExpressionNode() throws ParseException, EvaluationException {
    Expression expression = new Expression("a*b");
    ASTNode subExpression = expression.createExpressionNode("4+3");

    EvaluationValue result = expression.with("a", 2).and("b", subExpression).evaluate();

    assertThat(result.getStringValue()).isEqualTo("14");
  }

  @Test
  void testWithValues() throws ParseException, EvaluationException {
    Expression expression = new Expression("(a + b) * (a - b)");

    Map<String, Object> values = new HashMap<>();
    values.put("a", 3.5);
    values.put("b", 2.5);

    EvaluationValue result = expression.withValues(values).evaluate();

    assertThat(result.getStringValue()).isEqualTo("6");
  }

  @Test
  void testDefaultExpressionOwnsOwnConfigurationEntries() {
    Expression expression1 = new Expression("1+1");
    Expression expression2 = new Expression("1+1");

    assertThat(expression1.getDataAccessor()).isNotSameAs(expression2.getDataAccessor());
    assertThat(expression1.getConfiguration().getOperatorDictionary())
        .isNotSameAs(expression2.getConfiguration().getOperatorDictionary());
    assertThat(expression1.getConfiguration().getFunctionDictionary())
        .isNotSameAs(expression2.getConfiguration().getFunctionDictionary());
    assertThat(expression1.getConfiguration().getDefaultConstants())
        .isNotSameAs(expression2.getConfiguration().getDefaultConstants());
  }

  @Test
  void testDoubleConverterDefaultMathContext() {
    Expression defaultMathContextExpression = new Expression("1");
    assertThat(defaultMathContextExpression.convertDoubleValue(1.67987654321).getNumberValue())
        .isEqualByComparingTo("1.67987654321");
  }

  @Test
  void testDoubleConverterLimitedMathContext() {
    Expression limitedMathContextExpression =
        new Expression(
            "1", ExpressionConfiguration.builder().mathContext(new MathContext(3)).build());
    assertThat(limitedMathContextExpression.convertDoubleValue(1.6789).getNumberValue())
        .isEqualByComparingTo("1.68");
  }

  @Test
  void testGetAllASTNodes() throws ParseException {
    Expression expression = new Expression("1+2/3");
    List<ASTNode> nodes = expression.getAllASTNodes();
    assertThat(nodes.get(0).getToken().getValue()).isEqualTo("+");
    assertThat(nodes.get(1).getToken().getValue()).isEqualTo("1");
    assertThat(nodes.get(2).getToken().getValue()).isEqualTo("/");
    assertThat(nodes.get(3).getToken().getValue()).isEqualTo("2");
    assertThat(nodes.get(4).getToken().getValue()).isEqualTo("3");
  }

  @Test
  void testGetUsedVariables() throws ParseException {
    Expression expression = new Expression("a/2*PI+MIN(e,b)");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("a", "b");
  }

  @Test
  void testGetUsedVariablesLongNames() throws ParseException {
    Expression expression = new Expression("var1/2*PI+MIN(var2,var3)");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("var1", "var2", "var3");
  }

  @Test
  void testGetUsedVariablesNoVariables() throws ParseException {
    Expression expression = new Expression("1/2");
    assertThat(expression.getUsedVariables()).isEmpty();
  }

  @Test
  void testGetUsedVariablesCaseSensitivity() throws ParseException {
    Expression expression = new Expression("a+B*b-A/PI*(1/2)*pi+e-E+a");
    assertThat(expression.getUsedVariables()).containsExactlyInAnyOrder("a", "b");
  }
}
