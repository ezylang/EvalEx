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

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.Token.TokenType;
import org.junit.jupiter.api.Test;

class TokenTest {

  final ExpressionConfiguration expressionConfiguration =
      ExpressionConfiguration.defaultConfiguration();

  @Test
  void testTokenCreation() {
    int counter = 0;
    for (TokenType type : TokenType.values()) {
      counter++;
      String tokenString =
          type == TokenType.NUMBER_LITERAL ? Integer.toString(counter) : "token" + counter;
      Token token = new Token(counter, tokenString, type);

      assertThat(token.getType()).isEqualTo(type);
      assertThat(token.getStartPosition()).isEqualTo(counter);
      assertThat(token.getValue()).isEqualTo(tokenString);
      assertThat(token.getFunctionDefinition()).isNull();
      assertThat(token.getOperatorDefinition()).isNull();
    }
  }

  @Test
  void testFunctionToken() {
    Token token =
        new Token(
            3,
            "MAX",
            TokenType.FUNCTION,
            expressionConfiguration.getFunctionDictionary().getFunction("MAX"));

    assertThat(token.getStartPosition()).isEqualTo(3);
    assertThat(token.getValue()).isEqualTo("MAX");
    assertThat(token.getType()).isEqualTo(TokenType.FUNCTION);
    assertThat(token.getFunctionDefinition()).isNotNull();
    assertThat(token.getOperatorDefinition()).isNull();
  }

  @Test
  void testOperatorToken() {
    Token token =
        new Token(
            1,
            "+",
            TokenType.INFIX_OPERATOR,
            expressionConfiguration.getOperatorDictionary().getInfixOperator("+"));

    assertThat(token.getStartPosition()).isEqualTo(1);
    assertThat(token.getValue()).isEqualTo("+");
    assertThat(token.getType()).isEqualTo(TokenType.INFIX_OPERATOR);
    assertThat(token.getFunctionDefinition()).isNull();
    assertThat(token.getOperatorDefinition()).isNotNull();
  }
}
