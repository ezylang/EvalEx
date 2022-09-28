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

import static com.ezylang.evalex.parser.Token.TokenType.NUMBER_LITERAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TokenizerNumberLiteralTest extends BaseParserTest {

  @Test
  void testSingleDigit() throws ParseException {
    assertAllTokensParsedCorrectly("7", new Token(1, "7", NUMBER_LITERAL));
  }

  @Test
  void testMultipleDigit() throws ParseException {
    assertAllTokensParsedCorrectly("888", new Token(1, "888", NUMBER_LITERAL));
  }

  @Test
  void testBlanks() throws ParseException {
    assertAllTokensParsedCorrectly("\t 123 \r\n", new Token(3, "123", NUMBER_LITERAL));
  }

  @Test
  void testDecimal() throws ParseException {
    assertAllTokensParsedCorrectly("123.834", new Token(1, "123.834", NUMBER_LITERAL));
  }

  @Test
  void testDecimalStart() throws ParseException {
    assertAllTokensParsedCorrectly(".9", new Token(1, ".9", NUMBER_LITERAL));
  }

  @Test
  void testDecimalEnd() throws ParseException {
    assertAllTokensParsedCorrectly("123.", new Token(1, "123.", NUMBER_LITERAL));
  }

  @Test
  void testHexNumberSimple() throws ParseException {
    assertAllTokensParsedCorrectly("0x0", new Token(1, "0x0", NUMBER_LITERAL));
  }

  @Test
  void testHexNumberLong() throws ParseException {
    assertAllTokensParsedCorrectly("0x3ABCDEF0", new Token(1, "0x3ABCDEF0", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly(
        " \t0x3abcdefAbcdef09873EE ", new Token(3, "0x3abcdefAbcdef09873EE", NUMBER_LITERAL));
  }

  @Test
  void testHexNumberBlank() throws ParseException {
    assertAllTokensParsedCorrectly(
        " \t0x3abcdefAbcdef09873EE ", new Token(3, "0x3abcdefAbcdef09873EE", NUMBER_LITERAL));
  }

  @Test
  void testSciOK() throws ParseException {
    assertAllTokensParsedCorrectly("2e1", new Token(1, "2e1", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly("2E1", new Token(1, "2E1", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly("2e-1", new Token(1, "2e-1", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly("2E-1", new Token(1, "2E-1", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly("2e+1", new Token(1, "2e+1", NUMBER_LITERAL));
    assertAllTokensParsedCorrectly("2E+1", new Token(1, "2E+1", NUMBER_LITERAL));
  }

  @ParameterizedTest
  @ValueSource(strings = {"2e", "2E", "2e+", "2E+", "2e-", "2E-", "2e."})
  void testScientificLiteralsParseException(String expression) {
    assertThatThrownBy(() -> new Tokenizer(expression, configuration).parse())
        .isInstanceOf(ParseException.class)
        .hasMessage("Illegal scientific format");
  }
}
