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

import static com.ezylang.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_MULTIPLICATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.PostfixOperator;
import com.ezylang.evalex.operators.arithmetic.InfixModuloOperator;
import com.ezylang.evalex.parser.Token.TokenType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenizerAmbiguousOperatorTest extends BaseParserTest {

  @BeforeEach
  void setup() {
    configuration =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalOperators(
                Map.entry("%", new PostfixPercentOperator()),
                Map.entry("PERCENT", new InfixModuloOperator()),
                Map.entry("PERCENT", new PostfixPercentOperator()));
  }

  @Test
  void testSymbolicOperatorIsInfixBeforeNumber() throws ParseException {
    assertAllTokensParsedCorrectly(
        "99 % 100",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "%", TokenType.INFIX_OPERATOR),
        new Token(6, "100", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testSymbolicOperatorIsPostfixAtEndOfExpression() throws ParseException {
    assertAllTokensParsedCorrectly(
        "99%",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(3, "%", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testSymbolicOperatorIsInfixBeforePrefixOperator() throws ParseException {
    assertAllTokensParsedCorrectly(
        "99 % -100",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "%", TokenType.INFIX_OPERATOR),
        new Token(6, "-", TokenType.PREFIX_OPERATOR),
        new Token(7, "100", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testGroupedSymbolicOperatorIsPostfix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "(99%) - 100",
        new Token(1, "(", TokenType.BRACE_OPEN),
        new Token(2, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "%", TokenType.POSTFIX_OPERATOR),
        new Token(5, ")", TokenType.BRACE_CLOSE),
        new Token(7, "-", TokenType.INFIX_OPERATOR),
        new Token(9, "100", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testSymbolicOperatorIsPostfixBeforeInfixOperator() throws ParseException {
    assertAllTokensParsedCorrectly(
        "99 % * 100",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "%", TokenType.POSTFIX_OPERATOR),
        new Token(6, "*", TokenType.INFIX_OPERATOR),
        new Token(8, "100", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testLiteralOperatorUsesSameResolution() throws ParseException {
    assertAllTokensParsedCorrectly(
        "99 PERCENT 100",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "PERCENT", TokenType.INFIX_OPERATOR),
        new Token(12, "100", TokenType.NUMBER_LITERAL));

    assertAllTokensParsedCorrectly(
        "99 PERCENT",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "PERCENT", TokenType.POSTFIX_OPERATOR));

    assertAllTokensParsedCorrectly(
        "99 PERCENT -100",
        new Token(1, "99", TokenType.NUMBER_LITERAL),
        new Token(4, "PERCENT", TokenType.INFIX_OPERATOR),
        new Token(12, "-", TokenType.PREFIX_OPERATOR),
        new Token(13, "100", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testInvalidRightOperandDoesNotPreventPostfixResolution() {
    assertThatThrownBy(new Tokenizer("99 % [", configuration)::parse)
        .isInstanceOf(ParseException.class)
        .hasMessage("Array open not allowed here");
  }

  @Test
  void testResolvedOperatorsCanBeEvaluated() throws EvaluationException, ParseException {
    assertThat(new Expression("99 % 100", configuration).evaluate().getNumberValue())
        .isEqualByComparingTo("99");
    assertThat(new Expression("99%", configuration).evaluate().getNumberValue())
        .isEqualByComparingTo("0.99");
    assertThat(new Expression("99%-100", configuration).evaluate().getNumberValue())
        .isEqualByComparingTo("99");
    assertThat(new Expression("(99%)-1", configuration).evaluate().getNumberValue())
        .isEqualByComparingTo("-0.01");
  }

  @PostfixOperator(precedence = OPERATOR_PRECEDENCE_MULTIPLICATIVE - 1)
  static class PostfixPercentOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands) {
      return EvaluationValue.numberValue(operands[0].getNumberValue().movePointLeft(2));
    }
  }
}
