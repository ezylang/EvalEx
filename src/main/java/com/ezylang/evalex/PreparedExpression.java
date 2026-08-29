/*
  Copyright 2012-2026 Udo Klimaschewski

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
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.ShuntingYardConverter;
import com.ezylang.evalex.parser.Tokenizer;

/**
 * A pre-parsed, immutable, thread-safe expression that can cheaply produce bound {@link Expression}
 * instances for evaluation.
 *
 * <p>Use this class when the same expression text is evaluated repeatedly with different variable
 * bindings. The expensive parsing step (tokenization and AST construction) is performed once at
 * construction time. Subsequent evaluations only pay the cost of the AST tree walk.
 *
 * <p>This class is analogous to JDBC's {@code PreparedStatement}: compile once, bind and execute
 * many times.
 *
 * <p><b>Thread Safety:</b> Instances of this class are immutable and safe for concurrent use by
 * multiple threads. Each call to {@link #newExpression(DataAccessorIfc)} returns an independent
 * {@link Expression} instance that is not shared.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Parse once at startup
 * PreparedExpression prepared = new PreparedExpression("price * quantity", config);
 *
 * // Evaluate per request — no re-parsing
 * for (Order order : orders) {
 *     Expression expr = prepared.newExpression(order.asDataAccessor());
 *     EvaluationValue total = expr.evaluate();
 * }
 * }</pre>
 *
 * @author oswaldo.bapvic.jr
 * @since 3.8.0
 */
public class PreparedExpression {

  private final String expressionString;
  private final ASTNode abstractSyntaxTree;
  private final ExpressionConfiguration configuration;

  /**
   * Creates a new {@code PreparedExpression} by parsing the given expression string.
   *
   * <p>The expression is tokenized and converted to an abstract syntax tree immediately. If the
   * expression is syntactically invalid, a {@link ParseException} is thrown.
   *
   * @param expressionString the expression to parse; must not be null or blank
   * @param configuration the expression configuration defining available functions, operators, and
   *     evaluation settings
   * @throws ParseException if the expression cannot be parsed
   */
  public PreparedExpression(String expressionString, ExpressionConfiguration configuration)
      throws ParseException {
    this.expressionString = expressionString;
    this.configuration = configuration;
    Tokenizer tokenizer = new Tokenizer(expressionString, configuration);
    this.abstractSyntaxTree =
        new ShuntingYardConverter(expressionString, tokenizer.parse(), configuration)
            .toAbstractSyntaxTree();
  }

  /**
   * Creates a new {@code PreparedExpression} by parsing the given expression string with the
   * default configuration.
   *
   * @param expressionString the expression to parse; must not be null or blank
   * @throws ParseException if the expression cannot be parsed
   */
  public PreparedExpression(String expressionString) throws ParseException {
    this(expressionString, ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Creates a new {@link Expression} that reuses this prepared expression's pre-parsed AST and
   * configuration, bound to the given data accessor for variable resolution.
   *
   * <p>This method is cheap — it allocates an {@code Expression} object and initializes its
   * constants map, but performs no parsing.
   *
   * @param dataAccessor the data accessor providing variable values for evaluation
   * @return a new {@link Expression} ready to evaluate
   */
  public Expression newExpression(DataAccessorIfc dataAccessor) {
    return new Expression(
        this.expressionString, this.configuration, this.abstractSyntaxTree, dataAccessor);
  }

  /**
   * Creates a new {@link Expression} using the configuration's default data accessor supplier.
   *
   * @return a new {@link Expression} ready to evaluate
   */
  public Expression newExpression() {
    DataAccessorIfc accessor = configuration.getDataAccessorSupplier().get();
    return new Expression(
        this.expressionString, this.configuration, this.abstractSyntaxTree, accessor);
  }

  /**
   * Returns the original expression string.
   *
   * @return the expression string
   */
  public String getExpressionString() {
    return expressionString;
  }

  /**
   * Returns the pre-parsed abstract syntax tree.
   *
   * @return the AST root node
   */
  public ASTNode getAbstractSyntaxTree() {
    return abstractSyntaxTree;
  }

  /**
   * Returns the expression configuration.
   *
   * @return the configuration
   */
  public ExpressionConfiguration getConfiguration() {
    return configuration;
  }
}
