EvalEx - Java Expression Evaluator
==========

### Introduction

EvalEx is a handy expression evaluator for Java, that allows to evaluate simple mathematical and boolean expressions.

Key Features:
- Uses BigDecimal for calculation and result
- Single class implementation, very compact
- No dependencies to external libraries
- Precision and rounding mode can be set
- Supports variables
- Standard boolean and mathematical operators
- Standard basic mathematical and boolean functions
- Custom functions and operators can be added at runtime

### Examples

````java
 BigDecimal result = null;
 
 Expression expression = new Expression("1+1/3");
 result = expression.eval():
 expression.setPrecision(2);
 result = expression.eval():
 
 result = new Expression("(3.4 + -4.1)/2").eval();
 
 result = new Expression("SQRT(a^2 + b^2").with("a","2.4").and("b","9.253").eval();
 
 BigDecimal a = new BigDecimal("2.4");
 BigDecimal b = new BigDecimal("9.235");
 result = new Expression("SQRT(a^2 + b^2").with("a",a).and("b",b).eval();
 
 result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();
 
 result = new Expression("random() > 0.5").eval();

 result = new Expression("not(x<7 || sqrt(max(x,9)) <= 3))").with("x","22.9").eval();
````

### Supported Operators
<table>
  <tr><th>Mathematical Operators</th></tr>
  <tr><th>Operator</th><th>Description</th></tr>
  <tr><td>+</td><td>Additive operator</td></tr>
  <tr><td>-</td><td>Subtraction operator</td></tr>
  <tr><td>*</td><td>Multiplication operator</td></tr>
  <tr><td>/</td><td>Division operator</td></tr>
  <tr><td>%</td><td>Remainder operator (Modulo)</td></tr>
  <tr><td>^</td><td>Power operator</td></tr>
</table>

<table>
  <tr><th>Boolean Operators<sup>*</sup></th></tr>
  <tr><th>Operator</th><th>Description</th></tr>
  <tr><td>=</td><td>Equals</td></tr>
  <tr><td>==</td><td>Equals</td></tr>
  <tr><td>!=</td><td>Not equals</td></tr>
  <tr><td>&lt;&gt;</td><td>Not equals</td></tr>
  <tr><td>&lt;</td><td>Less than</td></tr>
  <tr><td>&lt;=</td><td>Less than or equal to</td></tr>
  <tr><td>&gt;</td><td>Greater than</td></tr>
  <tr><td>&gt;=</td><td>Greater than or equal to</td></tr>
  <tr><td>&amp;&amp;</td><td>Boolean and</td></tr>
  <tr><td>||</td><td>Boolean or</td></tr>
</table>
*Boolean operators result always in a BigDecimal value of 1 or 0 (zero). Any non-zero value is treated as a _true_ value. Boolean _not_ is implemented by a function.

### Supported Functions
<table>
  <tr><th>Function<sup>*</sup></th><th>Description</th></tr>
  <tr><td>NOT(<i>expression</i>)</td><td>Boolean negation, 1 (means true) if the expression is not zero</td></tr>
  <tr><td>RANDOM()</td><td>Produces a random number between 0 and 1</td></tr>
  <tr><td>MIN(<i>e1</i>,<i>e2</i>)</td><td>Returns the smaller of both expressions</td></tr>
  <tr><td>MAX(<i>e1</i>,<i>e2</i>)</td><td>Returns the bigger of both expressions</td></tr>
  <tr><td>ABS(<i>expression</i>)</td><td>Returns the absolute (non-negative) value of the expression</td></tr>
  <tr><td>ROUND(<i>expression</i>,precision)</td><td>Rounds a value to a certain number of digits, uses the current rounding mode</td></tr>
  <tr><td>FLOOR(<i>expression</i>)</td><td>Rounds the value down to the nearest integer</td></tr>
  <tr><td>CEILING(<i>expression</i>)</td><td>Rounds the value up to the nearest integer</td></tr>
  <tr><td>LOG(<i>expression</i>)</td><td>Returns the natural logarithm (base e) of an expression</td></tr>
  <tr><td>SQRT(<i>expression</i>)</td><td>Returns the square root of an expression</td></tr>
  <tr><td>SIN(<i>expression</i>)</td><td>Returns the trigonometric sine of an angle (in degrees)</td></tr>
  <tr><td>COS(<i>expression</i>)</td><td>Returns the trigonometric cosine of an angle (in degrees)</td></tr>
  <tr><td>TAN(<i>expression</i>)</td><td>Returns the trigonometric tangens of an angle (in degrees)</td></tr>
  <tr><td>SINH(<i>expression</i>)</td><td>Returns the hyperbolic sine of a value</td></tr>
  <tr><td>COSH(<i>expression</i>)</td><td>Returns the hyperbolic cosine of a value</td></tr>
  <tr><td>TANH(<i>expression</i>)</td><td>Returns the hyperbolic tangens of a value</td></tr>
  <tr><td>RAD(<i>expression</i>)</td><td>Converts an angle measured in degrees to an approximately equivalent angle measured in radians</td></tr>
  <tr><td>DEG(<i>expression</i>)</td><td>Converts an angle measured in radians to an approximately equivalent angle measured in degrees</td></tr>
</table>
*Functions names are case insensitive.

### Supported Constants
<table>
  <tr><th>Constant</th><th>Description</th></tr>
  <tr><td>PI</td><td>The value of <i>PI</i>, exact to 100 digits</td></tr>
  <tr><td>TRUE</td><td>The value one</td></tr>
  <tr><td>FALSE</td><td>The value zero</td></tr>
</table>

### Add Custom Operators

Custom operators can be added easily, simply create an instance of `Expression.Operator` and add it to the expression.
Parameters are the operator string, its precedence and if it is left associative. The operators `eval()` method will be called with the BigDecimal values of the operands.
All existing operators can also be overridden.

For example, add an operator `x >> n`, that moves the decimal point of _x_ _n_ digits to the right:

````java
Expression e = new Expression("2.1234 >> 2");

e.addOperator(e.new Operator(">>", 30, true) {
    @Override
    public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
        return v1.movePointRight(v2.toBigInteger().intValue());
    }
});

e.eval(); // returns 212.34
````

### Add Custom Functions

Adding custom functions is as easy as adding custom operators. Create an instance of `Expression.Function`and add it to the expression.
Parameters are the function name and the count of required parameters. The functions `eval()` method will be called with a list of the BigDecimal parameters.
All existing functions can also be overridden.

For example, add a function `average(a,b,c)`, that will calculate the average value of a, b and c:

````java
Expression e = new Expression("2 * average(12,4,8)");

e.addFunction(e.new Function("average", 3) {
    @Override
    public BigDecimal eval(List<BigDecimal> parameters) {
        BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
        return sum.divide(new BigDecimal(3));
    }
});

e.eval(); // returns 16
````

### Project Layout

The software was created and tested using Java 1.6.0.

    src/   The Java sources
    tests/ JUnit tests
  
### Author and License

Copyright 2012 by Udo Klimaschewski

http://about.me/udo.klimaschewski

http://UdoJava.com

The software is licensed under the MIT Open Source license (see LICENSE file).

- The *power of* operator (^) implementation was copied from [Stack Overflow](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java) Thanks to Gene Marin
- The SQRT() function implementation was taken from the book [The Java Programmers Guide To numerical Computing](http://www.amazon.de/Java-Number-Cruncher-Programmers-Numerical/dp/0130460419) (Ronald Mak, 2002)
