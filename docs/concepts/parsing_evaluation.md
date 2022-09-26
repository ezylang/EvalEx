---
layout: default
title: Parsing and Evaluation
parent: Concepts
nav_order: 1
---

## Parsing and Evaluation

### Parsing

To parse and evaluate an expression, you first create an expression object:

```java
Expression expression = new Expression("1 + 2 / (4 * SQRT(4))");
```

Optionally, you can create a custom configuration and pass this in the constructor:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .decimalPlacesRounding(2)
    .build();

Expression expression = new Expression("1 + 2 / (4 * SQRT(4))", configuration);
```

Creating the expression object does not parse or validate the expression.

Parsing will be performed, when one of the following happens:

- The expression is validated wih the _validate()_ method.
- The expression is evaluated with the _evaluate()_ method.
- An abstract syntax tree (AST) is created with the _getAbstractSyntaxTree()_ method or any public
  method that uses it.

Once an expression was parsed, the resulting abstract syntax tree is cached inside the expression
and wil be re-used on subsequent evaluations.

### Evaluation

Evaluation is done by traversing the parsed abstract syntax tree. If it has not been generated
before, it will be created and cached prior to the evaluation.

```java
Expression expression = new Expression("1 + 2 / (4 * SQRT(4))");

EvaluationValue result = expression.evaluate();

System.out.println(result.getNumberValue()); // prints 1.25
```

The evaluation will return an _EvaluationValue_ object. Depending on the expression, the
_EvaluationValue_ will be of one of the types:

- NUMBER - If the expression resulted in a number.
- STRING - If the expression resulted in a string.
- BOOLEAN - If the expression resulted in a boolean value.
- ARRAY - If the expression resulted in an array.
- STRUCTURE - If the expression resulted in a structure.

The _EvaluationValue_ has methods to check and retrieve/convert the evaluation value.

See chapter [Data Types](datatypes.html) for details on the different data types.

### Validation

An expression can be validated with the _validate()_ method. If there is a parsing problem, the
method will throw a _ParseException_, which holds detailed information about the problem and its
location.

### Passing Variables

Variables can be used in an expression. Their value will be retrieved during evaluation by querying
the defined data accessor.

by default, EvalEx uses the _MapBasedDataAccessor_, which stores the variable values in a
case-insensitive map. Each expression will have an own instance of the accessor, so that different
expression do not share the same data.

Prior to evaluation, the variable values can be set by setting them one by one, using the _with()_
and _and()_ methods.
(Both methods are the same, the name difference is just for convenience).

```java
Expression expression = new Expression("(a + b) * (a - b)");

EvaluationValue result = expression
    .with("a", 3.5)
    .and("b", 2.5)
    .evaluate();

System.out.println(result.getNumberValue()); // prints 6.00
```

Alternatively, the variable values can be set by defining a map with names and values and then
passing it to the _withValues()_ method:

```java
Expression expression = new Expression("(a + b) * (a - b)");

Map<String, Object> values = new HashMap<>();
values.put("a", 3.5);
values.put("b", 2.5);

EvaluationValue result = expression.withValues(values).evaluate();

System.out.println(result.getNumberValue()); // prints 6.00
```

The data conversion of the passed values will automatically be performed through the created
_EvaluationObject_.

See chapter [Data Types](datatypes.html) for details on the conversion.

Another option to have EvalEx use your data is to define a custom data accessor.

See chapter [Data Access](../customization/data_access.html) for details.

### Exception Handling

In EvalEx, there are two general exceptions:

- A _ParseException_ is thrown, when the expression could not be parsed.
- An _EvaluationException_ is thrown, when the expression could be parsed, but not evaluated.

If possible, both exceptions report the following details:

- Start Position of the error (character position, starting with 1).
- End Position of the error (character position, starting with 1).
- The Token string, usually the operator, function, variable or literal causing the error.
- The error message.

