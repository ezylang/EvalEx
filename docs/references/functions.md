---
layout: default
title: Standard Functions
nav_order: 3
parent: References
---

## Standard Functions

Available through the _ExpressionConfiguration.StandardFunctionsDictionary_ constant:

### Basic Functions

| Name                 | Description                                                                                                                                     |
|----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| ABS(value)           | Absolute (non-negative) value                                                                                                                   |
| AVERAGE(value, ...)  | Returns the average (arithmetic mean) of all parameters. If a parameter is of type _ARRAY_, the average of all elements is calculated.          |
| CEILING(value)       | Rounds the given value an integer using the rounding mode CEILING                                                                               |
| COALESCE(value, ...) | Returns the first non-null parameter, or NULL if all parameters are null                                                                        |
| FACT(base)           | Calculates the factorial of a base value                                                                                                        |
| FLOOR(value)         | Rounds the given value an integer using the rounding mode FLOOR                                                                                 |
| [IF](#if)            | Conditional evaluation function. Returns one value or another, depending on a given _condition_.                         |
| LOG(value)           | The natural logarithm (base e) of a value                                                                                                       |
| LOG10(value)         | The base 10 logarithm of a value                                                                                                                |
| MAX(value, ...)      | Returns the maximum value of all parameters. If a parameter is of type _ARRAY_, the maximum of all elements is calculated.                      |
| MIN(value, ...)      | Returns the minimum value of all parameters. If a parameter is of type _ARRAY_, the minimum of all elements is calculated.                      |
| NOT(value)           | Boolean negation, implemented as a function (for compatibility)                                                                                 |
| RANDOM()             | Produces a random value between 0 and 1                                                                                                         |
| ROUND(value, scale)  | Rounds the given value to the specified scale, using the current rounding mode                                                                  |
| SQRT(value)          | Square root function. Uses the implementation from _The Java Programmers Guide To numerical Computing_ by Ronald Mak, 2002.                     |
| SUM(value, ...)      | Returns the sum of all parameters. If a parameter is of type _ARRAY_, the sum of all elements is calculated.                                    |
| [SWITCH](#switch)    | Returns the _result_ correponding to the first matching _value_ in the specified _expression_ or an optional _default_ value if no match found. |

### String Functions

| Name                                    | Description                                                                                                                                   |
|-----------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| STR_CONTAINS(string, substring)         | Returns true if the string contains the substring (case-insensitive)                                                                          |
| STR_ENDS_WITH(string, substring)        | Returns true if the string ends with the substring (case-sensitive)                                                                           |
| STR_FORMAT(format [,argument, ...])     | Returns a formatted string using the specified format string and arguments, using the configured locale                                       |
| STR_LEFT(value, n)                      | Returns the first n characters from the left of the given string                                                                              |  
| STR_LENGTH(string)                      | Returns the length of the string                                                                                                              |
| STR_LOWER(value)                        | Converts the given value to lower case                                                                                                        |
| STR_MATCHES(string, pattern)            | Returns true if the string matches the RegEx pattern                                                                                          |
| STR_RIGHT(value, n)                     | Returns the last n characters from the left of the given string                                                                               |
| STR_SPLIT(string, separator)            | Splits the given _string_ into an array, given a _separator_.                                                                                 |
| STR_STARTS_WITH(string, substring)      | Returns true if the string starts with the substring (case-sensitive)                                                                         |
| STR_SUBSTRING(string, start[, end])     | Returns a substring of the given string, starting at the _start_ index and ending at the _end_ index (the end of the string if not specified) |
| STR_TRIM(string)                        | Returns the given string with all leading and trailing space removed.                                                                         |
| STR_UPPER(value)                        | Converts the given value to upper case                                                                                                        |

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

| Name                                                                           | Description                                                                                                                                                                                                                                                                        |
|--------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DT_DATE_NEW(year, month, day [,hour, minute, second, millis, nanos] [,zoneId]) | Returns a new DATE_TIME value with the given parameters. An optional time zone (string) can be specified, e.g. "Europe/Berlin", or "GMT+02:00". If no zone id is specified, the configured zone id is used.                                                                        |
| DT_DATE_NEW(millis)                                                            | Returns a new DATE_TIME from the epoch of 1970-01-01T00:00:00Z in milliseconds.                                                                                                                                                                                                    |
| DT_DATE_PARSE(value [,zoneId] [,format, ...])                                  | Converts the given string value to a date time value by using the optional time zone and formats. All formats are used until the first matching format. Without a format, the configured formats are used. Time zone can be NULL, the the configured time zone and locale is used. |
| DT_DATE_FORMAT(value, [,format] [,zoneId])                                     | Formats the given date-time to a string using the given optional format and time zone. Without a format, the first configured format is used. The zone id defaults to the configured zone id.                                                                                      |
| DT_DATE_TO_EPOCH(value)                                                        | Converts the given value to epoch timestamp in millisecond.                                                                                                                                                                                                                        |
| DT_DURATION_NEW(days [,hours, minutes, seconds, nanos])                        | Returns a new DURATION value with the given parameters.                                                                                                                                                                                                                            |
| DT_DURATION_PARSE(value)                                                       | Converts the given ISO-8601 duration string representation to a duration value. E.g. "P2DT3H4M" parses 2 days, 3 hours and 4 minutes.                                                                                                                                              | 
| DT_DURATION_FROM_MILLIS(millis)                                                | Returns a new DURATION value with the given milliseconds.                                                                                                                                                                                                                          |
| DT_DURATION_TO_MILLIS(value)                                                   | Converts the given duration to a milliseconds value.                                                                                                                                                                                                                               | 
| DT_NOW()                                                                       | Produces a new DATE_TIME that represents the current moment in time.                                                                                                                                                                                                               |
| DT_TODAY([zoneId])                                                             | Produces a new DATE_TIME that represents the current date, at midnight (00:00). An optional time zone (string) can be specified, e.g. "America/Sao_Paulo", or "GMT-03:00". If no zone id is specified, the configured zone id is used.                                             |

---


## IF

The IF function performs a logical test and returns one value if the test is true and another one if it's false.

### Syntax

```
IF(condition, value_if_true, value_if_false)
```

### Parameters

| Name           | Description                                      |
|----------------|--------------------------------------------------|
| condition      | The logical test you want to evaluate.           |
| value_if_true  | The value returned if the logical test is true.  |
| value_if_false | The value returned if the logical test if false. |


### Examples

Consider the following variables:

| Name           | Value   |
|----------------|---------|
| `myFlag`       | `false` |
| `numSales`     | `1200`  |
| `status`       | `"a"`   |

And the following expressions:

| Expression                                 | Result     |
|--------------------------------------------|------------|
| `IF(myFlag, "On", "Off")`                  | `"Off"`    |
| `IF(numSales > 1000, "High", "Low")`       | `"High"`   |
| `IF(status == "a", "Active", "Suspended")` | `"Active"` |

[Return to top](#top)



## SWITCH

*Since: 3.3.0*

The SWITCH function provides a way to compare one value against a series of possible values and return a specific result based on the first matching value. It's similar to nested IF statements but can be more concise and easier to read for certain scenarios.

### Syntax

```
SWITCH(expression, value1, result1, [value2-N, result2-N, ...], [default])
```

### Parameters

| Name                                  | Description                                                                                        |
|---------------------------------------|----------------------------------------------------------------------------------------------------|
| expression                            | The value or expression that the function will compare against the provided list of values.        |
| value1, result1, value2, result2, ... | Pairs of values, where the function checks if the expression matches the first value in each pair. |
| default                               | An optional default value to be returned if no matching value is found.                            |


### Examples

Consider the following variables:

| Name           | Value   |
|----------------|---------|
| `weekday`      | `1`     |
| `country_code` | `"BRA"` |
| `status`       | `"x"`   |

And the following expressions:

| Expression                                                   | Result      |
|--------------------------------------------------------------|-------------|
| `SWITCH(weekday, 1, "Sunday", 2, "Monday", 3, "Tuesday")`    | `"Sunday"`  |
| `SWITCH(country_code, "DEU", "Germany", "BRA", "Brazil")`    | `"Brazil"`  |
| `SWITCH(status, "a", "Active", "s", "Suspended", "Unknown")` | `"Unknown"` |

[Return to top](#top)
