/*
  Copyright 2012-2026 Udo Klimaschewski

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

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.config.TestConfigurationProvider;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.EvaluationValue.DataType;
import com.ezylang.evalex.parser.ParseException;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for the structure operator combined with the lenient mode.
 *
 * @author oswaldo.bapvic.jr
 */
class ExpressionEvaluatorStructureLenientTest {

  final ExpressionConfiguration configuration =
      TestConfigurationProvider.StandardConfigurationWithAdditionalTestOperatorsLenient;

  String evaluate(String expressionString) throws ParseException, EvaluationException {
    Expression expression = createExpression(expressionString);
    return expression.evaluate().getStringValue();
  }

  Expression createExpression(String expressionString) {
    return new Expression(expressionString, configuration);
  }

  @Test
  void testStructureTwoLevelsWithAllVariablesDefined() throws EvaluationException, ParseException {
    Expression expression = createExpression("root.child").with("root", Map.of("child", "text"));
    EvaluationValue evaluationValue = expression.evaluate();
    assertThat(evaluationValue.getStringValue()).isEqualTo("text");
  }

  @Test
  void testStructureTwoLevelsWithRootVariableNotDefined()
      throws EvaluationException, ParseException {
    Expression expression = createExpression("root.child");
    EvaluationValue evaluationValue = expression.evaluate();
    assertThat(evaluationValue.getDataType()).isEqualTo(DataType.UNDEFINED);
  }

  @Test
  void testStructureTwoLevelsWithRootVariableNull() throws EvaluationException, ParseException {
    Expression expression = createExpression("root.child").with("root", null);
    EvaluationValue evaluationValue = expression.evaluate();
    assertThat(evaluationValue.getDataType()).isEqualTo(DataType.UNDEFINED);
  }

  @Test
  void testStructureTwoLevelsWithChildVariableNotDefined()
      throws EvaluationException, ParseException {
    Expression expression = createExpression("root.child").with("root", emptyMap());
    EvaluationValue evaluationValue = expression.evaluate();
    assertThat(evaluationValue.getDataType()).isEqualTo(DataType.UNDEFINED);
  }

  @Test
  void testStructureThreoLevelsWithBrachVariableNotDefined()
      throws EvaluationException, ParseException {
    Expression expression =
        createExpression("root.branch.child").with("root", Map.of("branch", emptyMap()));
    EvaluationValue evaluationValue = expression.evaluate();
    assertThat(evaluationValue.getDataType()).isEqualTo(DataType.UNDEFINED);
  }
}
