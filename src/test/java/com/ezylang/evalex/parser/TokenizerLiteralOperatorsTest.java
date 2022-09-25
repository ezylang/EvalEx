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
package com.ezylang.evalex.parser;

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_AND;
import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_OR;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.InfixOperator;
import com.ezylang.evalex.operators.PostfixOperator;
import com.ezylang.evalex.operators.PrefixOperator;
import com.ezylang.evalex.parser.Token.TokenType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenizerLiteralOperatorsTest extends BaseParserTest {

  @BeforeEach
  public void setup() {
    configuration =
        configuration.withAdditionalOperators(
            Map.entry("AND", new AndOperator()),
            Map.entry("OR", new OrOperator()),
            Map.entry("NOT", new NotOperator()),
            Map.entry("DENIED", new DeniedOperator()));
  }

  @Test
  void testAndOrNot() throws ParseException {
    assertAllTokensParsedCorrectly(
        "NOT a AND b DENIED OR NOT(c)",
        new Token(1, "NOT", TokenType.PREFIX_OPERATOR),
        new Token(5, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(7, "AND", TokenType.INFIX_OPERATOR),
        new Token(11, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(13, "DENIED", TokenType.POSTFIX_OPERATOR),
        new Token(20, "OR", TokenType.INFIX_OPERATOR),
        new Token(23, "NOT", TokenType.PREFIX_OPERATOR),
        new Token(26, "(", TokenType.BRACE_OPEN),
        new Token(27, "c", TokenType.VARIABLE_OR_CONSTANT),
        new Token(28, ")", TokenType.BRACE_CLOSE));
  }

  @InfixOperator(precedence = OPERATOR_PRECEDENCE_AND)
  static class AndOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return new EvaluationValue(operands[0].getBooleanValue() && operands[1].getBooleanValue());
    }
  }

  @InfixOperator(precedence = OPERATOR_PRECEDENCE_OR)
  static class OrOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return new EvaluationValue(operands[0].getBooleanValue() || operands[1].getBooleanValue());
    }
  }

  @PrefixOperator(leftAssociative = false)
  static class NotOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return new EvaluationValue(!operands[0].getBooleanValue());
    }
  }

  @PostfixOperator()
  static class DeniedOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return new EvaluationValue(!operands[0].getBooleanValue());
    }
  }
}
