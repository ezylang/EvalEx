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

import static com.ezylang.evalex.parser.Token.TokenType.ARRAY_INDEX;
import static com.ezylang.evalex.parser.Token.TokenType.ARRAY_OPEN;
import static com.ezylang.evalex.parser.Token.TokenType.BRACE_OPEN;
import static com.ezylang.evalex.parser.Token.TokenType.FUNCTION;
import static com.ezylang.evalex.parser.Token.TokenType.STRUCTURE_SEPARATOR;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.parser.Token.TokenType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The shunting yard algorithm can be used to convert a mathematical expression from an infix
 * notation into either a postfix notation (RPN, reverse polish notation), or into an abstract
 * syntax tree (AST).
 *
 * <p>Here it is used to parse and convert a list of already parsed expression tokens into an AST.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Shunting_yard_algorithm">Shunting yard algorithm</a>
 * @see <a href="https://en.wikipedia.org/wiki/Abstract_syntax_tree">Abstract syntax tree</a>
 */
public class ShuntingYardConverter {

  private final List<Token> expressionTokens;

  private final String originalExpression;

  private final ExpressionConfiguration configuration;

  private final Deque<Token> operatorStack = new ArrayDeque<>();
  private final Deque<ASTNode> operandStack = new ArrayDeque<>();

  public ShuntingYardConverter(
      String originalExpression,
      List<Token> expressionTokens,
      ExpressionConfiguration configuration) {
    this.originalExpression = originalExpression;
    this.expressionTokens = expressionTokens;
    this.configuration = configuration;
  }

  public ASTNode toAbstractSyntaxTree() throws ParseException {

    Token previousToken = null;
    for (Token currentToken : expressionTokens) {
      switch (currentToken.getType()) {
        case VARIABLE_OR_CONSTANT:
        case NUMBER_LITERAL:
        case STRING_LITERAL:
          operandStack.push(new ASTNode(currentToken));
          break;
        case FUNCTION:
          operatorStack.push(currentToken);
          break;
        case COMMA:
          processOperatorsFromStackUntilTokenType(BRACE_OPEN);
          break;
        case INFIX_OPERATOR:
        case PREFIX_OPERATOR:
        case POSTFIX_OPERATOR:
          processOperator(currentToken);
          break;
        case BRACE_OPEN:
          processBraceOpen(previousToken, currentToken);
          break;
        case BRACE_CLOSE:
          processBraceClose();
          break;
        case ARRAY_OPEN:
          processArrayOpen(currentToken);
          break;
        case ARRAY_CLOSE:
          processArrayClose();
          break;
        case STRUCTURE_SEPARATOR:
          processStructureSeparator(currentToken);
          break;
        default:
          throw new ParseException(
              currentToken, "Unexpected token of type '" + currentToken.getType() + "'");
      }
      previousToken = currentToken;
    }

    while (!operatorStack.isEmpty()) {
      Token token = operatorStack.pop();
      createOperatorNode(token);
    }

    if (operandStack.isEmpty()) {
      throw new ParseException(this.originalExpression, "Empty expression");
    }

    return operandStack.pop();
  }

  private void processStructureSeparator(Token currentToken) throws ParseException {
    Token nextToken = operatorStack.isEmpty() ? null : operatorStack.peek();
    while (nextToken != null && nextToken.getType() == STRUCTURE_SEPARATOR) {
      Token token = operatorStack.pop();
      createOperatorNode(token);
      nextToken = operatorStack.peek();
    }
    operatorStack.push(currentToken);
  }

  private void processBraceOpen(Token previousToken, Token currentToken) {
    if (previousToken != null && previousToken.getType() == FUNCTION) {
      // start of parameter list, marker for variable number of arguments
      Token paramStart =
          new Token(
              currentToken.getStartPosition(),
              currentToken.getValue(),
              TokenType.FUNCTION_PARAM_START);
      operandStack.push(new ASTNode(paramStart));
    }
    operatorStack.push(currentToken);
  }

  private void processBraceClose() throws ParseException {
    processOperatorsFromStackUntilTokenType(BRACE_OPEN);
    operatorStack.pop(); // throw away the marker
    if (!operatorStack.isEmpty() && operatorStack.peek().getType() == FUNCTION) {
      Token functionToken = operatorStack.pop();
      ArrayList<ASTNode> parameters = new ArrayList<>();
      while (true) {
        // add all parameters in reverse order from stack to the parameter array
        ASTNode node = operandStack.pop();
        if (node.getToken().getType() == TokenType.FUNCTION_PARAM_START) {
          break;
        }
        parameters.add(0, node);
      }
      validateFunctionParameters(functionToken, parameters);
      operandStack.push(new ASTNode(functionToken, parameters.toArray(new ASTNode[0])));
    }
  }

  private void validateFunctionParameters(Token functionToken, ArrayList<ASTNode> parameters)
      throws ParseException {
    FunctionIfc function = functionToken.getFunctionDefinition();
    if (parameters.size() < function.getFunctionParameterDefinitions().size()) {
      throw new ParseException(functionToken, "Not enough parameters for function");
    }
    if (!function.hasVarArgs()
        && parameters.size() > function.getFunctionParameterDefinitions().size()) {
      throw new ParseException(functionToken, "Too many parameters for function");
    }
  }

  /**
   * Array index is treated like a function with two parameters. First parameter is the array (name
   * or evaluation result). Second parameter is the array index.
   *
   * @param currentToken The current ARRAY_OPEN ("[") token.
   */
  private void processArrayOpen(Token currentToken) throws ParseException {
    Token nextToken = operatorStack.isEmpty() ? null : operatorStack.peek();
    while (nextToken != null && (nextToken.getType() == STRUCTURE_SEPARATOR)) {
      Token token = operatorStack.pop();
      createOperatorNode(token);
      nextToken = operatorStack.isEmpty() ? null : operatorStack.peek();
    }
    // create ARRAY_INDEX operator (just like a function name) and push it to the operator stack
    Token arrayIndex =
        new Token(currentToken.getStartPosition(), currentToken.getValue(), ARRAY_INDEX);
    operatorStack.push(arrayIndex);

    // push the ARRAY_OPEN to the operators, too (to later match the ARRAY_CLOSE)
    operatorStack.push(currentToken);
  }

  /**
   * Follows the logic for a function, but with two fixed parameters.
   *
   * @throws ParseException If there were problems while processing the stacks.
   */
  private void processArrayClose() throws ParseException {
    processOperatorsFromStackUntilTokenType(ARRAY_OPEN);
    operatorStack.pop(); // throw away the marker
    Token arrayToken = operatorStack.pop();
    ArrayList<ASTNode> operands = new ArrayList<>();

    // second parameter of the "ARRAY_INDEX" function is the index (first on stack)
    ASTNode index = operandStack.pop();
    operands.add(0, index);

    // first parameter of the "ARRAY_INDEX" function is the array (name or evaluation result)
    // (second on stack)
    ASTNode array = operandStack.pop();
    operands.add(0, array);

    operandStack.push(new ASTNode(arrayToken, operands.toArray(new ASTNode[0])));
  }

  private void processOperatorsFromStackUntilTokenType(TokenType untilTokenType)
      throws ParseException {
    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != untilTokenType) {
      Token token = operatorStack.pop();
      createOperatorNode(token);
    }
  }

  private void createOperatorNode(Token token) throws ParseException {
    if (operandStack.isEmpty()) {
      throw new ParseException(token, "Missing operand for operator");
    }

    ASTNode operand1 = operandStack.pop();

    if (token.getType() == TokenType.PREFIX_OPERATOR
        || token.getType() == TokenType.POSTFIX_OPERATOR) {
      operandStack.push(new ASTNode(token, operand1));
    } else {
      if (operandStack.isEmpty()) {
        throw new ParseException(token, "Missing second operand for operator");
      }
      ASTNode operand2 = operandStack.pop();
      operandStack.push(new ASTNode(token, operand2, operand1));
    }
  }

  private void processOperator(Token currentToken) throws ParseException {
    Token nextToken = operatorStack.isEmpty() ? null : operatorStack.peek();
    while (isOperator(nextToken)
        && isNextOperatorOfHigherPrecedence(
            currentToken.getOperatorDefinition(), nextToken.getOperatorDefinition())) {
      Token token = operatorStack.pop();
      createOperatorNode(token);
      nextToken = operatorStack.isEmpty() ? null : operatorStack.peek();
    }
    operatorStack.push(currentToken);
  }

  private boolean isNextOperatorOfHigherPrecedence(
      OperatorIfc currentOperator, OperatorIfc nextOperator) {
    // structure operator (null) has always a higher precedence than other operators
    if (nextOperator == null) {
      return true;
    }

    if (currentOperator.isLeftAssociative()) {
      return currentOperator.getPrecedence(configuration)
          <= nextOperator.getPrecedence(configuration);
    } else {
      return currentOperator.getPrecedence(configuration)
          < nextOperator.getPrecedence(configuration);
    }
  }

  private boolean isOperator(Token token) {
    if (token == null) {
      return false;
    }
    TokenType tokenType = token.getType();
    switch (tokenType) {
      case INFIX_OPERATOR:
      case PREFIX_OPERATOR:
      case POSTFIX_OPERATOR:
      case STRUCTURE_SEPARATOR:
        return true;
      default:
        return false;
    }
  }
}
