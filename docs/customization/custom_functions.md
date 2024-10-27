---
layout: default
title: Custom Functions
parent: Customization
nav_order: 1
---

## Custom Functions

EvalEx comes with a predefined set of ready-to use functions.
A standard collection is added to the default configuration, using the
_ExpressionConfiguration.StandardFunctionsDictionary_.

Function definition is made in two parts:

- Defining the function parameters and logic by adding a class that implements the _FunctionIfc_
  interface.
- Adding the function with a unique name to the function dictionary, so it can be found and used in
  expressions.

### Defining the Function

To ease some common implementation routines, a custom function usually extends the
_AbstractFunction_ class.
As an example, we can look at the _ROUND()_ function:

```java
@FunctionParameter(name = "value")
@FunctionParameter(name = "scale")
public class RoundFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {

    EvaluationValue value = parameterValues[0];
    EvaluationValue precision = parameterValues[1];

    return new EvaluationValue(
        value
            .getNumberValue()
            .setScale(
                precision.getNumberValue().intValue(),
                expression.getConfiguration().getMathContext().getRoundingMode()));
  }
}
```

As you can see, the only method that has to be implemented, is the _evaluate()_ method. It will be
called by EvalEx during evaluation. It will pass all parameters values to function.

The expression itself and also the function token are passed to the call, they can be used to
access the configuration or to find out the function name and its position in the expression.

#### Parameter Definition

Parameters are defined by annotating the function class. Each _FunctionParameter_ has at least a
name.
In the above example, the function expects two parameters, the value and the scale.

#### Variable Number of Arguments

Some functions allow a variable number of arguments. For this, a parameter can have the flag _
isVarArg_ set to _true_.

> NOTE: Only the last parameter and only one parameter is allowed to have the _isVarArg_ flag set
> to _true_.

For example, the _MAX()_ function makes use of a variable number of arguments:

```java
@FunctionParameter(name = "value", isVarArg = true)
public class MaxFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    BigDecimal max = null;
    for (EvaluationValue parameter : parameterValues) {
      if (max == null || parameter.getNumberValue().compareTo(max) > 0) {
        max = parameter.getNumberValue();
      }
    }
    return new EvaluationValue(max);
  }
}
```

#### Lazy Parameter Evaluation

Usually, a function parameter already holds the evaluated value. E.g. in "ROUND(4/3, 2)", the
function _evaluate()_ will be passed the already calculated result of "4/3".

In some cases, this is not useful. Take for example the _IF()_ function. It has three parameters:

- A boolean condition
- An expression value that is returned if the condition is _true_
- An expression value that is returned if the condition is _false_

Here not both expressions should be evaluated, before the function is called. This may lead to
errors, e.g. in "IF(a == 0, 0, 2 / a)" the evaluation of "2 / a" would lead to a division by zero
error.

Therefore, function parameters can be defined as "lazy", which means that they will not be
evaluated, before the function is called. Instead, the expression sub-nodes are passed as a
parameter and the function is responsible to evaluate the subexpression.

This can be easily done, as the _IF()_ function demonstrates:

```java
@FunctionParameter(name = "condition")
@FunctionParameter(name = "resultIfTrue", isLazy = true)
@FunctionParameter(name = "resultIfFalse", isLazy = true)
public class IfFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues)
      throws EvaluationException {
    if (parameterValues[0].getBooleanValue()) {
      return expression.evaluateSubtree(parameterValues[1].getExpressionNode());
    } else {
      return expression.evaluateSubtree(parameterValues[2].getExpressionNode());
    }
  }
}
```

### Adding the Function

You can always add the function directly to the function dictionary, using the
_addFunction(String functionName, FunctionIfc function)_ method.

But there is an easier way by using the expression configuration, which also allows you to add more
than one function:

```java
ExpressionConfiguration configuration =
    ExpressionConfiguration.defaultConfiguration()
        .withAdditionalFunctions(
            Map.entry("MAX_VALUE", new MaxFunction()),
            Map.entry("MIN_VALUE", new MinFunction()));
    
Expression expression = new Expression("MAX_VALUE(1,2,3) + MIN_VALUE(7,8,9)", configuration);
```
