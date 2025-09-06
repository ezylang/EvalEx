---
layout: default
title: Data types
parent: Concepts
nav_order: 2
---

## Data Types

EvalEx supports the following data types:

| Data Type       | Internal Representation                    |
|-----------------|--------------------------------------------|
| NUMBER          | java.math.BigDecimal                       |
| BOOLEAN         | java.lang.Boolean                          |
| STRING          | java.lang.String                           |
| DATE_TIME       | java.time.Instant                          |
| DURATION        | java.time.Duration                         |
| ARRAY           | java.util.List                             |
| STRUCTURE       | java.util.Map                              |
| EXPRESSION_NODE | com.ezylang.evalex.parser.ASTNode          |
| BINARY[^1]      | java.lang.Object                           |
| NULL            | null                                       |
| UNDEFINED[^2]   | null, but returned as "logical null" value |

[^1]: Since 3.3.0
[^2]: Since 3.6.0

Data is stored in an _EvaluationValue_, which holds the value and the data type.

### NUMBER

Numbers are stored as _java.math.BigDecimal_ values and are either passed directly, or will
automatically be converted from one of the following Java data types:

- long
- int
- short
- byte
- double
- float
- java.math.BigInteger

Be careful when passing double or float values, as these have to be converted from their
floating-point arithmetic representation to a BigDecimal representation, which stores values a
signed decimal number of arbitrary precision with an associated scale.

### BOOLEAN

Booleans can be passed directly as variable values to an expression evaluation, or are the result of
a boolean operation or function.

If _STRING_ or _NUMBER_ values are used in boolean expressions, they are converted using the
following rules:

- If the value is a _NUMBER_, it is false if it equals to zero. All other values evaluate to true.
- If the value is a _STRING_, it is true if it equals to "true" (any case), false otherwise.

Consider this example, it will evaluate to true:

```java
Expression expression = new Expression("stringValue && numberValue")
    .with("stringValue", "True")
    .and("numberValue", 42);
```

### STRING

Any instance of _java.lang.CharSequence_ or _java.lang.Character_ will automatically be converted to
a _STRING_ datatype. Conversion will be done by invoking the _toString()_ method on the input
object.

By default, the string literal delimiter is the double quote character ("). You can also use both
`"` and `'` as string literal delimiters by changing the configuration. See
chapter [Configuration](../configuration/configuration.html) for details.

### DATE_TIME

Any instance of _java.time.Instant_, _java.time.LocalDate_, _java.time.LocalDateTime_, _java.time.ZoneDateTime_,
_java.time.OffsetDateTime_, _java.util.Date_ or _java.util.Calendar_, will automatically be converted to
a _DATE_TIME_ datatype. If the conversion requires a time zone and no time zone is given in the input object,
then the configured time zone is used.

### DURATION

Duration are stored as a _java.time.Duration_. The duration values are useful for calculations with _DATE_TIME_ values.

### ARRAY

Arrays are stored internally as a _java.util.List&lt;EvaluationValue&gt;_. When passed as a
variable, the list will be iterated and each entry will be converted using the data type conversion
rules. Instances of _java.util.List_ and Java arrays can be passed as parameters.
So, for example, a list or array of double values will be converted to a list of _EvaluationValue_
objects of type _NUMBER_, with an internal BigDecimal representation.

Arrays can hold mixed data types:

```java
List<Object> list = List.of(2.5, "Hello", true);

// prints "true" 
System.out.println(
    new Expression("list[2]").with("list", list).evaluate().getStringValue());
```

Array index starts at 0 with the first array element.

Arrays can hold other arrays as members, which can hold other arrays and so on:

```java
List<Object> list1 = List.of(1, 2, 3);
List<Object> list2 = List.of(4, 5, 6);
List<Object> sublist = List.of(100, 200, 300);
List<Object> list3 = List.of(7, 8, sublist);

List list = List.of(list1, list2, list3);

// prints 200
System.out.println(
    new Expression("list[2][2][1]").with("list", list).evaluate().getStringValue());
```

Arrays can also hold _STRUCTURE_ elements as entries.

### STRUCTURE

Structures are stored internally as a _java.util.Map&lt;String, EvaluationValue&gt;_. When passed as
a variable, the map entries will be iterated and each entry value will be converted using the data
type conversion rules.

Structures can hold other structures, which can form a tree like data structure.

Arrays and structures can be combined in any arbitrary way.

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

#### Structure elements containing spaces in name
If your structure has element names that contain spaces, you can use double quotes in the expression to access them.

```java
Map<String, Object> data = new HashMap<>();
data.put("property 1", 12345);

Expression expression = new Expression("data.\"property 1\"")
    .with("data", data);

BigDecimal result = expression.evaluate().getNumberValue();

System.out.println(result); // prints 12345
```

This also works with arrays.

```java
Map<String, Object> data = new HashMap<>();
data.put("property 1", Arrays.asList(1, 2, 3));

Expression expression = new Expression("data.\"property 1\"[1]")
    .with("data", data);

BigDecimal result = expression.evaluate().getNumberValue();

System.out.println(result); // prints 2
```

### EXPRESSION_NODE

A string expression is converted into an abstract syntax tree (AST), which represents the expression
structure and evaluation order. When an expression is evaluated, the evaluation starts at the lowest
tree (leaf) node and works its way up to the root node, which then gives the expression result
value.

The internal object for such a node is the _com.ezylang.evalex.parser.ASTNode_. It contains the
token associated to the node (e.g. a multiplication operator, or a function name) and has zero or
more children (the operator or function parameters).

By passing an _ASTNode_ and its substructure as a variable, this will be handled as a subtree and
will be evaluated when the node is evaluated.

With this, a _lazy evaluation_ is possible, only evaluating a node when it is desired (see the 
_IF()_ function for an example. Also, it allows the creation of sub-expressions.

The _Expression_ object has a method to create such sub-expressions, returning the root node of an
arbitrary string expression:

```java
Expression expression = new Expression("a*b");

ASTNode subExpression = expression.createExpressionNode("4+3");

EvaluationValue result = expression
    .with("a", 2)
    .and("b", subExpression)
    .evaluate();

System.out.println(result); // prints 14
```

Note that the above expression is not evaluated as "2 * 4 + 3", which would result in 11.
Instead, the sub-expression "4 + 3" is calculated first, when it comes to finding the value of the
variable _b_. Resulting in calculation of "2 * 7", which is 14.


### BINARY

A representation for an undefined (raw), non-null object that could not fit in any of the previous
data types.

This allows for special functions to handle any object type.

The binary data type is **disabled** by default and can be enabled by setting a dedicated property
in the [Configuration](../configuration/configuration.html).


### NULL

A representation for _null_ objects.

This allows the handling of nulls inside the expression itself (for example using the _IF()_ function), 
in case it can not be guaranteed that the passed variable values are not null before passing them.

```java
Expression expression = new Expression("if(name == null, "unknown", name)");

EvaluationValue result = expression
    .with("name", null)
    .evaluate();

System.out.println(result); // prints unknown
```

### UNDEFINED

A representation for undeclared variables or constants, when the lenient mode is enabled.

Differently from _NULL_, the _UNDEFINED_ data type provides some **logical nulls** (e.g.: `false` for _boolean_)
allowing for graceful evaluation of variables without an exception, like in the example below (where `b` is
not defined):

```java
Expression expression = new Expression("a || b");

EvaluationValue result = expression
    .with("a", false)
    .evaluate();

System.out.println(result); // returns false
```

The lenient mode is **disabled** by default and can be enabled by setting a dedicated property
in the [Configuration](../configuration/configuration.html).