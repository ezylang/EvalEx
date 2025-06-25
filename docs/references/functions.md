---
layout: default
title: Standard Functions
nav_order: 3
parent: References
---

## Standard Functions

Available through the _ExpressionConfiguration.StandardFunctionsDictionary_ constant:

### Basic Functions

| Name                  | Description                                                                                                                             |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| [ABS](#abs)           | Absolute (non-negative) value                                                                                                           |
| [AVERAGE](#average)   | Returns the average (arithmetic mean) of all parameters. If a parameter is of type _ARRAY_, the average of all elements is calculated.  |
| [CEILING](#ceiling)   | Rounds the given value to the nearest integer using the rounding mode CEILING                                                           |
| [COALESCE](#coalesce) | Returns the first non-null parameter, or NULL if all parameters are null                                                                |
| [FACT](#fact)         | Calculates the factorial of a base value                                                                                                |
| [FLOOR](#floor)       | Rounds the given value to the nearest integer using the rounding mode FLOOR                                                             |
| [IF](#if)             | Conditional evaluation function. Returns one value or another, depending on a given condition.                                          |
| [LOG](#log)           | The natural logarithm (base e) of a value                                                                                               |
| [LOG10](#log10)       | The base 10 logarithm of a value                                                                                                        |
| [MAX](#max)           | Returns the maximum value of all parameters. If a parameter is of type _ARRAY_, the maximum of all elements is calculated.              |
| [MIN](#min)           | Returns the minimum value of all parameters. If a parameter is of type _ARRAY_, the minimum of all elements is calculated.              |
| [NOT](#not)           | Boolean negation, implemented as a function (for compatibility)                                                                         |
| [RANDOM](#random)     | Produces a random value between 0 and 1                                                                                                 |
| [ROUND](#round)       | Rounds the given value to the specified scale, using the current rounding mode                                                          |
| [SQRT](#sqrt)         | Returns the square root of a given number                                                                                               |
| [SUM](#sum)           | Returns the sum of all parameters. If a parameter is of type _ARRAY_, the sum of all elements is calculated.                            |
| [SWITCH](#switch)     | Returns the result correponding to the first matching value in the specified expression or an optional default value if no match found. |

### String Functions

| Name                                | Description                                                                                                                       |
|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [STR_CONTAINS](#str_contains)       | Returns true if the string contains the substring (case-insensitive)                                                              |
| [STR_ENDS_WITH](#str_ends_with)     | Returns true if the string ends with the substring (case-sensitive)                                                               |
| [STR_FORMAT](#str_format)           | Returns a formatted string using the specified format string and arguments, using the configured locale                           |
| [STR_LEFT](#str_left)               | Returns the first n characters from the left of the given string                                                                  |
| [STR_LENGTH](#str_length)           | Returns the length of the string                                                                                                  |
| [STR_LOWER](#str_lower)             | Converts the given value to lower case                                                                                            |
| [STR_MATCHES](#str_matches)         | Returns true if the string matches the RegEx pattern                                                                              |
| [STR_RIGHT](#str_right)             | Returns the last n characters from the left of the given string                                                                   |
| [STR_SPLIT](#str_split)             | Splits the given string into an array, given a separator                                                                          |
| [STR_STARTS_WITH](#str_starts_with) | Returns true if the string starts with the substring (case-sensitive)                                                             |
| [STR_SUBSTRING](#str_substring)     | Returns a portion of the string from the specified start index up to the end index (or to the end of the string if not specified) |
| [STR_TRIM](#str_trim)               | Returns the given string with all leading and trailing spaces removed                                                             |
| [STR_UPPER](#str_upper)             | Converts the given value to upper case                                                                                            |

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

| Name                                                | Description                                                                                                                                                                                                                                                                        |
|-----------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DT_DATE_NEW](#dt_date_new)                         | Returns a new _DATE_TIME_ value with the given parameters (year, month, day, etc.). An optional time zone (string) can be specified, e.g. "Europe/Berlin", or "GMT+02:00". If no zone id is specified, the configured zone id is used.                                             |
| [DT_DATE_NEW(millis)](#dt_date_newmillis)           | Returns a new _DATE_TIME_ given the number of milliseconds from the Unix epoch (1970-01-01T00:00:00Z).                                                                                                                                                                             |
| [DT_DATE_PARSE](#dt_date_parse)                     | Converts the given string value to a date time value by using the optional time zone and formats. All formats are used until the first matching format. Without a format, the configured formats are used. Time zone can be NULL, the the configured time zone and locale is used. |
| [DT_DATE_FORMAT](#dt_date_format)                   | Formats the given date-time to a string using the given optional format and time zone. Without a format, the first configured format is used. The zone id defaults to the configured zone id.                                                                                      |
| [DT_DATE_TO_EPOCH](#dt_date_to_epoch)               | Converts the given value to epoch timestamp in millisecond.                                                                                                                                                                                                                        |
| [DT_DURATION_NEW](#dt_duration_new)                 | Returns a new DURATION value with the given parameters.                                                                                                                                                                                                                            |
| [DT_DURATION_PARSE](#dt_duration_parse)             | Converts the given ISO-8601 duration string representation to a duration value. E.g. "P2DT3H4M" parses 2 days, 3 hours and 4 minutes.                                                                                                                                              | 
| [DT_DURATION_FROM_MILLIS](#dt_duration_from_millis) | Returns a new DURATION value with the given milliseconds.                                                                                                                                                                                                                          |
| [DT_DURATION_TO_MILLIS](#dt_duration_to_millis)     | Converts the given duration to a milliseconds value.                                                                                                                                                                                                                               | 
| [DT_NOW](#dt_now)                                   | Produces a new DATE_TIME that represents the current moment in time.                                                                                                                                                                                                               |
| [DT_TODAY](#dt_today)                               | Produces a new DATE_TIME that represents the current date, at midnight (00:00). An optional time zone (string) can be specified, e.g. "America/Sao_Paulo", or "GMT-03:00". If no zone id is specified, the configured zone id is used.                                             |

---

# Basic Functions (detailed)

## ABS

The ABS function returns the absolute (non-negative) value of a given number.

### Syntax

```
ABS(value)
```

### Parameters

| Name  | Description                                         |
|-------|-----------------------------------------------------|
| value | The number from which to obtain the absolute value. |

### Examples

Consider the following variables:

| Name   | Value  |
|--------|--------|
| `x`    | `-5`   |
| `y`    | `10`   |

And the following expressions:

| Expression | Result |
|------------|--------|
| `ABS(x)`   | `5`    |
| `ABS(y)`   | `10`   |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## AVERAGE

*Since: 3.3.0*

The AVERAGE function calculates the arithmetic mean of a set of numbers. If any parameter is an array, the average of all its elements is calculated.

### Syntax

```
AVERAGE(value, [...])
```

### Parameters

| Name       | Description                                                        |
|------------|--------------------------------------------------------------------|
| value, ... | One or more values or arrays from which the average is calculated. |

### Examples

Consider the following variables:

| Name       | Value          |
|------------|----------------|
| `scores`   | `[70, 80, 90]` |
| `a`        | `85`           |
| `b`        | `95`           |

And the following expressions:

| Expression                   | Result |
|------------------------------|--------|
| `AVERAGE(scores)`            | `80`   |
| `AVERAGE(a, b)`              | `90`   |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## CEILING

The CEILING function rounds a given number up to the nearest integer using the rounding mode CEILING.

> See [Rounding Modes](../concepts/rounding.html#rounding-mode) for more information.

### Syntax

```
CEILING(value)
```

### Parameters

| Name  | Description                                         |
|-------|-----------------------------------------------------|
| value | The number to be rounded up to the nearest integer. |

### Examples

Consider the following expressions:

| Expression     | Result |
|----------------|--------|
| `CEILING(4.3)` | `5`    |
| `CEILING(2.7)` | `-2`   |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## COALESCE

*Since: 3.1.0*

The COALESCE function returns the first non-null parameter from a list of arguments, or `NULL` if all parameters are null.

### Syntax

```
COALESCE(value, [...])
```

### Parameters

| Name       | Description                         |
|------------|-------------------------------------|
| value, ... | One or more values to be evaluated. |

### Examples

Consider the following variables:

| Name     | Value     |
|----------|-----------|
| `a`      | `null`    |
| `b`      | `"hello"` |
| `c`      | `42`      |

And the following expressions:

| Expression            | Result    |
|-----------------------|-----------|
| `COALESCE(a, b, c)`   | `"hello"` |
| `COALESCE(a, null, c)`| `42`      |
| `COALESCE(a, null)`   | `null`    |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## FACT

The FACT function calculates the factorial of a base value, that is, the product of all positive integers from 1 up to that number.

### Syntax

```
FACT(base)
```

### Parameters

| Name | Description                                       |
|------|---------------------------------------------------|
| base | The number for which the factorial is calculated. |

### Examples

Consider the following expressions:

| Expression | Result |
|------------|--------|
| `FACT(5)`  | `120`  |
| `FACT(3)`  | `6`    |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## FLOOR

The FLOOR function rounds a given number down to the nearest integer using the rounding mode FLOOR.

> See [Rounding Modes](../concepts/rounding.html#rounding-mode) for more information.

### Syntax

```
FLOOR(value)
```

### Parameters

| Name  | Description                                           |
|-------|-------------------------------------------------------|
| value | The number to be rounded down to the nearest integer. |

### Examples

Consider the following expressions:

| Expression   | Result |
|--------------|--------|
| `FLOOR(4.7)` | `4`    |
| `FLOOR(2.4)` | `-3`   |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


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
| value_if_false | The value returned if the logical test is false. |


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

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## LOG

The LOG function returns the natural logarithm (base e) of a given value.

### Syntax

```
LOG(value)
```

### Parameters

| Name  | Description                                              |
|-------|----------------------------------------------------------|
| value | The number for which to calculate the natural logarithm. |

### Examples

Consider the following expressions:

| Expression   | Result              |
|--------------|---------------------|
| `LOG(2.718)` | `1`                 |
| `LOG(1)`     | `0`                 |
| `LOG(10)`    | `2.302585092994046` |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)

## LOG10

The LOG10 function returns the base 10 logarithm of a given value.

### Syntax

```
LOG10(value)
```

### Parameters

| Name  | Description                                              |
|-------|----------------------------------------------------------|
| value | The number for which to calculate the base 10 logarithm. |

### Examples

Consider the following expressions:

| Expression       | Result               |
|------------------|----------------------|
| `LOG10(100)`     | `2`                  |
| `LOG10(10)`      | `1`                  |
| `LOG10(2.12345)` | `0.3270420392943239` |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## MAX

The MAX function returns the maximum value of all parameters. If any parameter is an array, the maximum of all its elements is calculated.

### Syntax

```
MAX(value, [...])
```

### Parameters

| Name       | Description                                                         |
|------------|---------------------------------------------------------------------|
| value, ... | One or more numbers or arrays from which to find the maximum value. |

### Examples

Consider the following variables:

| Name      | Value       |
|-----------|-------------|
| `numbers` | `[3, 8, 5]` |
| `x`       | `7`         |
| `y`       | `4`         |

And the following expressions:

| Expression                 | Result |
|----------------------------|--------|
| `MAX(numbers)`             | `8`    |
| `MAX(x, y)`                | `7`    |
| `MAX(x, y, numbers)`       | `8`    |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## MIN

The MIN function returns the minimum value of all parameters. If any parameter is an array, the minimum of all its elements is calculated.

### Syntax

```
MIN(value, [...])
```

### Parameters

| Name       | Description                                                         |
|------------|---------------------------------------------------------------------|
| value, ... | One or more numbers or arrays from which to find the minimum value. |

### Examples

Consider the following variables:

| Name      | Value       |
|-----------|-------------|
| `numbers` | `[3, 8, 5]` |
| `x`       | `7`         |
| `y`       | `4`         |

And the following expressions:

| Expression            | Result |
|-----------------------|--------|
| `MIN(numbers)`        | `3`    |
| `MIN(x, y)`           | `4`    |
| `MIN(x, y, numbers)`  | `3`    |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## NOT

The NOT function performs a Boolean negation on a given value.

### Syntax

```
NOT(value)
```

### Parameters

| Name  | Description                                      |
|-------|--------------------------------------------------|
| value | The boolean value to be negated.                 |

### Examples

Consider the following variables:

| Name    | Value  |
|---------|--------|
| `flag`  | `true` |
| `x`     | `10`   |

And the following expressions:

| Expression   | Result  |
|--------------|---------|
| `NOT(flag)`  | `false` |
| `NOT(x < 5)` | `true`  |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## RANDOM

The RANDOM function produces a random value between 0 and 1.

### Syntax

```
RANDOM()
```

### Examples

Expressions that use RANDOM will produce varying results:

| Expression  | Result            |
|-------------|-------------------|
| `RANDOM()`  | `0.372` (example) |
| `RANDOM()`  | `0.847` (example) |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## ROUND

The ROUND function rounds a given value to the specified scale using the current rounding mode.

> See [Rounding Modes](../concepts/rounding.html#rounding-mode) for more information.

### Syntax

```
ROUND(value, scale)
```

### Parameters

| Name  | Description                                   |
|-------|-----------------------------------------------|
| value | The number to be rounded.                     |
| scale | The number of decimal places to round to.     |

### Examples

Consider the following variables:

| Name   | Value  |
|--------|--------|
| `num`  | `5.678`|

And the following expressions:

| Expression      | Result |
|-----------------|--------|
| `ROUND(num, 1)` | `5.7`  |
| `ROUND(num, 2)` | `5.68` |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## SQRT

The SQRT function calculates the square root of a given number.

> **Note:** This function uses the implementation from _The Java Programmers Guide To numerical Computing_ by Ronald Mak, 2002.

### Syntax

```
SQRT(value)
```

### Parameters

| Name  | Description                                        |
|-------|----------------------------------------------------|
| value | The number for which to calculate the square root. |

### Examples

Consider the following expressions:

| Expression  | Result |
|-------------|--------|
| `SQRT(16)`  | `4`    |
| `SQRT(9)`   | `3`    |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


## SUM

The SUM function calculates the total of a set of numbers. If any parameter is an array, the sum of all its elements is calculated.

### Syntax

```
SUM(value, [...])
```

### Parameters

| Name         | Description                                                   |
|--------------|---------------------------------------------------------------|
| value, [...] | One or more values or arrays from which to calculate the sum. |

### Examples

Consider the following variables:

| Name     | Value       |
|----------|-------------|
| `values` | `[3, 8, 5]` |
| `a`      | `10`        |
| `b`      | `15`        |

And the following expressions:

| Expression                | Result |
|---------------------------|--------|
| `SUM(values)`             | `16`   |
| `SUM(a, b)`               | `25`   |
| `SUM(a, b, values)`       | `41`   |

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


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

🔝 [Back to Basic Functions](#basic-functions) | 🔝 [Back to top](#top)


---

# String Functions (detailed)

## STR_CONTAINS

The `STR_CONTAINS` function checks whether a specified substring is present within a larger string. This comparison is **case-insensitive**, meaning it does not differentiate between uppercase and lowercase letters.

### Syntax

```
STR_CONTAINS(string, substring)
```

### Parameters

| Name      | Description                                                               |
|-----------|---------------------------------------------------------------------------|
| string    | The text in which you want to search for the presence of a substring.     |
| substring | The text to search for within the main string.                            |

### Examples

Consider the following variables:

| Name       | Value               |
|------------|---------------------|
| `title`    | `"Introduction"`    |
| `phrase`   | `"quick brown fox"` |

And the following expressions:

| Expression                                  | Result   |
|---------------------------------------------|----------|
| `STR_CONTAINS(title, "intro")`              | `true`   |
| `STR_CONTAINS(phrase, "BROWN")`             | `true`   |
| `STR_CONTAINS(phrase, "lazy")`              | `false`  |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_ENDS_WITH

*Since: 3.2.0*

The `STR_ENDS_WITH` function checks if a string ends with the specified substring. This comparison is **case-sensitive**.

### Syntax

```
STR_ENDS_WITH(string, substring)
```

### Parameters

| Name      | Description                                                |
|-----------|------------------------------------------------------------|
| string    | The text to be evaluated.                                  |
| substring | The text to check for at the end of the main string.       |

### Examples

Consider the following variables:

| Name     | Value             |
|----------|-------------------|
| `file`   | `"Report.pdf"`    |
| `word`   | `"sunflower"`     |

And the following expressions:

| Expression                                | Result  |
|-------------------------------------------|---------|
| `STR_ENDS_WITH(file, ".pdf")`             | `true`  |
| `STR_ENDS_WITH(word, "flower")`           | `true`  |
| `STR_ENDS_WITH(word, "Flower")`           | `false` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_FORMAT

*Since: 3.3.0*

The `STR_FORMAT` function returns a formatted string using the provided format string and one or more arguments.

The format respects the configured locale for number and date presentation.

> Learn how to change the locale in the [Configuration](../configuration/configuration.html) section.

### Syntax

```
STR_FORMAT(format[, argument, ...])
```

### Parameters

| Name      | Description                                                                     |
|-----------|---------------------------------------------------------------------------------|
| format    | A string format in the [Java Formatter syntax](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax). |
| arguments | One or more arguments referenced by the format specifiers in the format string. |

### Examples

Consider the following variables:

| Name         | Value     |
|--------------|-----------|
| `name`       | `"Maria"` |
| `count`      | `3`       |
| `price`      | `25.5`    |

And the following expressions:

| Expression                          | Locale  | Result            |
|-------------------------------------|---------|-------------------|
| `STR_FORMAT("Hello, %s!", name)`    | English | `"Hello, Maria!"` |
| `STR_FORMAT("%03.0f items", count)` | English | `"003 items"`     |
| `STR_FORMAT("Total: %.2f", price)`  | English | `"Total: 25.50"`  |
| `STR_FORMAT("Total: %.2f", price)`  | German  | `"Total: 25,50"`  |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_LEFT

*Since: 3.4.0*

The `STR_LEFT` function extracts the first *n* characters from the start (left side) of a given string.

### Syntax

```
STR_LEFT(value, n)
```

### Parameters

| Name  | Description                                       |
|-------|---------------------------------------------------|
| value | The original string from which to extract text.   |
| n     | The number of characters to return from the left. |

### Examples

Consider the following variables:

| Name        | Value         |
|-------------|---------------|
| `city`      | `"Barcelona"` |
| `language`  | `"Spanish"`   |

And the following expressions:

| Expression               | Result      |
|--------------------------|-------------|
| `STR_LEFT(city, 3)`      | `"Bar"`     |
| `STR_LEFT(language, 16)` | `"Spanish"` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_LENGTH

*Since: 3.4.0*

The `STR_LENGTH` function returns the total number of characters in a string, including spaces and punctuation.

### Syntax

```
STR_LENGTH(string)
```

### Parameters

| Name   | Description                                |
|--------|--------------------------------------------|
| string | The text whose length you want to measure. |

### Examples

Consider the following expressions:

| Expression                     | Result |
|--------------------------------|--------|
| `STR_LENGTH("abc")`            | `3`    |
| `STR_LENGTH("Hello, EvalEx!")` | `14`   |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_LOWER

The `STR_LOWER` function converts all characters in a given string to lowercase.

### Syntax

```
STR_LOWER(value)
```

### Parameters

| Name  | Description                      |
|-------|----------------------------------|
| value | The text to be converted.        |

### Examples

Consider the following expressions:

| Expression               | Result     |
|--------------------------|------------|
| `STR_LOWER("BR")`        | `"br"`     |
| `STR_LOWER("ABC1-abc2")` | `"abc1-abc2"` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_MATCHES

*Since: 3.4.0*

The `STR_MATCHES` function checks whether the given string matches a specific [regular expression pattern](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/regex/Pattern.html#sum).

### Syntax

```
STR_MATCHES(string, pattern)
```

### Parameters

| Name    | Description                                  |
|---------|----------------------------------------------|
| string  | The text to test against the regex pattern.  |
| pattern | A regular expression used for matching.      |

### Examples

| Name      | Value            |
|-----------|------------------|
| `message` | `"Hello World`"  |
| `email`   | `"test@abc.com"` |
| `zip`     | `"12345-678"`    |

| Expression                                 | Result  |
|--------------------------------------------|---------|
| `STR_MATCHES(message, ".*World")`          | `true`  |
| `STR_MATCHES(email, "\\S+@\\S+\\.\\S+")`   | `true`  |
| `STR_MATCHES(zip, "^\\d{5}-\\d{3}$")`      | `true`  |
| `STR_MATCHES(zip, "^\\d{8}$")`             | `false` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_RIGHT

*Since: 3.4.0*

The `STR_RIGHT` function extracts the last *n* characters from a string.

### Syntax

```
STR_RIGHT(value, n)
```

### Parameters

| Name  | Description                                      |
|-------|--------------------------------------------------|
| value | The original string to extract from.             |
| n     | The number of characters to return from the end. |

### Examples

Consider the following variables:

| Name       | Value           |
|------------|-----------------|
| `message`  | `"Hello World"` |
| `code`     | `"HTTP/404"`    |

And the following expressions:

| Expression                     | Result    |
|--------------------------------|-----------|
| `STR_RIGHT(message, 5)`        | `"World"` |
| `STR_RIGHT(code, 3)`           | `"404"`  |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_SPLIT

*Since: 3.5.0*

The `STR_SPLIT` function splits a string into an array of substrings based on the provided separator.

### Syntax

```
STR_SPLIT(string, separator)
```

### Parameters

| Name      | Description                                           |
|-----------|-------------------------------------------------------|
| string    | The original text to split.                           |
| separator | A delimiter string used to divide the input string.   |

### Examples

Consider the following variables:

| Name      | Value                    |
|-----------|--------------------------|
| `colors`  | `"red,green,blue"`       |
| `path`    | `"home/user/documents"`  |

And the following expressions:

| Expression                         | Result                          |
|------------------------------------|---------------------------------|
| `STR_SPLIT(colors, ",")`           | `["red", "green", "blue"]`      |
| `STR_SPLIT(path, "/")`             | `["home", "user", "documents"]` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_STARTS_WITH

*Since: 3.2.0*

The `STR_STARTS_WITH` function checks whether a string begins with the specified substring. This comparison is **case-sensitive**.

### Syntax

```
STR_STARTS_WITH(string, substring)
```

### Parameters

| Name      | Description                                               |
|-----------|-----------------------------------------------------------|
| string    | The main text you want to examine.                        |
| substring | The expected prefix to test at the beginning of `string`. |

### Examples

Consider the following variables:

| Name    | Value        |
|---------|--------------|
| `title` | `"DataSet"`  |
| `code`  | `"ERR-2025"` |

And the following expressions:

| Expression                       | Result  |
|----------------------------------|---------|
| `STR_STARTS_WITH(title, "Data")` | `true`  |
| `STR_STARTS_WITH(title, "data")` | `false` |
| `STR_STARTS_WITH(code, "ERR")`   | `true`  |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_SUBSTRING

*Since: 3.4.0*

The `STR_SUBSTRING` function extracts a portion of a string starting from a given index and optionally ending at another index. If the end is not specified, it returns the substring until the end of the string.

### Syntax

```
STR_SUBSTRING(string, start[, end])
```

### Parameters

| Name   | Description                                                                 |
|--------|-----------------------------------------------------------------------------|
| string | The original string from which to extract a section.                        |
| start  | The starting index (inclusive). Indexing begins at 0.                       |
| end    | (Optional) The ending index (exclusive). Defaults to the end of the string. |

### Examples

Consider the following variables:

| Name     | Value           |
|----------|-----------------|
| `text`   | `"Expression"`  |
| `id`     | `"AB123456"`    |

And the following expressions:

| Expression                  | Result     |
|-----------------------------|------------|
| `STR_SUBSTRING(text, 0, 4)` | `"Expr"`   |
| `STR_SUBSTRING(text, 5)`    | `"ssion"`  |
| `STR_SUBSTRING(id, 2, 6)`   | `"1234"`   |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_TRIM

*Since: 3.3.0*

The `STR_TRIM` function removes all leading and trailing whitespace from a given string.

### Syntax

```
STR_TRIM(string)
```

### Parameters

| Name   | Description                             |
|--------|-----------------------------------------|
| string | The text to be trimmed of whitespace.   |

### Examples

Consider the following variables:

| Name     | Value                    |
|----------|--------------------------|
| `input1` | `"  Hello world  "`      |
| `input2` | `"\tTabbed text\t"`      |

And the following expressions:

| Expression          | Result          |
|---------------------|-----------------|
| `STR_TRIM(input1)`  | `"Hello world"` |
| `STR_TRIM(input2)`  | `"Tabbed text"` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)


## STR_UPPER

The `STR_UPPER` function converts all characters in a given string to uppercase.

### Syntax

```
STR_UPPER(value)
```

### Parameters

| Name  | Description                      |
|-------|----------------------------------|
| value | The text to be converted.        |

### Examples

Consider the following expressions:

| Expression                    | Result     |
|-------------------------------|------------|
| `STR_UPPER("abc")`            | `"ABC"`    |
| `STR_UPPER("EvalEx")`         | `"EVALEX"` |

🔝 [Back to String Functions](#string-functions) | 🔝 [Back to top](#top)

---

# Date Time Functions (detailed)

## DT_DATE_NEW

The `DT_DATE_NEW` function creates a new `DATE_TIME` value using the specified date components, with optional time, sub-second precision, and time zone. If no time zone is provided, the configured system default is used.

### Syntax

```
DT_DATE_NEW(year, month, day [, hour] [, minute] [, second] [, nanos] [, zoneId])
```

### Parameters

| Name     | Description                                                                               |
|----------|------------------------------------------------------------------------------------------ |
| year     | The year (e.g. 2025).                                                                     |
| month    | The month number (1 = January, 12 = December).                                            |
| day      | The day of the month.                                                                     |
| hour     | *(Optional)* Hour of the day (0–23).                                                      |
| minute   | *(Optional)* Minute of the hour (0–59).                                                   |
| second   | *(Optional)* Second of the minute (0–59).                                                 |
| nanos    | *(Optional)* Nanoseconds (0–999,999,999).                                                 |
| zoneId   | *(Optional)* Time zone identifier as a string (e.g. `"Europe/Berlin"`, or `"GMT+02:00"`). |

### Examples

These examples illustrate how different combinations of date, time, and zone components produce distinct `DATE_TIME` results.

| Expression                                                                 | Result (example)                           |
|----------------------------------------------------------------------------|--------------------------------------------|
| `DT_DATE_NEW(2025, 6, 15)`                                                 | `2025-06-15T00:00:00Z`*                    |
| `DT_DATE_NEW(2025, 6, 15, 9, 30)`                                          | `2025-06-15T09:30:00Z`*                    |
| `DT_DATE_NEW(2025, 6, 15, "Europe/Berlin")`                                | `2025-06-15T00:00:00+02:00[Europe/Berlin]` |
| `DT_DATE_NEW(2025, 6, 15, 9, 30, 0, 0, "Europe/Berlin")`                   | `2025-06-15T09:30:00+02:00[Europe/Berlin]` |

\* Timezone and precision may vary depending on your system configuration.

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DATE_NEW(millis)

The `DT_DATE_NEW` function can also create a `DATE_TIME` value by specifying a number of milliseconds since the Unix epoch (`1970-01-01T00:00:00Z`).

### Syntax

```
DT_DATE_NEW(millis)
```

### Parameters

| Name   | Description                                                                           |
|--------|---------------------------------------------------------------------------------------|
| millis | The number of milliseconds since `1970-01-01T00:00:00Z`. Must be a positive `NUMBER`. |

### Examples

Use this form when working with timestamp values or durations that represent time since the epoch.

| Expression                   | Result (example)*       |
|------------------------------|-------------------------|
| `DT_DATE_NEW(0)`             | `1970-01-01T00:00:00Z`  |
| `DT_DATE_NEW(1489227300000)` | `2017-03-11T10:15:00Z`  |

\* Examples considering UTC as system time zone.

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DATE_PARSE

The `DT_DATE_PARSE` function converts a string into a `DATE_TIME` value by attempting to match it against one or more date-time formats until success. Optionally, a specific time zone may also be provided. If no format is supplied, the system's configured date/time formats will be used. If the zone is omitted or `NULL`, the system’s default zone and locale apply.

### Syntax

```
DT_DATE_PARSE(value [, zoneId] [, format, ...])
```

### Parameters

| Name    | Description                                                                                          |
|---------|------------------------------------------------------------------------------------------------------|
| value   | The text to be parsed into a date/time. Must be of type `STRING`.                                    |
| zoneId  | *(Optional)* A time zone identifier string (e.g. `"Europe/Berlin"`, `"UTC"`, or `"GMT-03:00"`).      |
| format  | *(Optional)* One or more custom date-time format strings (e.g. `"yyyy-MM-dd HH:mm"`, `"dd/MM/yyyy"`).|

### Examples

These examples demonstrate how different formats and zone configurations influence how the date string is interpreted.

| Expression                                                                | Result (example)                           |
|---------------------------------------------------------------------------|--------------------------------------------|
| `DT_DATE_PARSE("2025-06-16T17:41:00Z")`                                   | `2025-06-16T17:41:00Z`                     |
| `DT_DATE_PARSE("16/06/2025 17:41", "Europe/Lisbon", "dd/MM/yyyy HH:mm")`  | `2025-06-16T17:41:00+01:00[Europe/Lisbon]` |
| `DT_DATE_PARSE("06-16-2025", "MM-dd-yyyy")`                               | `2025-06-16T00:00:00Z` (system zone)       |

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DATE_FORMAT

The `DT_DATE_FORMAT` function formats a given `DATE_TIME` value into a string. You can optionally supply a specific format pattern and time zone. If omitted, the system’s configured default format and time zone are used.

### Syntax

```
DT_DATE_FORMAT(value [, format] [, zoneId])
```

### Parameters

| Name    | Description                                                                                   |
|---------|-----------------------------------------------------------------------------------------------|
| value   | The `DATE_TIME` value to format.                                                              |
| format  | *(Optional)* Format string following date/time pattern syntax (e.g. `"yyyy-MM-dd HH:mm:ss"`). |
| zoneId  | *(Optional)* Time zone identifier string to use (e.g. `"UTC"`, `"America/Chicago"`).          |

### Examples

These examples show how various combinations of formats and zones affect the resulting string. 

>**Note:** The function [DT_DATE_NEW](#dt_date_new) is being used to produce `DATE_TIME` values for the format operations.

| Expression                                                                                  | Result (example)*           |
|---------------------------------------------------------------------------------------------|-----------------------------|
| `DT_DATE_FORMAT(DT_DATE_NEW(2019, 6, 12, 18, 10))`                                          | `"2019-06-12T18:10:00Z"`    |
| `DT_DATE_FORMAT(DT_DATE_NEW(2019, 6, 12, 21, 10), "dd/MM/yyyy HH:mm", "America/Sao_Paulo")` | `"12/06/2019 18:10"`        |
| `DT_DATE_FORMAT(DT_DATE_NEW(2019, 6, 12), "EEEE, MMM d, yyyy")`                             | `"Wednesday, Jun 12, 2019"` |

\* Examples considering UTC as system time zone.

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DATE_TO_EPOCH

The `DT_DATE_TO_EPOCH` function converts a `DATE_TIME` value to the number of milliseconds elapsed since the Unix epoch (`1970-01-01T00:00:00Z`).

### Syntax

```
DT_DATE_TO_EPOCH(value)
```

### Parameters

| Name   | Description                                       |
|--------|---------------------------------------------------|
| value  | A `DATE_TIME` value to convert into epoch millis. |

### Examples

These examples demonstrate how this function turns time values into numeric timestamps.

>**Note:** The function [DT_DATE_NEW](#dt_date_new) is being used to produce `DATE_TIME` values for the operations.

| Expression                                            | Result (example)* |
|-------------------------------------------------------|-------------------|
| `DT_DATE_TO_EPOCH(DT_DATE_NEW(1970, 1, 1))`           | `0`               |
| `DT_DATE_TO_EPOCH(DT_DATE_NEW(2022, 3, 19, 10, 15))`  | `1647684900000`   |
| `DT_DATE_TO_EPOCH(DT_DATE_NEW(1969, 12, 31, 23, 59))` | `-60000`          |

\* Examples considering UTC as system time zone.

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DURATION_NEW

The `DT_DURATION_NEW` function constructs a `DURATION` value using the specified number of days, with optional time-based components such as hours, minutes, seconds, and nanoseconds.

### Syntax

```
DT_DURATION_NEW(days [, hours] [, minutes] [, seconds] [, millis] [, nanos])
```

### Parameters

| Name     | Description                                             |
|----------|---------------------------------------------------------|
| days     | The number of days. Required.                           |
| hours    | *(Optional)* Number of hours to include.                |
| minutes  | *(Optional)* Number of minutes to include.              |
| seconds  | *(Optional)* Number of seconds to include.              |
| millis   | *(Optional)* Milliseconds (1/1,000 of a second).        |
| nanos    | *(Optional)* Nanoseconds (1/1,000,000,000 of a second). |

> **Note:** Although both `millis` and `nanos` are accepted, it’s generally recommended to use *either* one or the other to represent sub-second precision. If you provide nanoseconds, it should capture the full fractional portion, and `millis` should be set to `0` to avoid double counting.

### Examples

These examples show how duration values can range from whole days to finely tuned time spans.

| Expression                                  | Result           |
|---------------------------------------------|------------------|
| `DT_DURATION_NEW(5)`                        | `PT120H`         |
| `DT_DURATION_NEW(0, 1, 15)`                 | `PT1H15M`        |
| `DT_DURATION_NEW(1, 0, 0, 30)`              | `PT24H30S`       |
| `DT_DURATION_NEW(0, 0, 0, 0, 999)`          | `PT0.999S`       |
| `DT_DURATION_NEW(0, 0, 0, 0, 0, 999999999)` | `PT0.999999999S` |

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DURATION_PARSE

The `DT_DURATION_PARSE` function converts an ISO-8601 compliant duration string into a `DURATION` value. This format can represent combinations of days, hours, minutes, seconds, and more using a standardized pattern.

### Syntax

```
DT_DURATION_PARSE(value)
```

### Parameters

| Name   | Description                                                                         |
|--------|-------------------------------------------------------------------------------------|
| value  | An ISO-8601 duration string (e.g. `"P2DT3H4M"` for 2 days, 3 hours, and 4 minutes). |

### Examples

These examples highlight how standard duration strings are interpreted and parsed into precise `DURATION` values.

| Expression                      | Result    |
|---------------------------------|-----------|
| `DT_DURATION_PARSE("P1D")`      | `PT24H`   |
| `DT_DURATION_PARSE("PT2H30M")`  | `PT2H30M` |
| `DT_DURATION_PARSE("P2DT3H4M")` | `PT51H4M` |
| `DT_DURATION_PARSE("PT0.75S")`  | `PT0.75S` |

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DURATION_FROM_MILLIS

The `DT_DURATION_FROM_MILLIS` function creates a `DURATION` value from the number of milliseconds provided. This is useful when handling timestamp or elapsed-time data that’s already in millisecond precision.

### Syntax

```
DT_DURATION_FROM_MILLIS(millis)
```

### Parameters

| Name   | Description                                     |
|--------|-------------------------------------------------|
| millis | The number of milliseconds as a `NUMBER` value. |

### Examples

These examples convert raw millisecond values into proper `DURATION` values.

| Expression                      | Result          |
|---------------------------------|-----------------|
| `DT_DURATION_FROM_MILLIS(60000)`| `PT1M`          |
| `DT_DURATION_FROM_MILLIS(0)`    | `PT0S`          |
| `DT_DURATION_FROM_MILLIS(1234)` | `PT1.234S`      |

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_DURATION_TO_MILLIS

The `DT_DURATION_TO_MILLIS` function returns the number of milliseconds in a given `DURATION` value. It's commonly used to serialize durations into numeric formats for storage or computation.

### Syntax

```
DT_DURATION_TO_MILLIS(duration)
```

### Parameters

| Name      | Description                                  |
|-----------|----------------------------------------------|
| duration  | A `DURATION` value to be converted to millis.|

### Examples

Here are some sample conversions showing how durations get flattened into exact millisecond quantities.

>**Note:** The function [DT_DURATION_PARSE](#dt_duration_parse) is being used to produce `DURATION` values for the operations.

| Expression                                         | Result  |
|----------------------------------------------------|---------|
| `DT_DURATION_TO_MILLIS(DT_DURATION_PARSE("PT1S"))` | `1000`  |
| `DT_DURATION_TO_MILLIS(DT_DURATION_PARSE("PT1M"))` | `60000` |

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_NOW

The `DT_NOW` function returns a `DATE_TIME` value representing the exact current instant, perfect for logging, comparisons, or triggering time-based events.

### Syntax

```
DT_NOW()
```

### Examples

Consider the following variable:

| Name      | Value                                   |
|-----------|-----------------------------------------|
| `dueDate` | `2025-05-31T23:59:59.999Z` (`DATETIME`) |

Here are some expressions. The first one produces a `DATE_TIME` value. The second one uses the result to compare against another `DATE_TIME` using the [IF](#if) function.

| Expression                                 | Result (example)            |
|--------------------------------------------|-----------------------------|
| `DT_NOW()`                                 | `2025-06-25T10:47:26.234Z`* |
| `IF(dueDate < DT_NOW(), "due", "overdue")` | `"overdue"`                 |

\* Example considering UTC as system time zone

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)


## DT_TODAY

The `DT_TODAY` function returns a `DATE_TIME` representing the current date with time set to midnight (`00:00`). You may optionally provide a time zone; otherwise, the system's configured zone is used.

### Syntax

```
DT_TODAY([zoneId])
```

### Parameters

| Name    | Description                                                                             |
|---------|-----------------------------------------------------------------------------------------|
| zoneId  | *(Optional)* Time zone identifier string (e.g. `"Africa/Nairobi"`, or `"GMT+03:00"`) |

### Examples

Use this when you need a reference to today’s date without the current time component.

| Expression                       | Result (example)            |
|----------------------------------|-----------------------------|
| `DT_TODAY()`                     | `2025-06-25T00:00:00Z`*     |
| `DT_TODAY("Australia/Canberra")` | `2025-06-25T00:00:00+10:00` |

\* Example considering UTC as system time zone

🔝 [Back to Date Time Functions](#date-time-functions) | 🔝 [Back to top](#top)
