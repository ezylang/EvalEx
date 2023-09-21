---
layout: default
title: Standard Functions
nav_order: 3
parent: References
---

## Standard Functions

Available through the _ExpressionConfiguration.StandardFunctionsDictionary_ constant:

### Basic Functions

| Name                                       | Description                                                                                                             |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| ABS(value)                                 | Absolute (non-negative) value                                                                                           |
| CEILING(value)                             | Rounds the given value an integer using the rounding mode CEILING                                                       |
| COALESCE(value, ...)                       | Returns the first non-null parameter, or NULL if all parameters are null                                                |
| FACT(base)                                 | Calculates the factorial of a base value                                                                                |
| FLOOR(value)                               | Rounds the given value an integer using the rounding mode FLOOR                                                         |
| IF(condition, resultIfTrue, resultIfFalse) | Conditional evaluation function. If _condition_ is true, the _resultIfTrue_ is returned, else the _resultIfFalse_ value |
| LOG(value)                                 | The natural logarithm (base e) of a value                                                                               |
| LOG10(value)                               | The base 10 logarithm of a value                                                                                        |
| MAX(value, ...)                            | Returns the maximum value of all parameters                                                                             |
| MIN(value, ...)                            | Returns the maximum value of all parameters                                                                             |
| NOT(value)                                 | Boolean negation, implemented as a function (for compatibility)                                                         |
| RANDOM()                                   | Produces a random value between 0 and 1                                                                                 |
| ROUND(value, scale)                        | Rounds the given value to the specified scale, using the current rounding mode                                          |
| SQRT(value)                                | Square root function                                                                                                    |
| SUM(value, ...)                            | Returns the sum of all parameters                                                                                       |

### String Functions

| Name                            | Description                                                                 |
|---------------------------------|-----------------------------------------------------------------------|
| STR_CONTAINS(string, substring) | Returns true, if the string contains the substring (case-insensitive) |
| STR_LOWER(value)                | Converts the given value to lower case                                |
| STR_UPPER(value)                | Converts the given value to upper case                                |

### Trigonometric Functions

| Name         | Description                                                                                    |
|--------------|------------------------------------------------------------------------------------------------|
| ACOS(value)  | Returns the arc-cosine (in degrees)                                                            |
| ACOSH(value) | Returns the hyperbolic arc-cosine                                                              |
| ACOSR(value) | Returns the arc-cosine (in radians)                                                            |
| ACOT(value)  | Returns the arc-co-tangent (in degrees)                                                        |
| ACOTH(value) | Returns the hyperbolic arc-co-tangent (in degrees)                                             |
| ACOTR(value) | Returns the arc-co-tangent (in radians)                                                        |
| ASIN(value)  | Returns the arc-sine (in degrees)                                                              |
| ASINH(value) | Returns the hyperbolic arc-sine                                                                |
| ASINR(value) | Returns the arc-sine (in radians)                                                              |
| ATAN2(y, x)  | Returns the angle of atan2 (in degrees)                                                        |
| ATAN2R(y, x) | Returns the angle of atan2 (in radians)                                                        |
| ATAN(value)  | Returns the arc-tangent (in degrees)                                                           |
| ATANH(value) | Returns the hyperbolic arc-tangent                                                             |
| ATANR(value) | Returns the arc-tangent (in radians)                                                           |
| COS(value)   | Returns the cosine of an angle (in degrees)                                                    |
| COSH(value)  | Returns the hyperbolic cosine of a value                                                       |
| COSR(value)  | Returns the cosine of an angle (in radians)                                                    |
| COT(value)   | Returns the co-tangent of an angle (in degrees)                                                |
| COTH(value)  | Returns the hyperbolic co-tangent of a value                                                   |
| COTR(value)  | Returns the co-tangent of an angle (in radians)                                                |
| CSC(value)   | Returns the co-secant of an angle (in degrees)                                                 |
| CSCH(value)  | Returns the hyperbolic co-secant of a value                                                    |
| CSCR(value)  | Returns the co-secant of an angle (in radians)                                                 |
| DEG(rad)     | Converts an angle measured in radians to an approximately equivalent angle measured in degrees |
| RAD(degrees) | Converts an angle measured in degrees to an approximately equivalent angle measured in radians |
| SEC(value)   | Returns the secant of an angle (in degrees)                                                    |
| SECH(value)  | Returns the hyperbolic secant of an angle                                                      |
| SECR(value)  | Returns the secant of an angle (in radians)                                                    |
| SIN(value)   | Returns the sine of an angle (in degrees)                                                      |
| SINH(value)  | Returns the hyperbolic sine of a value                                                         |
| SINR(value)  | Returns the sine of an angle (in radians)                                                      |
| TAN(value)   | Returns the tangent of an angle (in degrees)                                                   |
| TANH(value)  | Returns the hyperbolic tangent of a value                                                      |
| TANR(value)  | Returns the tangent of an angle (in radians)                                                   |

### Date Time Functions

| Name                                                          | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|---------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DT_DATE_TIME(year, month, day [, hour, minute, second, nano]) | Returns the corresponding date time value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| DT_PARSE(value [, format])                                    | Converts the given string value to a date time value by using the optional format. Without a format, the [ISO_LOCAL_DATE_TIME ](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME), [ISO_LOCAL_DATE](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME) or [ISO_INSTANT](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#ISO_INSTANT)(for more details on the format, see [JDK DateTimeFormatted documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html) |
| DT_ZONED_PARSE(value [, format])                              | Converts the given string zoned date time value to a date time type by using the optional format. Without a format, the [ISO representation is used](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#ISO_ZONED_DATE_TIME) (for more details on the format, see [JDK DateTimeFormatted documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html)                                                                                                                                                                                                                                                          |
| DT_FORMAT(value, [, format])                                  | Converts the given date time value to a string value by using the optional format. Without a format, the [ISO representation is used](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME). (for more details on the format, see [JDK DateTimeFormatted documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html)                                                                                                                                                                                                                                                                        |
| DT_EPOCH(value)                                               | Converts the given value to epoch timestamp in millisecond                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| DT_DATE_TIME_EPOCH(value)                                     | Converts the given epoch timestamp value to a date time value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| DT_DURATION_MILLIS(value)                                     | Converts the given value in millisecond to a duration value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| DT_DURATION_DAYS(value)                                       | Converts the given days quantity to a duration value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| DT_DURATION_PARSE(value)                                      | Converts the given duration representation to a duration value (see [JDK Duration#parse documentation](https://docs.oracle.com/javase/11/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | 
