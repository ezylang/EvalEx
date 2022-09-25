---
layout: default
title: Function Dictionary
parent: Customization
nav_order: 3
---

## Function Dictionary

By default, EvalEx has its own dictionary for the functions that can be used in an expression,
the _MapBasedFunctionDictionary_. It stores and retrieves the functions in a case-insensitive
_TreeMap_.

You can easily define your own function directory, by defining a class that implements the
_FunctionDictionaryIfc_:

```java
public interface FunctionDictionaryIfc {

  /**
   * Allows to add a function to the dictionary. Implementation is optional, if you have a fixed set
   * of functions, this method can throw an exception.
   *
   * @param functionName The function name.
   * @param function The function implementation.
   */
  void addFunction(String functionName, FunctionIfc function);

  /**
   * Check if the dictionary has a function with that name.
   *
   * @param functionName The function name to look for.
   * @return <code>true</code> if a function was found or <code>false</code> if not.
   */
  default boolean hasFunction(String functionName) {
    return getFunction(functionName) != null;
  }

  /**
   * Get the function definition for a function name.
   *
   * @param functionName The name of the function.
   * @return The function definition or <code>null</code> if no function was found.
   */
  FunctionIfc getFunction(String functionName);
```

Only two methods need to be implemented:

- addFunction(String functionName, FunctionIfc function) - which allows to add a function.
- FunctionIfc getFunction(String functionName) - retrieves a function implementation.

The _addFunction()_ method is only called when a function is added to the configuration, using the
_withAdditionalFunctions()_ in the _ExpressionConfiguration_ object.

> NOTE: The function names that are passed to the methods are case-sensitive, just like they were
> entered in the expression.

The custom function dictionary can then be specified in the expression configuration:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .functionDictionary(new MyFunctionDirectory())
    .build();
```
