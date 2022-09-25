---
layout: default
title: Operator Dictionary
parent: Customization
nav_order: 4
---

## Function Dictionary

By default, EvalEx has its own dictionary for the operators that can be used in an expression,
the _MapBasedOperatorDictionary_. It stores and retrieves the operators in a case-insensitive
_TreeMap_.

You can easily define your own operator directory, by defining a class that implements the
_FunctionDictionaryIfc_:

```java
public interface OperatorDictionaryIfc {

  /**
   * Allows to add an operator to the dictionary. Implementation is optional, if you have a fixed
   * set of operators, this method can throw an exception.
   *
   * @param operatorString The operator name.
   * @param operator The operator implementation.
   */
  void addOperator(String operatorString, OperatorIfc operator);

  /**
   * Check if the dictionary has a prefix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasPrefixOperator(String operatorString) {
    return getPrefixOperator(operatorString) != null;
  }

  /**
   * Check if the dictionary has a postfix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasPostfixOperator(String operatorString) {
    return getPostfixOperator(operatorString) != null;
  }

  /**
   * Check if the dictionary has an infix operator with that name.
   *
   * @param operatorString The operator name to look for.
   * @return <code>true</code> if an operator was found or <code>false</code> if not.
   */
  default boolean hasInfixOperator(String operatorString) {
    return getInfixOperator(operatorString) != null;
  }

  /**
   * Get the operator definition for a prefix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getPrefixOperator(String operatorString);

  /**
   * Get the operator definition for a postfix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getPostfixOperator(String operatorString);

  /**
   * Get the operator definition for an infix operator name.
   *
   * @param operatorString The name of the operator.
   * @return The operator definition or <code>null</code> if no operator was found.
   */
  OperatorIfc getInfixOperator(String operatorString);
}
```

Only four methods need to be implemented:

- addOperator(String operatorString, OperatorIfc operator) - which allows to add an operator.
- OperatorIfc getPrefixOperator(String operatorString) - retrieves a prefix operator.
- OperatorIfc getPostfixOperator(String operatorString) - retrieves a postfix operator.
- OperatorIfc getInfixOperator(String operatorString) - retrieves a infix operator.

The _addOperator()_ method is only called when an operator is added to the configuration, using the
_withAdditionalOperators()_ in the _ExpressionConfiguration_ object.

> NOTE: The operator names that are passed to the methods are case-sensitive, just like they were
> entered in the expression.

The custom operator dictionary can then be specified in the expression configuration:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .operatorDictionary(new MyOperatorDictionary())
    .build();
```
