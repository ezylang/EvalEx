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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.Token.TokenType;
import org.junit.jupiter.api.Test;

class TokenizerImplicitMultiplicationTest extends BaseParserTest {

  @Test
  void testImplicitBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "(a+b)(a-b)",
        new Token(1, "(", TokenType.BRACE_OPEN),
        new Token(2, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(3, "+", TokenType.INFIX_OPERATOR),
        new Token(4, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(5, ")", TokenType.BRACE_CLOSE),
        new Token(6, "*", TokenType.INFIX_OPERATOR),
        new Token(6, "(", TokenType.BRACE_OPEN),
        new Token(7, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(8, "-", TokenType.INFIX_OPERATOR),
        new Token(9, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(10, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testImplicitNumberBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2(x)",
        new Token(1, "2", TokenType.NUMBER_LITERAL),
        new Token(2, "*", TokenType.INFIX_OPERATOR),
        new Token(2, "(", TokenType.BRACE_OPEN),
        new Token(3, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testImplicitNumberNoBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2x",
        new Token(1, "2", TokenType.NUMBER_LITERAL),
        new Token(2, "*", TokenType.INFIX_OPERATOR),
        new Token(2, "x", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testImplicitNumberVariable() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2x",
        new Token(1, "2", TokenType.NUMBER_LITERAL),
        new Token(2, "*", TokenType.INFIX_OPERATOR),
        new Token(2, "x", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testImplicitNumberFunction() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2F(x)",
        new Token(1, "2", TokenType.NUMBER_LITERAL),
        new Token(2, "*", TokenType.INFIX_OPERATOR),
        new Token(2, "F", TokenType.FUNCTION),
        new Token(3, "(", TokenType.BRACE_OPEN),
        new Token(4, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(5, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testImplicitMultiplicationNotAllowed() {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().implicitMultiplicationAllowed(false).build();

    assertThatThrownBy(() -> new Tokenizer("2(x+y)", config).parse())
        .isEqualTo(new ParseException(2, 2, "(", "Missing operator"));
  }
}
