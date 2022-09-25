---
layout: default
title: Data Access
parent: Customization
nav_order: 5
---

## Data Access

By default, EvalEx has its own storage for variable values, the _MapBasedDataAccessor_.
It stores and retrieves the variables in a case-insensitive _TreeMap_.
A separate instance of the _MapBasedDataAccessor_ will be created through the default configured
supplier for each new Expression, separating the storage for each expression instance.

You can easily define your own data access interface, by defining a class that implements the
_DataAccessorInterface_:

```java
public interface DataAccessorIfc {

  /**
   * Retrieves a data value.
   *
   * @param variable The variable name, e.g. a variable or constant name.
   * @return The data value, or <code>null</code> if not found.
   */
  EvaluationValue getData(String variable);

  /**
   * Sets a data value.
   *
   * @param variable The variable name, e.g. a variable or constant name.
   * @param value The value to set.
   */
  void setData(String variable, EvaluationValue value);
}
```

Only two methods need to be implemented:

- getData(String variable) - should return a value for a variable name.
- setData(String variable, EvaluationValue value) - set a variable value.

The _setData()_ method is only called when a variable is added to the expression, e.g. using the
_with()_ method. You may leave the implementation empty, if you do want to support this.

> NOTE: The variable names that are passed to the methods are case-sensitive, just like they were
> entered in the expression.

The custom data accessor can then be specified in the expression configuration:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .dataAccessorSupplier(MyCustomDataAccessor::new)
    .build();

Expression expression = new Expression("2.128 + a", configuration);
```

The example here creates a new instance of the _MyCustomDataAccessor_ for each new instance of an
expression. But you could also share an instance for all expressions:

```java
final MyCustomDataAccessor customAccessor = new MyCustomDataAccessor();
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .dataAccessorSupplier(() -> customAccessor)
    .build();

Expression expression = new Expression("2.128 + a", configuration);
```
