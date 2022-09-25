---
layout: default
title: Data types
parent: Concepts
nav_order: 2
---

## Data Types

EvalEx supports the following data types:

| Data Type       | Internal Representation           |
|-----------------|-----------------------------------|
| NUMBER          | java.math.BigDecimal              |
| BOOLEAN         | java.lang.Boolean                 |
| STRING          | java.lang.String                  |
| ARRAY           | java.util.List                    |
| STRUCTURE       | java.util.Map                     |
| EXPRESSION_NODE | com.ezylang.evalex.parser.ASTNode |

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

### ARRAY

Arrays are stored internally as a _java.util.List&lt;EvaluationValue&gt;_. When passed as a
variable, the list will be iterated and each entry will be converted using the data type conversion
rules.
So, for example, a list of double values will be converted to a list of _EvaluationValue_
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
