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

class TokenizerArrayTest extends BaseParserTest {

  @Test
  void testArraySimple() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1]",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE));
  }

  @Test
  void testArrayNested() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[b[2] + c[3]]",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "b", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, "[", TokenType.ARRAY_OPEN),
        new Token(5, "2", TokenType.NUMBER_LITERAL),
        new Token(6, "]", TokenType.ARRAY_CLOSE),
        new Token(8, "+", TokenType.INFIX_OPERATOR),
        new Token(10, "c", TokenType.VARIABLE_OR_CONSTANT),
        new Token(11, "[", TokenType.ARRAY_OPEN),
        new Token(12, "3", TokenType.NUMBER_LITERAL),
        new Token(13, "]", TokenType.ARRAY_CLOSE),
        new Token(14, "]", TokenType.ARRAY_CLOSE));
  }

  @Test
  void testArrayEquals() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] = 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, "=", TokenType.INFIX_OPERATOR),
        new Token(8, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayGreaterThan() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] > 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, ">", TokenType.INFIX_OPERATOR),
        new Token(8, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayLessThan() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] < 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, "<", TokenType.INFIX_OPERATOR),
        new Token(8, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayNotEquals() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] != 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, "!=", TokenType.INFIX_OPERATOR),
        new Token(9, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayEqualsEquals() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] == 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, "==", TokenType.INFIX_OPERATOR),
        new Token(9, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayGreaterEqualsThan() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] >= 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, ">=", TokenType.INFIX_OPERATOR),
        new Token(9, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testArrayLessEqualsThan() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a[1] <= 2",
        new Token(1, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(2, "[", TokenType.ARRAY_OPEN),
        new Token(3, "1", TokenType.NUMBER_LITERAL),
        new Token(4, "]", TokenType.ARRAY_CLOSE),
        new Token(6, "<=", TokenType.INFIX_OPERATOR),
        new Token(9, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testMissingClosingArray() {
    assertThatThrownBy(() -> new Tokenizer("a[2+4", configuration).parse())
        .isEqualTo(new ParseException(1, 5, "a[2+4", "Closing array not found"));
  }

  @Test
  void testUnexpectedClosingArray() {
    assertThatThrownBy(() -> new Tokenizer("a[2+4]]", configuration).parse())
        .isEqualTo(new ParseException(7, 7, "]", "Unexpected closing array"));
  }

  @Test
  void testOpenArrayNotAllowedBeginning() {
    assertThatThrownBy(() -> new Tokenizer("[1]", configuration).parse())
        .isEqualTo(new ParseException(1, 1, "[", "Array open not allowed here"));
  }

  @Test
  void testOpenArrayNotAllowedAfterOperator() {
    assertThatThrownBy(() -> new Tokenizer("1+[1]", configuration).parse())
        .isEqualTo(new ParseException(3, 3, "[", "Array open not allowed here"));
  }

  @Test
  void testOpenArrayNotAllowedAfterBrace() {
    assertThatThrownBy(() -> new Tokenizer("([1]", configuration).parse())
        .isEqualTo(new ParseException(2, 2, "[", "Array open not allowed here"));
  }

  @Test
  void testCloseArrayNotAllowedBeginning() {
    assertThatThrownBy(() -> new Tokenizer("]", configuration).parse())
        .isEqualTo(new ParseException(1, 1, "]", "Array close not allowed here"));
  }

  @Test
  void testCloseArrayNotAllowedAfterBrace() {
    assertThatThrownBy(() -> new Tokenizer("(]", configuration).parse())
        .isEqualTo(new ParseException(2, 2, "]", "Array close not allowed here"));
  }

  @Test
  void testArraysNotAllowedOpen() {
    ExpressionConfiguration config = ExpressionConfiguration.builder().arraysAllowed(false).build();

    assertThatThrownBy(() -> new Tokenizer("a[0]", config).parse())
        .isEqualTo(new ParseException(2, 2, "[", "Undefined operator '['"));
  }

  @Test
  void testArraysNotAllowedClose() {
    ExpressionConfiguration config = ExpressionConfiguration.builder().arraysAllowed(false).build();

    assertThatThrownBy(() -> new Tokenizer("]", config).parse())
        .isEqualTo(new ParseException(1, 1, "]", "Undefined operator ']'"));
  }
}
