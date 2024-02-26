---
layout: default
title: Welcome
nav_order: 1
---

EvalEx is a handy expression evaluator for Java, that allows to parse and evaluate expression strings.

## Key Features:

- Supports numerical, boolean, string, date time, duration, array and structure expressions, operations and variables.
- Array and structure support: Arrays and structures can be mixed, building arbitrary data
  structures.
- Supports the NULL datatype.
- Uses BigDecimal for numerical calculations.
- MathContext and number of decimal places can be configured, with optional automatic rounding.
- No dependencies to external libraries.
- Easy integration into existing systems to access data.
- Predefined boolean and mathematical operators.
- Predefined mathematical, boolean and string functions.
- Custom functions and operators can be added.
- Functions can be defined with a variable number of arguments (see MIN, MAX and SUM functions).
- Supports hexadecimal and scientific notations of numbers.
- Supports implicit multiplication, e.g. 2x or (a+b)(a-b) or 2(x-y) which equals to (a+b)\*(a-b) or 2\*(
  x-y)
- Lazy evaluation of function parameters (see the IF function) and support of sub-expressions.
- Requires minimum Java version 11.

## Documentation

The full documentation for EvalEx can be found
on [GitHub Pages](https://ezylang.github.io/EvalEx/)

## Discussion

For announcements, questions and ideas visit
the [Discussions area](https://github.com/ezylang/EvalEx/discussions).

## Download / Including

You can download the binaries, source code and JavaDoc jars from
[Maven Central](https://central.sonatype.com/artifact/com.ezylang/EvalEx).\
You will find there also copy/paste templates for including EvalEx in your project with build
systems like Maven or Gradle.

### Maven

To include it in your Maven project, add the dependency to your pom. For example:

```xml
<dependencies>
    <dependency>
      <groupId>com.ezylang</groupId>
      <artifactId>EvalEx</artifactId>
      <version>3.1.2</version>
    </dependency>
</dependencies>
```

### Gradle

If you're using gradle add the dependencies to your project's app build.gradle:

```gradle
dependencies {
    compile 'com.ezylang:EvalEx:3.1.2'
}
```

## Examples

### A simple example, that shows how it works in general:

```java
Expression expression = new Expression("1 + 2 / (4 * SQRT(4))");

EvaluationValue result = expression.evaluate();

System.out.println(result.getNumberValue()); // prints 1.25
```

### Variables can be specified in the expression and their values can be passed for evaluation:

```java
Expression expression = new Expression("(a + b) * (a - b)");

EvaluationValue result = expression
    .with("a", 3.5)
    .and("b", 2.5)
    .evaluate();

System.out.println(result.getNumberValue()); // prints 6.00
```

### Values can be passed in a map

Instead of specifying the variable values one  by one, they can be set by defining a map with names and values and then
passing it to the _withValues()_ method:

The data conversion of the passed values will automatically be performed through a customizable converter.

It is also possible to configure a custom data accessor to read and write values.

```java
Expression expression = new Expression("a+b+c");

Map<String, Object> values = new HashMap<>();
values.put("a", true);
values.put("b", " : ");
values.put("c", 24.7);

EvaluationValue result = expression.withValues(values).evaluate();

System.out.println(result.getStringValue()); // prints "true : 24.7"
```

See chapter [Data Types](https://ezylang.github.io/EvalEx/concepts/datatypes.html) for details on the conversion.

Another option to have EvalEx use your data is to define a custom data accessor.

See chapter [Data Access](https://ezylang.github.io/EvalEx/customization/data_access.html) for details.


### Boolean expressions produce a boolean result:

```java
Expression expression = new Expression("level > 2 || level <= 0");

EvaluationValue result = expression
    .with("level", 3.5)
    .evaluate();

System.out.println(result.getBooleanValue()); // prints true
```

### Like in Java, strings and text can be mixed:

```java
Expression expression = new Expression("\"Hello \" + name + \", you are \" + age")
    .with("name","Frank")
    .and("age",38);

System.out.println(expression.evaluate().getStringValue()); // prints Hello Frank, you are 38
```

### Arrays (also multidimensional) are supported and can be passed as Java _Lists_.

See the [Documentation](https://ezylang.github.io/EvalEx/concepts/datatypes.html#array)
for more details.

```java
Expression expression = new Expression("values[i-1] * factors[i-1]");

EvaluationValue result = expression
    .with("values", List.of(2, 3, 4))
    .and("factors", List.of(2, 4, 6))
    .and("i", 1)
    .evaluate();

System.out.println(result.getNumberValue()); // prints 4
```

### Structures are supported and can be passed as Java _Maps_.

Arrays and Structures can be combined to build arbitrary data structures. See
the [Documentation](https://ezylang.github.io/EvalEx/concepts/datatypes.html#structure)
for more details.

```java
Map<String, Object> order = new HashMap<>();
order.put("id", 12345);
order.put("name", "Mary");

Map<String, Object> position = new HashMap<>();
position.put("article", 3114);
position.put("amount", 3);
position.put("price", new BigDecimal("14.95"));

order.put("positions", List.of(position));

Expression expression = new Expression("order.positions[x].amount * order.positions[x].price")
    .with("order", order)
    .and("x", 0);

BigDecimal result = expression.evaluate().getNumberValue();

System.out.println(result); // prints 44.85
```

### Calculating with date-time and duration

Date-tme and duration values are supported. There are functions to create, parse and format these values.
Additionally, the plus and minus operators can be used to e.g. add or subtract durations, or to calculate the
difference between two dates:

```java
Instant start = Instant.parse("2023-12-05T11:20:00.00Z");
Instant end = Instant.parse("2023-12-04T23:15:30.00Z");

Expression expression = new Expression("start - end");
EvaluationValue result = expression
        .with("start", start)
        .and("end", end)
        .evaluate();
System.out.println(result); // will print "EvaluationValue(value=PT12H4M30S, dataType=DURATION)"
```
See the [Documentation](https://ezylang.github.io/EvalEx/concepts/date_time_duration.html) for more details.

## EvalEx-big-math

[Big-math](https://github.com/eobermuhlner/big-math) is a library by Eric Obermühlner. It provides
advanced Java BigDecimal math functions using an arbitrary precision.

[EvalEx-big-math](https://github.com/ezylang/EvalEx-big-math) adds the advanced math functions from
big-math to EvalEx.

## Author and License

Copyright 2012-2023 by Udo Klimaschewski

**Thanks to all who contributed to this
project: [Contributors](https://github.com/ezylang/EvalEx/graphs/contributors)**

The software is licensed under the Apache License, Version 2.0 (
see [LICENSE](https://raw.githubusercontent.com/ezylang/EvalEx/main/LICENSE) file).

* The *power of* operator (^) implementation was copied
  from [Stack Overflow](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java)
  Thanks to Gene Marin
* The SQRT() function implementation was taken from the
  book [The Java Programmers Guide To numerical Computing](http://www.amazon.de/Java-Number-Cruncher-Programmers-Numerical/dp/0130460419) (
  Ronald Mak, 2002)
