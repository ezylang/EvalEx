EvalEx - Frequently Asked Questions (and answers)
==========
Please feel free to add content to this list.

### Can I use hexadecimal values?

Yes e.g., `BigDecimal result = new Expression("0xcafe + 0xbabe").eval();` will be correctly
evaluated by EvalEx.

### Incorrect results with large numbers

The default math context used in expressions has a precision of 7, which is too small for large
numbers. Either create the expression with a different math context, or specify the precision before
evaluating:

````Java
    Expression expression = new Expression("timestamp >= 1500551315569", new MathContext(13));
````

or

````Java
    expression.with("timestamp", "1500551315568").setPrecision(13).eval();
````

### Results are in scientific notation (e.g. 4E+1)

When evaluation an expression, e.g.:

````Java
    String result = new Expression("5*8").eval();
````

The string value in result will read `4E+1`. The reason is, that Expression.eval() returns
a `java.math.BigDecimal`
and the `toString()` method of `BigDecimal` uses scientific notation.
Use `Expression.eval().toPlainString()` instead.

### -2^2 returns 4 instead of -4

There is a bit confusion among different calculators on how the precedence and association is
handled in combination with the power of operator. Excel, for example, evaluates above expression to
4, but Google calculator -4. The default precedence for the power of operator in EvalEx is lower
than the unary minus operator and therefore, -2 is calculated first, which results in `(-2)*(-2)`,
which calculates to 4. The precedence of the power operator can be adjusted when an expression is
created:

````Java
    ExpressionSettings settings=ExpressionSettings.builder()
    .powerOperatorPrecedenceHigher()
    .build();
    BigDecimal result=new Expression("-2^2",settings).eval();
````
