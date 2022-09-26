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

class TokenizerPrefixPostfixTest extends BaseParserTest {

  @Test
  void testPrefixSingle() throws ParseException {
    assertAllTokensParsedCorrectly(
        "++a",
        new Token(1, "++", TokenType.PREFIX_OPERATOR),
        new Token(3, "a", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testPostfixSingle() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a++",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "++", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testPostfixAsPrefixThrowsException() {
    assertThatThrownBy(new Tokenizer("?a", configuration)::parse)
        .isInstanceOf(ParseException.class)
        .hasMessage("Undefined operator '?'");
  }

  @Test
  void testPrefixAndPostfix() throws ParseException {
    // note: if this is supported, depends on the operator and what type it expects as operand
    assertAllTokensParsedCorrectly(
        "++a++",
        new Token(1, "++", TokenType.PREFIX_OPERATOR),
        new Token(3, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, "++", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testPrefixWithInfixAndPostfix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "++a+a++",
        new Token(1, "++", TokenType.PREFIX_OPERATOR),
        new Token(3, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, "+", TokenType.INFIX_OPERATOR),
        new Token(5, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(6, "++", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testPrefixWithBraces() throws ParseException {
    assertAllTokensParsedCorrectly(
        "(++a)+(a++)",
        new Token(1, "(", TokenType.BRACE_OPEN),
        new Token(2, "++", TokenType.PREFIX_OPERATOR),
        new Token(4, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(5, ")", TokenType.BRACE_CLOSE),
        new Token(6, "+", TokenType.INFIX_OPERATOR),
        new Token(7, "(", TokenType.BRACE_OPEN),
        new Token(8, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(9, "++", TokenType.POSTFIX_OPERATOR),
        new Token(11, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testPrefixWithFunction() throws ParseException {
    assertAllTokensParsedCorrectly(
        "++MAX(++a,a++,b++)++",
        new Token(1, "++", TokenType.PREFIX_OPERATOR),
        new Token(3, "MAX", TokenType.FUNCTION),
        new Token(6, "(", TokenType.BRACE_OPEN),
        new Token(7, "++", TokenType.PREFIX_OPERATOR),
        new Token(9, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(10, ",", TokenType.COMMA),
        new Token(11, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(12, "++", TokenType.POSTFIX_OPERATOR),
        new Token(14, ",", TokenType.COMMA),
        new Token(15, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(16, "++", TokenType.POSTFIX_OPERATOR),
        new Token(18, ")", TokenType.BRACE_CLOSE),
        new Token(19, "++", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testPrefixWithStringLiteral() throws ParseException {
    assertAllTokensParsedCorrectly(
        "++\"hello\"++",
        new Token(1, "++", TokenType.PREFIX_OPERATOR),
        new Token(3, "hello", TokenType.STRING_LITERAL),
        new Token(10, "++", TokenType.POSTFIX_OPERATOR));
  }

  @Test
  void testPrefixWithUnaryAndBinary() throws ParseException {
    assertAllTokensParsedCorrectly(
        "-a - -b++",
        new Token(1, "-", TokenType.PREFIX_OPERATOR),
        new Token(2, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, "-", TokenType.INFIX_OPERATOR),
        new Token(6, "-", TokenType.PREFIX_OPERATOR),
        new Token(7, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(8, "++", TokenType.POSTFIX_OPERATOR));
  }
}
