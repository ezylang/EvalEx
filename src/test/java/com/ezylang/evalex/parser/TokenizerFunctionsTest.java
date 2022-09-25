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

import com.ezylang.evalex.config.TestConfigurationProvider.DummyFunction;
import com.ezylang.evalex.parser.Token.TokenType;
import org.junit.jupiter.api.Test;

class TokenizerFunctionsTest extends BaseParserTest {

  @Test
  void testSimple() throws ParseException {
    configuration.getFunctionDictionary().addFunction("f", new DummyFunction());
    assertAllTokensParsedCorrectly(
        "f(x)",
        new Token(1, "f", TokenType.FUNCTION),
        new Token(2, "(", TokenType.BRACE_OPEN),
        new Token(3, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(4, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testBlanks() throws ParseException {
    configuration.getFunctionDictionary().addFunction("f", new DummyFunction());
    assertAllTokensParsedCorrectly(
        "f (x)",
        new Token(1, "f", TokenType.FUNCTION),
        new Token(3, "(", TokenType.BRACE_OPEN),
        new Token(4, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(5, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testUnderscores() throws ParseException {
    configuration.getFunctionDictionary().addFunction("_f_x_", new DummyFunction());
    assertAllTokensParsedCorrectly(
        "_f_x_(x)",
        new Token(1, "_f_x_", TokenType.FUNCTION),
        new Token(6, "(", TokenType.BRACE_OPEN),
        new Token(7, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(8, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testWithNumbers() throws ParseException {
    configuration.getFunctionDictionary().addFunction("f1x2", new DummyFunction());
    assertAllTokensParsedCorrectly(
        "f1x2(x)",
        new Token(1, "f1x2", TokenType.FUNCTION),
        new Token(5, "(", TokenType.BRACE_OPEN),
        new Token(6, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(7, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testWithMoreParameters() throws ParseException {
    assertAllTokensParsedCorrectly(
        "SUM(a, 2, 3)",
        new Token(1, "SUM", TokenType.FUNCTION),
        new Token(4, "(", TokenType.BRACE_OPEN),
        new Token(5, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(6, ",", TokenType.COMMA),
        new Token(8, "2", TokenType.NUMBER_LITERAL),
        new Token(9, ",", TokenType.COMMA),
        new Token(11, "3", TokenType.NUMBER_LITERAL),
        new Token(12, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testWithMixedParameters() throws ParseException {
    assertAllTokensParsedCorrectly(
        "TEST(a, \"hello\", 3)",
        new Token(1, "TEST", TokenType.FUNCTION),
        new Token(5, "(", TokenType.BRACE_OPEN),
        new Token(6, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(7, ",", TokenType.COMMA),
        new Token(9, "hello", TokenType.STRING_LITERAL),
        new Token(16, ",", TokenType.COMMA),
        new Token(18, "3", TokenType.NUMBER_LITERAL),
        new Token(19, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testFunctionInParameter() throws ParseException {
    assertAllTokensParsedCorrectly(
        "TEST(a, FACT(x), 3)",
        new Token(1, "TEST", TokenType.FUNCTION),
        new Token(5, "(", TokenType.BRACE_OPEN),
        new Token(6, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(7, ",", TokenType.COMMA),
        new Token(9, "FACT", TokenType.FUNCTION),
        new Token(13, "(", TokenType.BRACE_OPEN),
        new Token(14, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(15, ")", TokenType.BRACE_CLOSE),
        new Token(16, ",", TokenType.COMMA),
        new Token(18, "3", TokenType.NUMBER_LITERAL),
        new Token(19, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testFunctionInParameterInFunctionParameter() throws ParseException {
    assertAllTokensParsedCorrectly(
        "SUM(a,FACT(MIN(x,y)),3)",
        new Token(1, "SUM", TokenType.FUNCTION),
        new Token(4, "(", TokenType.BRACE_OPEN),
        new Token(5, "a", TokenType.VARIABLE_OR_CONSTANT),
        new Token(6, ",", TokenType.COMMA),
        new Token(7, "FACT", TokenType.FUNCTION),
        new Token(11, "(", TokenType.BRACE_OPEN),
        new Token(12, "MIN", TokenType.FUNCTION),
        new Token(15, "(", TokenType.BRACE_OPEN),
        new Token(16, "x", TokenType.VARIABLE_OR_CONSTANT),
        new Token(17, ",", TokenType.COMMA),
        new Token(18, "y", TokenType.VARIABLE_OR_CONSTANT),
        new Token(19, ")", TokenType.BRACE_CLOSE),
        new Token(20, ")", TokenType.BRACE_CLOSE),
        new Token(21, ",", TokenType.COMMA),
        new Token(22, "3", TokenType.NUMBER_LITERAL),
        new Token(23, ")", TokenType.BRACE_CLOSE));
  }

  @Test
  void testUndefinedFunction() {
    assertThatThrownBy(() -> new Tokenizer("a(b)", configuration).parse())
        .isEqualTo(new ParseException(1, 2, "a", "Undefined function 'a'"));
  }
}
