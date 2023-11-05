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
package com.ezylang.evalex.config;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.PostfixOperator;
import com.ezylang.evalex.operators.PrefixOperator;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Map;

public class TestConfigurationProvider {

  public static final ExpressionConfiguration StandardConfigurationWithAdditionalTestOperators =
      ExpressionConfiguration.builder()
          .zoneId(ZoneId.of("Europe/Berlin"))
          .build()
          .withAdditionalOperators(
              Map.entry("++", new PrefixPlusPlusOperator()),
              Map.entry("++", new PostfixPlusPlusOperator()),
              Map.entry("?", new PostfixQuestionOperator()))
          .withAdditionalFunctions(Map.entry("TEST", new DummyFunction()));

  @FunctionParameter(name = "input", isVarArg = true)
  public static class DummyFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token functionToken, EvaluationValue... parameterValues) {
      // dummy implementation
      return expression.convertValue("OK");
    }
  }

  @PrefixOperator(leftAssociative = false)
  public static class PrefixPlusPlusOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      EvaluationValue operand = operands[0];
      return EvaluationValue.numberValue(operand.getNumberValue().add(BigDecimal.ONE));
    }
  }

  @PostfixOperator()
  public static class PostfixPlusPlusOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      EvaluationValue operand = operands[0];
      return EvaluationValue.numberValue(operand.getNumberValue().add(BigDecimal.ONE));
    }
  }

  @PostfixOperator(leftAssociative = false)
  public static class PostfixQuestionOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      // dummy implementation
      return EvaluationValue.stringValue("?");
    }
  }
}
