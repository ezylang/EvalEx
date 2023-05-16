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
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;

public abstract class BaseEvaluationTest {

  protected void assertExpressionHasExpectedResult(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertThat(
            evaluate(
                    expression,
                    TestConfigurationProvider.StandardConfigurationWithAdditionalTestOperators)
                .getStringValue())
        .isEqualTo(expectedResult);
  }

  protected void assertExpressionHasExpectedResult(
      String expression, String expectedResult, ExpressionConfiguration expressionConfiguration)
      throws EvaluationException, ParseException {
    assertThat(evaluate(expression, expressionConfiguration).getStringValue())
        .isEqualTo(expectedResult);
  }

  protected void assertExpressionThrows(
      String expression, String message, ExpressionConfiguration expressionConfiguration)
      throws EvaluationException, ParseException {
    try {
      evaluate(expression, expressionConfiguration);
    } catch (Exception ex) {
      assertThat(ex.getMessage()).isEqualTo(message);
    }
  }

  private EvaluationValue evaluate(String expressionString, ExpressionConfiguration configuration)
      throws EvaluationException, ParseException {
    Expression expression = new Expression(expressionString, configuration);

    return expression.evaluate();
  }
}
