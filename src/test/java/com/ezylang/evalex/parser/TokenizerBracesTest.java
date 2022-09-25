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

import com.ezylang.evalex.parser.Token.TokenType;
import org.junit.jupiter.api.Test;

class TokenizerBracesTest extends BaseParserTest {

  @Test
  void testBracesSimple() throws ParseException {
    assertAllTokensParsedCorrectly(
        "(a + b)",
        new Token(1, "(", TokenType.BRACE_OPEN),
        new Token(2, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, "+", TokenType.INFIX_OPERATOR),
        new Token(6, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(7, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testBracesNested() throws ParseException {

    assertAllTokensParsedCorrectly(
        "(a+(b+c))",
        new Token(1, "(", TokenType.BRACE_OPEN),
        new Token(2, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(3, "+", TokenType.INFIX_OPERATOR),
        new Token(4, "(", TokenType.BRACE_OPEN),
        new Token(5, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(6, "+", TokenType.INFIX_OPERATOR),
        new Token(7, "c", TokenType.VARIABLE_OR_CONSTANT),
        new Token(8, ")", TokenType.BRACE_CLOSE),
        new Token(9, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testMissingClosingBrace() {
    assertThatThrownBy(() -> new Tokenizer("(2+4", configuration).parse())
        .isEqualTo(new ParseException(1, 4, "(2+4", "Closing brace not found"));
  }

  @Test
  void testUnexpectedClosingBrace() {
    assertThatThrownBy(() -> new Tokenizer("(2+4))", configuration).parse())
        .isEqualTo(new ParseException(6, 6, ")", "Unexpected closing brace"));
  }
}
