---
layout: default
title: Custom Operators
parent: Customization
nav_order: 2
---

## Custom Operators

EvalEx comes with a predefined set of ready-to use operators.
A standard collection is added to the default configuration, using the
_ExpressionConfiguration.StandardOperatorsDictionary_.

Operation definition is made in two parts:

- Defining the operator type and logic by adding a class that implements the _OperatorIfc_
  interface.
- Adding the operator with a unique name to the operator dictionary, so it can be found and used in
  expressions.

### Defining the Operator

To ease some common implementation routines, a custom operator usually extends the
_AbstractOperator_ class.
As an example, we can look at the boolean "AND" operator:

```java
@InfixOperator(precedence = OPERATOR_PRECEDENCE_AND)
public class InfixAndOperator extends AbstractOperator {

  @Override
  public EvaluationValue evaluate(
      Expression expression, Token operatorToken, EvaluationValue... operands) {
    return new EvaluationValue(operands[0].getBooleanValue() && operands[1].getBooleanValue());
  }
}
```

As you can see, the only method that has to be implemented, is the _evaluate()_ method. It will be
called by EvalEx during evaluation. It will pass the operand values to the method.

The expression itself and also the operator token are passed to the call, they can be used to
access the configuration or to find out the operator name and its position in the expression.

#### Operator type

The operator type is determined through the corresponding annotation:

- @PrefixOperator
- @InfixOperator
- @PostfixOperator

Infix operators will receive two operands in the _evaluate()_ method, pre- and postfix operator will
receive one operand.

#### Operator Precedence and associativity

The order of expression evaluation is determined by the operator precedence and its associativity.

The precedence tells EvalEx which operator have to be evaluated first. With this, it is e.g. defined
that multiplication happens before addition.

The associativity tells in which order (left to right, right to left) operators of the same
precedence are evaluated.

Precedence and associativity can be specified with the operator annotation.

There is a collection of predefined operator precedences in the _OperatorIfc_ interface.

### Adding the Operator

You can always add the operator directly to the operator dictionary, using the
_addOperator(String operatorString, OperatorIfc operator)_ method.

But there is an easier way by using the expression configuration, which also allows you to add more
than one operator:

```java
ExpressionConfiguration configuration =
    ExpressionConfiguration.defaultConfiguration()
        .withAdditionalOperators(
            Map.entry("AND", new InfixAndOperator()),
            Map.entry("OR", new InfixOrOperator()));
    
Expression expression = new Expression("(a > 5 AND x < 10) OR (y < 0)");
```
