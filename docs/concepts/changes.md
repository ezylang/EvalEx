---
layout: default
title: Major Changes
parent: Concepts
nav_order: 4
---

## Major Changes From Version 2 to 3

### New features

There are a lot of new features, see the README of the [home page](../index.htm) of this
documentation for more information.

Most notably new features:

* Better boolean support.
* Full support for string operations and functions.
* Full support for arrays.
* Full support for structures.
* Structures and arrays can be combined to work with arbitrary data structures.
* New data access support, connecting expressions with you data made easy.
* New configuration concept.

### License

The license has changed from MIT to Apache 2.0. This should not be a problem, as both licenses are
very similar. The Apache 2.0 license though is clearer about certain constraints to some use cases.

### New Java version

EvalEx 3 now requires at least Java 11 to run and compile.

### New package, group id and repository home.

EvalEx has moved from a personal repository to an organizational repository.
This decouples the product from a single person and allows more ad better control over the product.

### Complete rewrite

EvalEx 3 is a complete rewrite. After 10 years of adding features and trying to stay backward
compatible, I felt that this is the time for a big cut.
Trying to stay backward compatible has introduced several code constructs, that didn't feel good.
The lazy evaluation is one of that features to name.

This has direct impact to the integration of EvalEx in an existing application.
Though I believe that the new version has much better integration possibilities (e.g. the data
access interface, the separate configuration object), existing integration need some refactorings.

Depending on how you used EvalEx until now, this may not need very much changes, e.g.:

* Change the dependency coordinates of EvalEx to the new group id.
* Change the package imports.
* Adopt the new method and class names.
* Use the new return value (EvaluationValue instead of BigDecimal).

When you were using custom functions or operators, or own configurations, then you may need a bit
more work. Though even these migrations are straight forward and pretty easy.

Here are the main changes, when it comes to integration:

### New configuration concept

Configurations are now separated from the expression. You can create a configuration once and then
re-use it with your expressions. All the heavy constructor work is now in the configuration, making
the instantiation of an expression much faster. The new concept was designed with better thread
safety in mind.

### New default MathContext

The new default MathContext has a precision of 68 with a HALF_EVEN rounding mode, compared to a
precision of 7 in EvalEx 2. This is a major leap forward. Low precision has been a constant issue
with EvalEx 2.

### Moved from RPN to AST

EvalEx 2 used RPN (Reverse Polish Notation) for evaluation. The parser transformed the infix
notation to an RPN notation and used this representation for evaluation.

Now the parser transforms the infix notation to an AST (Abstract Syntax Tree), allowing several
improvements and new features. The main reason for the switch was the lazy evaluation needed for the
IF function. With the use of an AST, this was easy to achieve.

If you were using the getRPN() function in EvalEx 2, then the new getAllASTNodes()
getAbstractSyntaxTree() method may be useful for you.
If you still need RPN support, it should be possible to create an RPN notation out of the AST.

### New return value type

EvalEx 2 evaluation always returned a BigDecimal. With the support of strings, arrays and
structures, this was not suitable anymore. The new EvaluationValue return type for an evaluation has
a type member that defines what data type it holds (boolean, number, string, array, structure, or an
AST). It also has some convenience methods to transform the types, e.g. number to string.

### New data access concept

EvalEx 2 only had the possibility to pass and store variable values just before the evaluation into
a local java.util.Map data structure. This is also the default with EvalEx 3, so no change to
existing integrations is required.

The new concept allows additionally to configure a data access interface.
See chapter [Data Access](../customization/data_access.html) for details.

### Custom Operators

Adding custom operators is now easier, but has changed significantly.
See chapter [Custom Functions](../customization/custom_functions.html) for details.

### Custom function

Adding custom functions is now easier, but has changed significantly.
See chapter [Custom Operators](../customization/custom_operators.html) for details.
