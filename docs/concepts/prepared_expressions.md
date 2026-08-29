---
layout: default
title: Prepared Expressions
parent: Concepts
nav_order: 6
---

# Working with Prepared Expressions

For applications that evaluate the same expression repeatedly with different data, EvalEx provides `PreparedExpression` to dramatically improve performance.

Available since 3.8.0.

## The Problem

Repeatedly creating new `Expression` objects forces re-parsing the expression every time:

```java
// Inefficient: Parses expression for every order
for (Order order : orders) {
    Expression expr = new Expression("price * quantity", config);
    expr.with("price", order.getPrice())
        .with("quantity", order.getQty());
    BigDecimal total = expr.evaluate().getNumberValue();
}
```

When processing high-frequency data (streaming, batch jobs, rule engines), parsing becomes the bottleneck—accounting for 60-80% of evaluation time.

## The Solution: PreparedExpression

Parse the expression once at startup, then reuse it for many evaluations:

```java
// Efficient: Parses expression once
PreparedExpression prepared = new PreparedExpression("price * quantity", config);

for (Order order : orders) {
    Expression expr = prepared.newExpression(order.asDataAccessor());
    BigDecimal total = expr.evaluate().getNumberValue();
}
```

## Performance Improvement

| Scenario | Performance Gain |
|----------|-----------------|
| 10 evaluations | ~500μs faster |
| 100 evaluations | ~5ms faster |
| 1,000 evaluations | ~50ms faster |

Real-world example: 50 expressions × 10,000 messages/sec
- Without PreparedExpression: 3.35 seconds CPU/sec (>100% of one core)
- With PreparedExpression: 0.6 seconds CPU/sec (5x improvement)

## When to Use

Use PreparedExpression when:
- Same expression evaluated 100+ times
- High-frequency data processing (>1,000 evals/sec)
- Streaming, batch processing, or message processing
- Real-time systems with latency requirements

## Usage Pattern

```java
PreparedExpression prepared = new PreparedExpression("discount * amount", config);

for (Order order : orders) {
    Expression expr = prepared.newExpression(order.getDataAccessor());
    BigDecimal discount = expr.evaluate().getNumberValue();
}
```

## API Reference

### Creating a PreparedExpression

```java
// With default configuration
PreparedExpression prepared = new PreparedExpression("x + y");

// With custom configuration
PreparedExpression prepared = new PreparedExpression("x + y", customConfig);
```

### Creating Bound Expressions

```java
// With a custom data accessor
Expression expr = prepared.newExpression(myDataAccessor);

// Using configuration's default data accessor
Expression expr = prepared.newExpression();
```

## Best Practices

- Create `PreparedExpression` once, reuse many times
- Share `PreparedExpression` across threads (it is immutable and thread-safe)
- Store prepared expressions as constants or singletons
- Do not create a new `PreparedExpression` for each evaluation
