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

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.Token.TokenType;
import org.junit.jupiter.api.Test;

class TokenizerStringLiteralTest extends BaseParserTest {

  @Test
  void testSimpleQuote() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"Hello, World\"", new Token(1, "Hello, World", TokenType.STRING_LITERAL));
  }

  @Test
  void testSimpleQuoteLeadingBlanks() throws ParseException {
    assertAllTokensParsedCorrectly(
        "  \t\n \"Hello, World\"", new Token(6, "Hello, World", TokenType.STRING_LITERAL));
  }

  @Test
  void testSimpleQuoteTrailingBlanks() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"Hello, World\"  \t\n ", new Token(1, "Hello, World", TokenType.STRING_LITERAL));
  }

  @Test
  void testEscapeDoubleQuote() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"Hello, \\\"World\\\"\"", new Token(1, "Hello, \"World\"", TokenType.STRING_LITERAL));
  }

  @Test
  void testEscapeSingleQuote() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"Hello, \\'World\\'\"", new Token(1, "Hello, 'World'", TokenType.STRING_LITERAL));
  }

  @Test
  void testEscapeBackslash() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"a \\\\ b\"", new Token(1, "a \\ b", TokenType.STRING_LITERAL));
  }

  @Test
  void testEscapeCharacters() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\" \\t \\r \\n \\f \\b \"", new Token(1, " \t \r \n \f \b ", TokenType.STRING_LITERAL));
  }

  @Test
  void testUnknownEscapeCharacter() {
    assertThatThrownBy(() -> new Expression("\" \\y \"").evaluate())
        .isInstanceOf(ParseException.class)
        .hasMessage("Unknown escape character");
  }

  @Test
  void testSimpleQuoteOperation() throws ParseException {
    assertAllTokensParsedCorrectly(
        "\"Hello\" + \" \" + \"World\"",
        new Token(1, "Hello", TokenType.STRING_LITERAL),
        new Token(9, "+", TokenType.INFIX_OPERATOR),
        new Token(11, " ", TokenType.STRING_LITERAL),
        new Token(15, "+", TokenType.INFIX_OPERATOR),
        new Token(17, "World", TokenType.STRING_LITERAL));
  }

  @Test
  void testErrorUnmatchedQuoteStart() {
    assertThatThrownBy(() -> new Tokenizer("\"hello", configuration).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Closing quote not found");
  }

  @Test
  void testErrorUnmatchedQuoteOffset() {
    assertThatThrownBy(() -> new Tokenizer("test \"hello", configuration).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Closing quote not found");
  }

  @Test
  void testSingleQuoteAllowed() {
    assertThatThrownBy(() -> new Tokenizer("'hello'", configuration).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Undefined operator '''");
  }

  @Test
  void testSingleQuoteOperation() throws ParseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertAllTokensParsedCorrectly(
        "'\"Hello\", ' + \"'World'\"",
        config,
        new Token(1, "\"Hello\", ", TokenType.STRING_LITERAL),
        new Token(13, "+", TokenType.INFIX_OPERATOR),
        new Token(15, "'World'", TokenType.STRING_LITERAL));
  }

  @Test
  void testErrorUnmatchedSingleQuoteStart() {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertThatThrownBy(() -> new Tokenizer("'hello", config).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Closing quote not found");
  }

  @Test
  void testErrorUnmatchedSingleQuoteOffset() {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertThatThrownBy(() -> new Tokenizer("test 'hello", config).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Closing quote not found");
  }

  @Test
  void testErrorUnmatchedDelimiters() {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertThatThrownBy(() -> new Tokenizer("'test\"", config).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Closing quote not found");
  }

  @Test
  void testEscapeSingleQuoteCharacter() throws ParseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder().singleQuoteStringLiteralsAllowed(true).build();

    assertAllTokensParsedCorrectly(
        "' \\' \\' \\' '", config, new Token(1, " ' ' ' ", TokenType.STRING_LITERAL));
  }
}
