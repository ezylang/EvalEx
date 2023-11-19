---
layout: default
title: Precision, Scale and Rounding
parent: Concepts
nav_order: 3
---

## Precision, Scale and Rounding

EvalEx uses _java.math.BigDecimal_ values for (most) calculations and for internal storage of number
values and variables.

In contrast to float or double values, which are stored in floating-point arithmetic, BigDecimal
values are stored as signed decimal number of arbitrary precision with an associated scale.

This makes BigDecimal a perfect choice in applications, where the precise decimal values are needed,
e.g. in financial systems.

### Precision

In BigDecimal (and EvalEx), the precision is the number of digits in the unscaled value, independent
of the number of decimal digits.
Both of this values

- 123.456
- 1234.56

have a precision of 6, but have a different scale (the first has a scale of 3, the second of 2).

Values that exceed the maximum precision, will be rounded according to the rounding mode.

The default precision in EvalEx is 68.

### Scale

The scale of a BigDecimal is the number of decimal digits. Any BigDecimal can be rounded to a lower
number of scale (less decimal digits) using a rounding mode.

### Rounding Mode

EvalEx supports all rounding modes defined in _java.math.RoundingMode_:

| Mode        | Description                                                                                                                                  |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| CEILING     | Rounding mode to round towards positive infinity.                                                                                            |
| DOWN        | Rounding mode to round towards zero.                                                                                                         |
| FLOOR       | Rounding mode to round towards negative infinity.                                                                                            |                                                                                            
| HALF_DOWN   | Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round down.                           |
| HALF_EVEN   | Rounding mode to round towards the "nearest neighbor" unless both neighbors are equidistant, in which case, round towards the even neighbor. |
| HALF_UP     | Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.                             |
| UNNECESSARY | Rounding mode to assert that the requested operation has an exact result, hence no rounding is necessary.                                    |
| UP          | Rounding mode to round away from zero.                                                                                                       |

The default rounding mode in EvalEx is _HALF_EVEN_.

### Configuring Precision, Rounding Mode and Automatic Rounding

Precision and rounding mode are configured through the _ExpressionConfiguration_ by specifying
the _java.math.MathContext_:

```java
// set precision to 32 and rounding mode to HALF_UP
ExpressionConfiguration configuration =
    ExpressionConfiguration.builder()
        .mathContext(new MathContext(32, RoundingMode.HALF_UP))
        .build();
```

Automatic rounding is disabled by default. When enabled, EvalEx will round all input variables,
intermediate operation and function results and the final result to the specified number of decimal
digits, using the current rounding mode:

```java
// set automatic rounding
ExpressionConfiguration configuration =
    ExpressionConfiguration.builder()
        .decimalPlacesRounding(2)
        .build();

Expression expression = new Expression("2.128 + a", configuration);

// prints 3.26
System.out.println(
    expression
        .with("a", new BigDecimal("1.128"))
        .evaluate()
        .getNumberValue());        
```

If only the final result should be rounded, this can be configured using the _decimalPlacesResult_:

```java
// set rounding of final result
ExpressionConfiguration configuration =
    ExpressionConfiguration.builder()
        .decimalPlacesResult(3)
        .build();

Expression expression = new Expression("1.22222+1.22222+1.22222", configuration);

// prints 3.667
System.out.println(
    expression
        .evaluate()
        .getNumberValue());        
```
