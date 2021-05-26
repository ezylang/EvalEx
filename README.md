EvalEx - Java Expression Evaluator
==========
[![Build Status](https://travis-ci.com/uklimaschewski/EvalEx.svg?branch=master)](https://travis-ci.com/uklimaschewski/EvalEx) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=uklimaschewski_EvalEx&metric=alert_status)](https://sonarcloud.io/dashboard?id=uklimaschewski_EvalEx)

### Introduction

EvalEx is a handy expression evaluator for Java, that allows to evaluate simple mathematical and boolean expressions.

Key Features:
- Uses BigDecimal for calculation and result
- No dependencies to external libraries
- Precision and rounding mode can be set
- Supports variables
- Standard boolean and mathematical operators
- Standard basic mathematical and boolean functions
- Custom functions and operators can be added at runtime
- Functions can be defined with a variable number of arguments (see MIN and MAX functions)
- Supports for hexadecimal numbers and scientific notations of numbers
- Supports string literals in functions
- Supports implicit multiplication, e.g. (a+b)(a-b) or 2(x-y) which equals to (a+b)\*(a-b) or 2\*(x-y)

### Download / Maven
You can download the binaries, source code and JavaDoc jars from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22EvalEx%22%20g%3A%22com.udojava%22).

The project and source code in `zip` and `tar.gz` format can also be downloaded from the projects [release area](https://github.com/uklimaschewski/EvalEx/releases).

To include it in your Maven project, refer to the artifact in your pom. For example:
````xml
</dependencies>
    <dependency>
        <groupId>com.udojava</groupId>
        <artifactId>EvalEx</artifactId>
        <version>2.5</version>
    </dependency>
</dependencies>
````

If you're using gradle add to your project's app build.gradle:
````gradle
dependencies {
    ...
    compile 'com.udojava:EvalEx:2.5'
}
````

### FAQ
A list of frequently asked questions (and answers) can be found here: [FAQ](https://github.com/uklimaschewski/EvalEx/blob/master/FAQ.md)

### Usage Examples

````java
 BigDecimal result = null;
 
 // Simple usage with an expression without variables.
 Expression expression = new Expression("1+1/3");
 result = expression.eval(); // 1.333333
 // Lowering the precision.
 expression.setPrecision(2);
 result = expression.eval(); // 1.3
 
 // A more complex expression showing support for unary operators.
 result = new Expression("(3.4 + -4.1)/2").eval(); // -0.35
 
 // Using functions and variables.
 result = new Expression("SQRT(a^2 + b^2)")
         .with("a", "2.4")
         .and("b", "9.253")
         .eval(); // 9.5591845
 
 // Using pre-created BigDecimals for variables
 BigDecimal a = new BigDecimal("2.4");
 BigDecimal b = new BigDecimal("9.235");
 result = new Expression("SQRT(a^2 + b^2)")
         .with("a", a)
         .and("b", b)
         .eval(); // 9.5591845
 
 // Increasing the precision and setting a different rounding mode.
 result = new Expression("2.4/PI")
         .setPrecision(128)
         .setRoundingMode(RoundingMode.UP)
         .eval(); // 0.763943726841...
 
 // Using a function to receive a random number and test it.
 result = new Expression("random() > 0.5").eval(); // 1

 // Using more functions and showing the boolean support.
 result = new Expression("not(x<7 || sqrt(max(x,9,3,min(4,3))) <= 3)")
         .with("x", "22.9")
         .eval(); // 1
 
 // Calling a pre-defined function.
 result = new Expression("log10(100)").eval(); // 2
````

### Precision

The default precision is set to 7 digits (`MathContext.DECIMAL32`). Depending on
your use-case you will want to set a different precision to get accurate
results:

```java
 new Expression("1/3")
         .setPrecision(3)
         .eval(); // 0.333

 new Expression("1/3")
         .setPrecision(12)
         .eval(); // 0.333333333333
```

If you do not increase the precision as needed, you will get inaccurate results:

```java
 new Expression("123456789 + 123456789").eval(); // 246913600

 new Expression("123456789 + 123456789")
         .setPrecision(12)
         .eval(); // 246913578
```

### Default Settings
The default settings for an expression can be set on creation through an `ExpressionSettings` object.
It can be created using a builder pattern:
````java
ExpressionSettings settings = ExpressionSettings.builder()
         .mathContext(MathContext.DECIMAL128)
         .powerOperatorPrecedenceHigher()
         .build();
new Expression("-2^2", settings).eval();
````

### Supported Operators
<table>
  <tr><th>Mathematical Operators</th></tr>
  <tr><th>Operator</th><th>Description</th></tr>
  <tr><td>+</td><td>Additive operator / Unary plus</td></tr>
  <tr><td>-</td><td>Subtraction operator / Unary minus</td></tr>
  <tr><td>*</td><td>Multiplication operator, can be omitted in front of an open bracket</td></tr>
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
  <tr><td>&gt;</td><td>Greater than</td></tr>  <tr><td>&gt;=</td><td>Greater than or equal to</td></tr>
  <tr><td>&amp;&amp;</td><td>Boolean and</td></tr>
  <tr><td>||</td><td>Boolean or</td></tr>
</table>
*Boolean operators result always in a BigDecimal value of 1 or 0 (zero). Any non-zero value is treated as a _true_ value. Boolean _not_ is implemented by a function.

### Supported Functions
<table>
  <tr><th>Function<sup>*</sup></th><th>Description</th></tr>
  <tr><td>NOT(<i>expression</i>)</td><td>Boolean negation, 1 (means true) if the expression is not zero</td></tr>
  <tr><td>IF(<i>condition</i>,<i>value_if_true</i>,<i>value_if_false</i>)</td><td>Returns one value if the condition evaluates to true or the other if it evaluates to false</td></tr>
  <tr><td>RANDOM()</td><td>Produces a random number between 0 and 1</td></tr>
  <tr><td>MIN(<i>e1</i>,<i>e2</i>, <i>...</i>)</td><td>Returns the smallest of the given expressions</td></tr>
  <tr><td>MAX(<i>e1</i>,<i>e2</i>, <i>...</i>)</td><td>Returns the biggest of the given expressions</td></tr>
  <tr><td>ABS(<i>expression</i>)</td><td>Returns the absolute (non-negative) value of the expression</td></tr>
  <tr><td>ROUND(<i>expression</i>,precision)</td><td>Rounds a value to a certain number of digits, uses the current rounding mode</td></tr>
  <tr><td>FLOOR(<i>expression</i>)</td><td>Rounds the value down to the nearest integer</td></tr>
  <tr><td>CEILING(<i>expression</i>)</td><td>Rounds the value up to the nearest integer</td></tr>
  <tr><td>LOG(<i>expression</i>)</td><td>Returns the natural logarithm (base e) of an expression</td></tr>
  <tr><td>LOG10(<i>expression</i>)</td><td>Returns the common logarithm (base 10) of an expression</td></tr>
  <tr><td>SQRT(<i>expression</i>)</td><td>Returns the square root of an expression</td></tr>
  <tr><td>SINR(<i>expression</i>)</td><td>Returns the trigonometric sine of an angle (in radians)</td></tr>
  <tr><td>COSR(<i>expression</i>)</td><td>Returns the trigonometric cosine of an angle (in radians)</td></tr>
  <tr><td>TANR(<i>expression</i>)</td><td>Returns the trigonometric tangensuiju of an angle (in radians)</td></tr>
  <tr><td>COTR(<i>expression</i>)</td><td>Returns the trigonometric cotangens of an angle (in radians)</td></tr>
  <tr><td>SECR(<i>expression</i>)</td><td>Returns the secant (in radians)</td></tr>
  <tr><td>CSCR(<i>expression</i>)</td><td>Returns the cosecant (in radians)</td></tr>
  <tr><td>ASINR(<i>expression</i>)</td><td>Returns the angle of asin (in radians)</td></tr>
  <tr><td>ACOSR(<i>expression</i>)</td><td>Returns the angle of acos (in radians)</td></tr>
  <tr><td>ATANR(<i>expression</i>)</td><td>Returns the angle of atan (in radians)</td></tr>
  <tr><td>ACOTR(<i>expression</i>)</td><td>Returns the angle of acot (in radians)</td></tr>
  <tr><td>ATAN2R(<i>y</i>,<i>x</i>)</td><td>Returns the angle of atan2 (in radians)</td></tr>
  <tr><td>SIN(<i>expression</i>)</td><td>Returns the trigonometric sine of an angle (in degrees)</td></tr>
  <tr><td>COS(<i>expression</i>)</td><td>Returns the trigonometric cosine of an angle (in degrees)</td></tr>
  <tr><td>TAN(<i>expression</i>)</td><td>Returns the trigonometric tangens of an angle (in degrees)</td></tr>
  <tr><td>COT(<i>expression</i>)</td><td>Returns the trigonometric cotangens of an angle (in degrees)</td></tr>
  <tr><td>SEC(<i>expression</i>)</td><td>Returns the secant (in degrees)</td></tr>
  <tr><td>CSC(<i>expression</i>)</td><td>Returns the cosecant (in degrees)</td></tr>
  <tr><td>ASIN(<i>expression</i>)</td><td>Returns the angle of asin (in degrees)</td></tr>
  <tr><td>ACOS(<i>expression</i>)</td><td>Returns the angle of acos (in degrees)</td></tr>
  <tr><td>ATAN(<i>expression</i>)</td><td>Returns the angle of atan (in degrees)</td></tr>
  <tr><td>ACOT(<i>expression</i>)</td><td>Returns the angle of acot (in degrees)</td></tr>
  <tr><td>ATAN2(<i>y</i>,<i>x</i>)</td><td>Returns the angle of atan2 (in degrees)</td></tr>
  <tr><td>SINH(<i>expression</i>)</td><td>Returns the hyperbolic sine of a value</td></tr>
  <tr><td>COSH(<i>expression</i>)</td><td>Returns the hyperbolic cosine of a value</td></tr>
  <tr><td>TANH(<i>expression</i>)</td><td>Returns the hyperbolic tangens of a value</td></tr>
  <tr><td>COTH(<i>expression</i>)</td><td>Returns the hyperbolic cotangens of a value</td></tr>
  <tr><td>SECH(<i>expression</i>)</td><td>Returns the hyperbolic secant (in degrees)</td></tr>
  <tr><td>CSCH(<i>expression</i>)</td><td>Returns the hyperbolic cosecant (in degrees)</td></tr>
  <tr><td>ASINH(<i>expression</i>)</td><td>Returns the angle of hyperbolic sine (in degrees)</td></tr>
  <tr><td>ACOSH(<i>expression</i>)</td><td>Returns the angle of hyperbolic cosine (in degrees)</td></tr>
  <tr><td>ATANH(<i>expression</i>)</td><td>Returns the angle of hyperbolic tangens of a value</td></tr>
  <tr><td>RAD(<i>expression</i>)</td><td>Converts an angle measured in degrees to an approximately equivalent angle measured in radians</td></tr>
  <tr><td>DEG(<i>expression</i>)</td><td>Converts an angle measured in radians to an approximately equivalent angle measured in degrees</td></tr>
  <tr><td>FACT(<i>expression</i>)</td><td>Retuns the factorial value of an integer.  Will return 1 for 0 or a negative number</td></tr>
</table>
*Functions names are case insensitive.

### Supported Constants
<table>
  <tr><th>Constant</th><th>Description</th></tr>
  <tr><td>e</td><td>The value of <i>e</i>, exact to 70 digits</td></tr>
  <tr><td>PI</td><td>The value of <i>PI</i>, exact to 100 digits</td></tr>
  <tr><td>TRUE</td><td>The value one</td></tr>
  <tr><td>FALSE</td><td>The value zero</td></tr>
  <tr><td>NULL</td><td>The null value</td></tr>
</table>

### Add Custom Operators

Custom operators can be added easily, simply create an instance of `Expression.Operator` and add it to the expression.
Parameters are the operator string, its precedence and if it is left associative. The operators `eval()` method will be called with the BigDecimal values of the operands.
All existing operators can also be overridden.

For example, add an operator `x >> n`, that moves the decimal point of _x_ _n_ digits to the right:

````java
Expression e = new Expression("2.1234 >> 2");

e.addOperator(new AbstractOperator(">>", 30, true) {
    @Override
    public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
        return v1.movePointRight(v2.toBigInteger().intValue());
    }
});

e.eval(); // returns 212.34
````

Or another example, add a postfix unary operator `n!`, that calculates the factorial of n. The parameters for postfix unary operators are the operator's string, its precedence, if it is left associative, is it is boolean and if it is unary (<code>true</code>).

````java
Expression e = new Expression("4!");

e.addOperator(new AbstractOperator("!", Expression.OPERATOR_PRECEDENCE_POWER_HIGHER + 1, true, false, true) {
    @Override
    public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
        if(v1 == null) {
            throw new ArithmeticException("Operand may not be null");
        }
        if(v1.remainder(BigDecimal.ONE) != BigDecimal.ZERO) {
            throw new ArithmeticException("Operand must be an integer");
        }
        BigDecimal factorial = v1;
        v1 = v1.subtract(BigDecimal.ONE);
        if (factorial.compareTo(BigDecimal.ZERO) == 0 || factorial.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ONE;
        } else {
            while (v1.compareTo(BigDecimal.ONE) > 0) {
                factorial = factorial.multiply(v1);
                v1 = v1.subtract(BigDecimal.ONE);
            }
            return factorial;
        }
    }
});

e.eval(); // returns 24
````

### Add Custom Functions

Adding custom functions is as easy as adding custom operators. Create an instance of `Expression.Function`and add it to the expression.
Parameters are the function name and the count of required parameters. The functions `eval()` method will be called with a list of the BigDecimal parameters.
A `-1` as the number of parameters denotes a variable number of arguments.
All existing functions can also be overridden.

For example, add a function `average(a,b,c)`, that will calculate the average value of a, b and c:

````java
Expression e = new Expression("2 * average(12,4,8)");

e.addFunction(new AbstractFunction("average", -1) {
    @Override
    public BigDecimal eval(List<BigDecimal> parameters) {
        if (parameters.size() == 0) {
            throw new ExpressionException("average requires at least one parameter");
        }
        BigDecimal avg = new BigDecimal(0);
        for (BigDecimal parameter : parameters) {
            avg = avg.add(parameter);
        }
        return avg.divide(new BigDecimal(parameters.size()));
    }
});

e.eval(); // returns 16
````

#### Custom Functions With String Parameters

You can create a custom function with string parameters. Create an instance of `Expression.LazyFunction`and add it to the expression.
Parameters are the function name and the count of required parameters. The functions `lazyEval()` method will be called with a list of the LazyNumber parameters.
A `-1` as the number of parameters denotes a variable number of arguments. String parameters needs to be surrounded by `"`.

For example, add a function `STREQ("string1","string2")`, that will compare whether string1 and string2 are equal:

````java
Expression e = new Expression("STREQ(\"test\", \"test2\")");
e.addLazyFunction(new AbstractLazyFunction("STREQ", 2) {
    private LazyNumber ZERO = new LazyNumber() {
        public BigDecimal eval() {
            return BigDecimal.ZERO;
        }
        public String getString() {
            return "0";
        }
    };
    private LazyNumber ONE = new LazyNumber() {
        public BigDecimal eval() {
            return BigDecimal.ONE;
        }         
        public String getString() {
            return null;
        }
    };  
    @Override
    public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
        if (lazyParams.get(0).getString().equals(lazyParams.get(1).getString())) {
            return ZERO;
        }
        return ONE;
    }
});

e.eval(); // returns 1
````

### Author and License

Copyright 2012-2020 by Udo Klimaschewski

http://about.me/udo.klimaschewski

http://UdoJava.com

**Thanks to all who contributed to this project: [Contributors](https://github.com/uklimaschewski/EvalEx/graphs/contributors)**

The software is licensed under the MIT Open Source license (see [LICENSE](https://raw.githubusercontent.com/uklimaschewski/EvalEx/master/LICENSE) file).

* The *power of* operator (^) implementation was copied from [Stack Overflow](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java) Thanks to Gene Marin
* The SQRT() function implementation was taken from the book [The Java Programmers Guide To numerical Computing](http://www.amazon.de/Java-Number-Cruncher-Programmers-Numerical/dp/0130460419) (Ronald Mak, 2002)
* Varargs implementation based on "David's method" outlined in Gene Pavlovsky's comment from  [here](http://www.kallisti.net.nz/blog/2008/02/extension-to-the-shunting-yard-algorithm-to-allow-variable-numbers-of-arguments-to-functions/#comment-125789)

### Similar Projects

 * [A port of EvalEx to Dart](https://github.com/RobluScouting/EvalEx)
 * [exp4j](https://github.com/fasseg/exp4j), a mathematical expression evaluator for Java using doubles.
