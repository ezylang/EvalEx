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
package com.ezylang.evalex;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.DataAccessorIfc;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.parser.*;
import java.math.BigDecimal;
import java.util.*;
import lombok.Getter;

/**
 * Main class that allow creating, parsing, passing parameters and evaluating an expression string.
 *
 * @see <a href="https://github.com/ezylang/EvalEx">EvalEx Homepage</a>
 */
public class Expression {
  @Getter private final ExpressionConfiguration configuration;

  @Getter private final String expressionString;

  @Getter private final DataAccessorIfc dataAccessor;

  @Getter
  private final Map<String, EvaluationValue> constants =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private ASTNode abstractSyntaxTree;

  /**
   * Creates a new expression with the default configuration. The expression is not parsed until it
   * is first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString) {
    this(expressionString, ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Creates a new expression with a custom configuration. The expression is not parsed until it is
   * first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString, ExpressionConfiguration configuration) {
    this.expressionString = expressionString;
    this.configuration = configuration;
    this.dataAccessor = configuration.getDataAccessorSupplier().get();
    this.constants.putAll(configuration.getDefaultConstants());
  }

  /**
   * Creates a copy with the same expression string, configuration and syntax tree from an existing
   * expression. The existing expression will be parsed to populate the syntax tree.
   *
   * @param expression An existing expression.
   * @throws ParseException If there were problems while parsing the existing expression.
   */
  public Expression(Expression expression) throws ParseException {
    this(expression.getExpressionString(), expression.getConfiguration());
    this.abstractSyntaxTree = expression.getAbstractSyntaxTree();
  }

  /**
   * Evaluates the expression by parsing it (if not done before) and the evaluating it.
   *
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public EvaluationValue evaluate() throws EvaluationException, ParseException {
    EvaluationValue result = evaluateSubtree(getAbstractSyntaxTree());
    if (result.isNumberValue()) {
      BigDecimal bigDecimal = result.getNumberValue();
      if (configuration.getDecimalPlacesResult()
          != ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED) {
        bigDecimal = roundValue(bigDecimal, configuration.getDecimalPlacesResult());
      }

      if (configuration.isStripTrailingZeros()) {
        bigDecimal = bigDecimal.stripTrailingZeros();
      }

      result = EvaluationValue.numberValue(bigDecimal);
    }

    return result;
  }

  /**
   * Evaluates only a subtree of the abstract syntax tree.
   *
   * @param startNode The {@link ASTNode} to start evaluation from.
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   */
  public EvaluationValue evaluateSubtree(ASTNode startNode) throws EvaluationException {
    Token token = startNode.getToken();
    EvaluationValue result;
    switch (token.getType()) {
      case NUMBER_LITERAL:
        result = EvaluationValue.numberOfString(token.getValue(), configuration.getMathContext());
        break;
      case STRING_LITERAL:
        result = EvaluationValue.stringValue(token.getValue());
        break;
      case VARIABLE_OR_CONSTANT:
        result = getVariableOrConstant(token);
        if (result.isExpressionNode()) {
          result = evaluateSubtree(result.getExpressionNode());
        }
        break;
      case PREFIX_OPERATOR:
      case POSTFIX_OPERATOR:
        result =
            token
                .getOperatorDefinition()
                .evaluate(this, token, evaluateSubtree(startNode.getParameters().get(0)));
        break;
      case INFIX_OPERATOR:
        result = evaluateInfixOperator(startNode, token);
        break;
      case ARRAY_INDEX:
        result = evaluateArrayIndex(startNode);
        break;
      case STRUCTURE_SEPARATOR:
        result = evaluateStructureSeparator(startNode);
        break;
      case FUNCTION:
        result = evaluateFunction(startNode, token);
        break;
      default:
        throw new EvaluationException(token, "Unexpected evaluation token: " + token);
    }
    if (result.isNumberValue()
        && configuration.getDecimalPlacesRounding()
            != ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED) {
      return EvaluationValue.numberValue(
          roundValue(result.getNumberValue(), configuration.getDecimalPlacesRounding()));
    }

    return result;
  }

  private EvaluationValue getVariableOrConstant(Token token) throws EvaluationException {
    EvaluationValue result = constants.get(token.getValue());
    if (result == null) {
      result = getDataAccessor().getData(token.getValue());
    }
    if (result == null) {
      throw new EvaluationException(
          token, String.format("Variable or constant value for '%s' not found", token.getValue()));
    }
    return result;
  }

  private EvaluationValue evaluateFunction(ASTNode startNode, Token token)
      throws EvaluationException {
    List<EvaluationValue> parameterResults = new ArrayList<>();
    for (int i = 0; i < startNode.getParameters().size(); i++) {
      if (token.getFunctionDefinition().isParameterLazy(i)) {
        parameterResults.add(convertValue(startNode.getParameters().get(i)));
      } else {
        parameterResults.add(evaluateSubtree(startNode.getParameters().get(i)));
      }
    }

    EvaluationValue[] parameters = parameterResults.toArray(new EvaluationValue[0]);

    FunctionIfc function = token.getFunctionDefinition();

    function.validatePreEvaluation(token, parameters);

    return function.evaluate(this, token, parameters);
  }

  private EvaluationValue evaluateArrayIndex(ASTNode startNode) throws EvaluationException {
    EvaluationValue array = evaluateSubtree(startNode.getParameters().get(0));
    EvaluationValue index = evaluateSubtree(startNode.getParameters().get(1));

    if (array.isArrayValue() && index.isNumberValue()) {
      return array.getArrayValue().get(index.getNumberValue().intValue());
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  private EvaluationValue evaluateStructureSeparator(ASTNode startNode) throws EvaluationException {
    EvaluationValue structure = evaluateSubtree(startNode.getParameters().get(0));
    Token nameToken = startNode.getParameters().get(1).getToken();
    String name = nameToken.getValue();

    if (structure.isStructureValue()) {
      if (!structure.getStructureValue().containsKey(name)) {
        throw new EvaluationException(
            nameToken, String.format("Field '%s' not found in structure", name));
      }
      return structure.getStructureValue().get(name);
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  private EvaluationValue evaluateInfixOperator(ASTNode startNode, Token token)
      throws EvaluationException {
    EvaluationValue left;
    EvaluationValue right;

    OperatorIfc op = token.getOperatorDefinition();
    if (op.isOperandLazy()) {
      left = convertValue(startNode.getParameters().get(0));
      right = convertValue(startNode.getParameters().get(1));
    } else {
      left = evaluateSubtree(startNode.getParameters().get(0));
      right = evaluateSubtree(startNode.getParameters().get(1));
    }
    return op.evaluate(this, token, left, right);
  }

  /**
   * Rounds the given value.
   *
   * @param value The input value.
   * @param decimalPlaces The number of decimal places to round to.
   * @return The rounded value, or the input value if rounding is not configured or possible.
   */
  private BigDecimal roundValue(BigDecimal value, int decimalPlaces) {
    value = value.setScale(decimalPlaces, configuration.getMathContext().getRoundingMode());
    return value;
  }

  /**
   * Returns the root ode of the parsed abstract syntax tree.
   *
   * @return The abstract syntax tree root node.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public ASTNode getAbstractSyntaxTree() throws ParseException {
    if (abstractSyntaxTree == null) {
      Tokenizer tokenizer = new Tokenizer(expressionString, configuration);
      ShuntingYardConverter converter =
          new ShuntingYardConverter(expressionString, tokenizer.parse(), configuration);
      abstractSyntaxTree = converter.toAbstractSyntaxTree();
    }

    return abstractSyntaxTree;
  }

  /**
   * Validates the expression by parsing it and throwing an exception, if the parser fails.
   *
   * @throws ParseException If there were problems while parsing the expression.
   */
  public void validate() throws ParseException {
    getAbstractSyntaxTree();
  }

  /**
   * Adds a variable value to the expression data storage. If a value with the same name already
   * exists, it is overridden. The data type will be determined by examining the passed value
   * object. An exception is thrown, if he found data type is not supported.
   *
   * @param variable The variable name.
   * @param value The variable value.
   * @return The Expression instance, to allow chaining of methods.
   */
  public Expression with(String variable, Object value) {
    if (constants.containsKey(variable)) {
      if (configuration.isAllowOverwriteConstants()) {
        constants.remove(variable);
      } else {
        throw new UnsupportedOperationException(
            String.format("Can't set value for constant '%s'", variable));
      }
    }
    getDataAccessor().setData(variable, convertValue(value));
    return this;
  }

  /**
   * Adds a variable value to the expression data storage. If a value with the same name already
   * exists, it is overridden. The data type will be determined by examining the passed value
   * object. An exception is thrown, if he found data type is not supported.
   *
   * @param variable The variable name.
   * @param value The variable value.
   * @return The Expression instance, to allow chaining of methods.
   */
  public Expression and(String variable, Object value) {
    return with(variable, value);
  }

  /**
   * Adds all variables values defined in the map with their name (key) and value to the data
   * storage.If a value with the same name already exists, it is overridden. The data type will be
   * determined by examining the passed value object. An exception is thrown, if he found data type
   * is not supported.
   *
   * @param values A map with variable values.
   * @return The Expression instance, to allow chaining of methods.
   */
  public Expression withValues(Map<String, ?> values) {
    for (Map.Entry<String, ?> entry : values.entrySet()) {
      with(entry.getKey(), entry.getValue());
    }
    return this;
  }

  /**
   * Return a copy of the expression using the copy constructor {@link Expression(Expression)}.
   *
   * @return The copied Expression instance.
   * @throws ParseException If there were problems while parsing the existing expression.
   */
  public Expression copy() throws ParseException {
    return new Expression(this);
  }

  /**
   * Create an AST representation for an expression string. The node can then be used as a
   * sub-expression. Subexpressions are not cached.
   *
   * @param expression The expression string.
   * @return The root node of the expression AST representation.
   * @throws ParseException On any parsing error.
   */
  public ASTNode createExpressionNode(String expression) throws ParseException {
    Tokenizer tokenizer = new Tokenizer(expression, configuration);
    ShuntingYardConverter converter =
        new ShuntingYardConverter(expression, tokenizer.parse(), configuration);
    return converter.toAbstractSyntaxTree();
  }

  /**
   * Converts a double value to an {@link EvaluationValue} by considering the configured {@link
   * java.math.MathContext}.
   *
   * @param value The double value to covert.
   * @return An {@link EvaluationValue} of type {@link EvaluationValue.DataType#NUMBER}.
   */
  public EvaluationValue convertDoubleValue(double value) {
    return convertValue(value);
  }

  /**
   * Converts an object value to an {@link EvaluationValue} by considering the configuration {@link
   * EvaluationValue(Object, ExpressionConfiguration)}.
   *
   * @param value The object value to covert.
   * @return An {@link EvaluationValue} of the detected type and value.
   */
  public EvaluationValue convertValue(Object value) {
    return new EvaluationValue(value, configuration);
  }

  /**
   * Returns the list of all nodes of the abstract syntax tree.
   *
   * @return The list of all nodes in the parsed expression.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public List<ASTNode> getAllASTNodes() throws ParseException {
    return getAllASTNodesForNode(getAbstractSyntaxTree());
  }

  private List<ASTNode> getAllASTNodesForNode(ASTNode node) {
    List<ASTNode> nodes = new ArrayList<>();
    nodes.add(node);
    for (ASTNode child : node.getParameters()) {
      nodes.addAll(getAllASTNodesForNode(child));
    }
    return nodes;
  }

  /**
   * Returns all variables that are used i the expression, excluding the constants like e.g. <code>
   * PI</code> or <code>TRUE</code> and <code>FALSE</code>.
   *
   * @return All used variables excluding constants.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public Set<String> getUsedVariables() throws ParseException {
    Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    for (ASTNode node : getAllASTNodes()) {
      if (node.getToken().getType() == Token.TokenType.VARIABLE_OR_CONSTANT
          && !constants.containsKey(node.getToken().getValue())) {
        variables.add(node.getToken().getValue());
      }
    }

    return variables;
  }

  /**
   * Returns all variables that are used in the expression, but have no value assigned.
   *
   * @return All variables that have no value assigned.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public Set<String> getUndefinedVariables() throws ParseException {
    Set<String> variables = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    for (String variable : getUsedVariables()) {
      if (getDataAccessor().getData(variable) == null) {
        variables.add(variable);
      }
    }
    return variables;
  }
}
