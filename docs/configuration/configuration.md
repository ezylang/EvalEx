---
layout: default
title: Configuration
nav_order: 3
---

## Configuration

EvalEx can be configured through an _ExpressionConfiguration_ object, which can be passed as a
parameter to the _Expression_ constructor.

Example usage, showing all default configuration values:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .allowOverwriteConstants(true)
    .arraysAllowed(true)
    .dataAccessorSupplier(MapBasedDataAccessor::new)
    .decimalPlacesRounding(ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED)
    .defaultConstants(ExpressionConfiguration.StandardConstants)
    .functionDictionary(ExpressionConfiguration.StandardFunctionsDictionary)
    .implicitMultiplicationAllowed(true)
    .mathContext(ExpressionConfiguration.DEFAULT_MATH_CONTEXT)
    .operatorDictionary(ExpressionConfiguration.StandardOperatorsDictionary)
    .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER)
    .stripTrailingZeros(true)
    .structuresAllowed(true)
    .build();

Expression expression = new Expression("2.128 + a", configuration);
```

### Allow to Overwrite Constants

If set to true (default), then variables can be set that have the name of a constant. In that case,
the constant value will be removed and a variable value will be set.

### Arrays allowed

Specifies if the array index function is allowed (default is true). If set to false, the expression
will throw a _ParseException_, if there is a '[' is encountered in the expression and also no
operator or function is defined for this character.

### Data Accessor

The Data Accessor is responsible for storing and retrieving variable values.

See chapter [Data Access](../customization/data_access.html) for details.

The default implementation is the _MapBasedDataAccessor_, which stores all variables in a
**case-insensitive** _Map_.

The supplier is called whenever a new Expression is created, so that each expression can own an own
instance of an accessor. Custom implementations of the supplier and data accessor may allow
expressions to share the same space.

### Decimal Places Rounding

Specifies the amount of decimal places to round to in each operation or function.
See chapter [Precision, Scale and Rounding](../concepts/rounding.html) for details.

### Default Constants

Specifies the default constants that can be used in every expression as a _Map_ with the constant
name and _EvaluationValue_ as value. Each expression has a case-insensitive copy of the default
constants.
See the reference chapter for a list: [Default Constants](../references/constants.html)

### Function Dictionary

The function dictionary is used to look up the functions that are used in an expression.

See chapter [Function Dictionary](../customization/function_dictionary.html) for details.

The default implementation is the _MapBasedFunctionDictionary_, which stores all variables in a
**case-insensitive** _Map_.

### Implicit Multiplication

Implicit multiplication automatically adds in expression like "(a+b)(b+c)" the missing
multiplication operator, so that the expression reads "(a+b) * (b+c)".

By default, implicit multiplication is enabled. It can be disabled with this configuration
parameter.

### Math Context

The math context is used throughout all operations and functions. The default has a precision of 68
and a rounding mode of _HALF_EVEN_.

See chapter [Precision, Scale and Rounding](../concepts/rounding.html) for details.

### Operator Dictionary

The operator dictionary is used to look up the operators that are used in an expression.

See chapter [Operator Dictionary](../customization/operator_dictionary.html) for details.

The default implementation is the _MapBasedOperatorDictionary_, which stores all variables in a
**case-insensitive** _Map_.

### Power Of Precedence

In mathematics, there is no general rule which precedence the power-of operator has.
Consider the expression "-2^2". The expression can have two different results, depending on the
precedence:

- If the precedence is lower than the unary minus, the result will be 4 (-2 * -2).
- If the precedence is higher than the unary minus, the result will be -4 -(2 * 2).

By default, EvalEx uses a lower precedence. You can configure to use a higher precedence by
specifying it here, or by using a predefined constant:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER_HIGHER)
    .build();

// will now result in -4, instead of 4:
Expression expression = new Expression("-2^2", configuration);
```

### Strip Trailing Zeros

If set to true (default), then the trailing decimal zeros in a number result will be stripped.
E.g. a _BigDecimal_ result of _9.0_ will become _9_ and _-2.120000_ will become _-2.12_.

### Structures Allowed

Specifies if the structure separator ('.') operator is allowed (default is true). If set to false,
the expression will throw a _ParseException_, if the a '.' is encountered in the expression and also
no operator or function is defined for this character.
