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

import static com.ezylang.evalex.parser.Token.TokenType.*;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.config.FunctionDictionaryIfc;
import com.ezylang.evalex.config.OperatorDictionaryIfc;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.parser.Token.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * The tokenizer is responsible to parse a string and return a list of tokens. The order of tokens
 * will follow the infix expression notation, skipping any blank characters.
 */
public class Tokenizer {

  private final String expressionString;

  private final OperatorDictionaryIfc operatorDictionary;

  private final FunctionDictionaryIfc functionDictionary;

  private final ExpressionConfiguration configuration;

  private final List<Token> tokens = new ArrayList<>();

  private int currentColumnIndex = 0;

  private int currentChar = -2;

  private int braceBalance;

  private int arrayBalance;

  public Tokenizer(String expressionString, ExpressionConfiguration configuration) {
    this.expressionString = expressionString;
    this.configuration = configuration;
    this.operatorDictionary = configuration.getOperatorDictionary();
    this.functionDictionary = configuration.getFunctionDictionary();
  }

  /**
   * Parse the given expression and return a list of tokens, representing the expression.
   *
   * @return A list of expression tokens.
   * @throws ParseException When the expression can't be parsed.
   */
  public List<Token> parse() throws ParseException {
    Token currentToken = getNextToken();
    while (currentToken != null) {
      if (implicitMultiplicationPossible(currentToken)) {
        if (configuration.isImplicitMultiplicationAllowed()) {
          Token multiplication =
              new Token(
                  currentToken.getStartPosition(),
                  "*",
                  TokenType.INFIX_OPERATOR,
                  operatorDictionary.getInfixOperator("*"));
          tokens.add(multiplication);
        } else {
          throw new ParseException(currentToken, "Missing operator");
        }
      }
      validateToken(currentToken);
      tokens.add(currentToken);
      currentToken = getNextToken();
    }

    if (braceBalance > 0) {
      throw new ParseException(expressionString, "Closing brace not found");
    }

    if (arrayBalance > 0) {
      throw new ParseException(expressionString, "Closing array not found");
    }

    return tokens;
  }

  private boolean implicitMultiplicationPossible(Token currentToken) {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return false;
    }

    return ((previousToken.getType() == BRACE_CLOSE && currentToken.getType() == BRACE_OPEN)
        || (previousToken.getType() == NUMBER_LITERAL
            && currentToken.getType() == VARIABLE_OR_CONSTANT)
        || (previousToken.getType() == NUMBER_LITERAL && currentToken.getType() == BRACE_OPEN));
  }

  private void validateToken(Token currentToken) throws ParseException {

    if (currentToken.getType() == STRUCTURE_SEPARATOR && getPreviousToken() == null) {
      throw new ParseException(currentToken, "Misplaced structure operator");
    }

    Token previousToken = getPreviousToken();
    if (previousToken != null
        && previousToken.getType() == INFIX_OPERATOR
        && invalidTokenAfterInfixOperator(currentToken)) {
      throw new ParseException(currentToken, "Unexpected token after infix operator");
    }
  }

  private boolean invalidTokenAfterInfixOperator(Token token) {
    switch (token.getType()) {
      case INFIX_OPERATOR:
      case BRACE_CLOSE:
      case COMMA:
        return true;
      default:
        return false;
    }
  }

  private Token getNextToken() throws ParseException {

    // blanks are always skipped.
    skipBlanks();

    // end of input
    if (currentChar == -1) {
      return null;
    }

    // we have a token start, identify and parse it
    if (isAtStringLiteralStart()) {
      return parseStringLiteral();
    } else if (currentChar == '(') {
      return parseBraceOpen();
    } else if (currentChar == ')') {
      return parseBraceClose();
    } else if (currentChar == '[' && configuration.isArraysAllowed()) {
      return parseArrayOpen();
    } else if (currentChar == ']' && configuration.isArraysAllowed()) {
      return parseArrayClose();
    } else if (currentChar == '.'
        && !isNextCharNumberChar()
        && configuration.isStructuresAllowed()) {
      return parseStructureSeparator();
    } else if (currentChar == ',') {
      Token token = new Token(currentColumnIndex, ",", TokenType.COMMA);
      consumeChar();
      return token;
    } else if (isAtIdentifierStart()) {
      return parseIdentifier();
    } else if (isAtNumberStart()) {
      return parseNumberLiteral();
    } else {
      return parseOperator();
    }
  }

  private Token parseStructureSeparator() throws ParseException {
    Token token = new Token(currentColumnIndex, ".", TokenType.STRUCTURE_SEPARATOR);
    if (arrayOpenOrStructureSeparatorNotAllowed()) {
      throw new ParseException(token, "Structure separator not allowed here");
    }
    consumeChar();
    return token;
  }

  private Token parseArrayClose() throws ParseException {
    Token token = new Token(currentColumnIndex, "]", TokenType.ARRAY_CLOSE);
    if (!arrayCloseAllowed()) {
      throw new ParseException(token, "Array close not allowed here");
    }
    consumeChar();
    arrayBalance--;
    if (arrayBalance < 0) {
      throw new ParseException(token, "Unexpected closing array");
    }
    return token;
  }

  private Token parseArrayOpen() throws ParseException {
    Token token = new Token(currentColumnIndex, "[", TokenType.ARRAY_OPEN);
    if (arrayOpenOrStructureSeparatorNotAllowed()) {
      throw new ParseException(token, "Array open not allowed here");
    }
    consumeChar();
    arrayBalance++;
    return token;
  }

  private Token parseBraceClose() throws ParseException {
    Token token = new Token(currentColumnIndex, ")", TokenType.BRACE_CLOSE);
    consumeChar();
    braceBalance--;
    if (braceBalance < 0) {
      throw new ParseException(token, "Unexpected closing brace");
    }
    return token;
  }

  private Token parseBraceOpen() {
    Token token = new Token(currentColumnIndex, "(", BRACE_OPEN);
    consumeChar();
    braceBalance++;
    return token;
  }

  private Token getPreviousToken() {
    return tokens.isEmpty() ? null : tokens.get(tokens.size() - 1);
  }

  private Token parseOperator() throws ParseException {
    int tokenStartIndex = currentColumnIndex;
    StringBuilder tokenValue = new StringBuilder();
    while (true) {
      tokenValue.append((char) currentChar);
      String tokenString = tokenValue.toString();
      String possibleNextOperator = tokenString + (char) peekNextChar();
      boolean possibleNextOperatorFound =
          (prefixOperatorAllowed() && operatorDictionary.hasPrefixOperator(possibleNextOperator))
              || (postfixOperatorAllowed()
                  && operatorDictionary.hasPostfixOperator(possibleNextOperator))
              || (infixOperatorAllowed()
                  && operatorDictionary.hasInfixOperator(possibleNextOperator));
      consumeChar();
      if (!possibleNextOperatorFound) {
        break;
      }
    }
    String tokenString = tokenValue.toString();
    if (prefixOperatorAllowed() && operatorDictionary.hasPrefixOperator(tokenString)) {
      OperatorIfc operator = operatorDictionary.getPrefixOperator(tokenString);
      return new Token(tokenStartIndex, tokenString, TokenType.PREFIX_OPERATOR, operator);
    } else if (postfixOperatorAllowed() && operatorDictionary.hasPostfixOperator(tokenString)) {
      OperatorIfc operator = operatorDictionary.getPostfixOperator(tokenString);
      return new Token(tokenStartIndex, tokenString, TokenType.POSTFIX_OPERATOR, operator);
    } else if (operatorDictionary.hasInfixOperator(tokenString)) {
      OperatorIfc operator = operatorDictionary.getInfixOperator(tokenString);
      return new Token(tokenStartIndex, tokenString, TokenType.INFIX_OPERATOR, operator);
    } else if (tokenString.equals(".") && configuration.isStructuresAllowed()) {
      return new Token(tokenStartIndex, tokenString, STRUCTURE_SEPARATOR);
    }
    throw new ParseException(
        tokenStartIndex,
        tokenStartIndex + tokenString.length() - 1,
        tokenString,
        "Undefined operator '" + tokenString + "'");
  }

  private boolean arrayOpenOrStructureSeparatorNotAllowed() {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return true;
    }

    switch (previousToken.getType()) {
      case BRACE_CLOSE:
      case VARIABLE_OR_CONSTANT:
      case ARRAY_CLOSE:
      case STRING_LITERAL:
        return false;
      default:
        return true;
    }
  }

  private boolean arrayCloseAllowed() {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return false;
    }

    switch (previousToken.getType()) {
      case BRACE_OPEN:
      case INFIX_OPERATOR:
      case PREFIX_OPERATOR:
      case FUNCTION:
      case COMMA:
      case ARRAY_OPEN:
        return false;
      default:
        return true;
    }
  }

  private boolean prefixOperatorAllowed() {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return true;
    }

    switch (previousToken.getType()) {
      case BRACE_OPEN:
      case INFIX_OPERATOR:
      case COMMA:
      case PREFIX_OPERATOR:
        return true;
      default:
        return false;
    }
  }

  private boolean postfixOperatorAllowed() {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return false;
    }

    switch (previousToken.getType()) {
      case BRACE_CLOSE:
      case NUMBER_LITERAL:
      case VARIABLE_OR_CONSTANT:
      case STRING_LITERAL:
        return true;
      default:
        return false;
    }
  }

  private boolean infixOperatorAllowed() {
    Token previousToken = getPreviousToken();

    if (previousToken == null) {
      return false;
    }

    switch (previousToken.getType()) {
      case BRACE_CLOSE:
      case VARIABLE_OR_CONSTANT:
      case STRING_LITERAL:
      case POSTFIX_OPERATOR:
      case NUMBER_LITERAL:
      case ARRAY_CLOSE:
        return true;
      default:
        return false;
    }
  }

  private Token parseNumberLiteral() throws ParseException {
    int nextChar = peekNextChar();
    if (currentChar == '0' && (nextChar == 'x' || nextChar == 'X')) {
      return parseHexNumberLiteral();
    } else {
      return parseDecimalNumberLiteral();
    }
  }

  private Token parseDecimalNumberLiteral() throws ParseException {
    int tokenStartIndex = currentColumnIndex;
    StringBuilder tokenValue = new StringBuilder();

    int lastChar = -1;
    boolean scientificNotation = false;
    boolean dotEncountered = false;
    while (currentChar != -1 && isAtNumberChar()) {
      if (currentChar == '.' && dotEncountered) {
        tokenValue.append((char) currentChar);
        throw new ParseException(
            new Token(tokenStartIndex, tokenValue.toString(), TokenType.NUMBER_LITERAL),
            "Number contains more than one decimal point");
      }
      if (currentChar == '.') {
        dotEncountered = true;
      }
      if (currentChar == 'e' || currentChar == 'E') {
        scientificNotation = true;
      }
      tokenValue.append((char) currentChar);
      lastChar = currentChar;
      consumeChar();
    }
    // illegal scientific format literal
    if (scientificNotation
        && (lastChar == 'e'
            || lastChar == 'E'
            || lastChar == '+'
            || lastChar == '-'
            || lastChar == '.')) {
      throw new ParseException(
          new Token(tokenStartIndex, tokenValue.toString(), TokenType.NUMBER_LITERAL),
          "Illegal scientific format");
    }
    return new Token(tokenStartIndex, tokenValue.toString(), TokenType.NUMBER_LITERAL);
  }

  private Token parseHexNumberLiteral() {
    int tokenStartIndex = currentColumnIndex;
    StringBuilder tokenValue = new StringBuilder();

    // hexadecimal number, consume "0x"
    tokenValue.append((char) currentChar);
    consumeChar();
    do {
      tokenValue.append((char) currentChar);
      consumeChar();
    } while (currentChar != -1 && isAtHexChar());
    return new Token(tokenStartIndex, tokenValue.toString(), TokenType.NUMBER_LITERAL);
  }

  private Token parseIdentifier() throws ParseException {
    int tokenStartIndex = currentColumnIndex;
    StringBuilder tokenValue = new StringBuilder();
    while (currentChar != -1 && isAtIdentifierChar()) {
      tokenValue.append((char) currentChar);
      consumeChar();
    }
    String tokenName = tokenValue.toString();

    if (prefixOperatorAllowed() && operatorDictionary.hasPrefixOperator(tokenName)) {
      return new Token(
          tokenStartIndex,
          tokenName,
          TokenType.PREFIX_OPERATOR,
          operatorDictionary.getPrefixOperator(tokenName));
    } else if (postfixOperatorAllowed() && operatorDictionary.hasPostfixOperator(tokenName)) {
      return new Token(
          tokenStartIndex,
          tokenName,
          TokenType.POSTFIX_OPERATOR,
          operatorDictionary.getPostfixOperator(tokenName));
    } else if (operatorDictionary.hasInfixOperator(tokenName)) {
      return new Token(
          tokenStartIndex,
          tokenName,
          TokenType.INFIX_OPERATOR,
          operatorDictionary.getInfixOperator(tokenName));
    }

    skipBlanks();
    if (currentChar == '(') {
      if (!functionDictionary.hasFunction(tokenName)) {
        throw new ParseException(
            tokenStartIndex,
            currentColumnIndex,
            tokenName,
            "Undefined function '" + tokenName + "'");
      }
      FunctionIfc function = functionDictionary.getFunction(tokenName);
      return new Token(tokenStartIndex, tokenName, TokenType.FUNCTION, function);
    } else {
      return new Token(tokenStartIndex, tokenName, TokenType.VARIABLE_OR_CONSTANT);
    }
  }

  Token parseStringLiteral() throws ParseException {
    int startChar = currentChar;
    int tokenStartIndex = currentColumnIndex;
    StringBuilder tokenValue = new StringBuilder();
    // skip starting quote
    consumeChar();
    boolean inQuote = true;
    while (inQuote && currentChar != -1) {
      if (currentChar == '\\') {
        consumeChar();
        tokenValue.append(escapeCharacter(currentChar));
      } else if (currentChar == startChar) {
        inQuote = false;
      } else {
        tokenValue.append((char) currentChar);
      }
      consumeChar();
    }
    if (inQuote) {
      throw new ParseException(
          tokenStartIndex, currentColumnIndex, tokenValue.toString(), "Closing quote not found");
    }
    return new Token(tokenStartIndex, tokenValue.toString(), TokenType.STRING_LITERAL);
  }

  private char escapeCharacter(int character) throws ParseException {
    switch (character) {
      case '\'':
        return '\'';
      case '"':
        return '"';
      case '\\':
        return '\\';
      case 'n':
        return '\n';
      case 'r':
        return '\r';
      case 't':
        return '\t';
      case 'b':
        return '\b';
      case 'f':
        return '\f';
      default:
        throw new ParseException(
            currentColumnIndex, 1, "\\" + (char) character, "Unknown escape character");
    }
  }

  private boolean isAtNumberStart() {
    if (Character.isDigit(currentChar)) {
      return true;
    }
    return currentChar == '.' && Character.isDigit(peekNextChar());
  }

  private boolean isAtNumberChar() {
    int previousChar = peekPreviousChar();

    if ((previousChar == 'e' || previousChar == 'E') && currentChar != '.') {
      return Character.isDigit(currentChar) || currentChar == '+' || currentChar == '-';
    }

    if (previousChar == '.' && currentChar != '.') {
      return Character.isDigit(currentChar) || currentChar == 'e' || currentChar == 'E';
    }

    return Character.isDigit(currentChar)
        || currentChar == '.'
        || currentChar == 'e'
        || currentChar == 'E';
  }

  private boolean isNextCharNumberChar() {
    if (peekNextChar() == -1) {
      return false;
    }
    consumeChar();
    boolean isAtNumber = isAtNumberChar();
    currentColumnIndex--;
    currentChar = expressionString.charAt(currentColumnIndex - 1);
    return isAtNumber;
  }

  private boolean isAtHexChar() {
    switch (currentChar) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
        return true;
      default:
        return false;
    }
  }

  private boolean isAtIdentifierStart() {
    return Character.isLetter(currentChar) || currentChar == '_';
  }

  private boolean isAtIdentifierChar() {
    return Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_';
  }

  private boolean isAtStringLiteralStart() {
    return currentChar == '"'
        || currentChar == '\'' && configuration.isSingleQuoteStringLiteralsAllowed();
  }

  private void skipBlanks() {
    if (currentChar == -2) {
      // consume first character of expression
      consumeChar();
    }
    while (currentChar != -1 && Character.isWhitespace(currentChar)) {
      consumeChar();
    }
  }

  private int peekNextChar() {
    return currentColumnIndex == expressionString.length()
        ? -1
        : expressionString.charAt(currentColumnIndex);
  }

  private int peekPreviousChar() {
    return currentColumnIndex == 1 ? -1 : expressionString.charAt(currentColumnIndex - 2);
  }

  private void consumeChar() {
    if (currentColumnIndex == expressionString.length()) {
      currentChar = -1;
    } else {
      currentChar = expressionString.charAt(currentColumnIndex++);
    }
  }
}
