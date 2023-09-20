---
layout: default
title: Working with Date-Times and Duration
parent: Concepts
nav_order: 4
---

## Working with Date-Times and Duration

Since version 3.1.0 of EvalEx, there are two additional data types _DATE_TIME_ and _DURATION_.

_DATE_TIME_ values are stored internally as _java.time.Instant_ values, _DURATION_ values are stored as
_java.time.Duration_ values.

A _DATE_TIME_ instant is instantaneous point on the time-line, it holds no information about the time zone.
Time zones come into play, when converting local dates-times to instants and vice versa.
The same instant can have different local date-time values, depending on the destination time zone.
The precision of a _DATE_TIME_ is up to nanoseconds.

A _DURATION_ is a certain amount of time, like e.g. "3 hours, 15 minutes and 6 seconds".
The smallest amount of a duration is 1 nanosecond.

### Arithmetic operations with _DATE_TIME_ and _DURATION_.

The infix plus and minus operators can be used to do calculations with _DATE_TIME_ and _DURATION_ values.
The outcome of the operation depends on the operator types:

#### Addition
| Left Operand | Right Operand | Result                                                                 |
|--------------|---------------|------------------------------------------------------------------------|
| _DATE_TIME_  | _DURATION_    | A new _DATE_TIME_ where the duration is added to the date.             |
| _DURATION_   | _DURATION_    | A new duration, which is the sum of both durations.                    |
| _DATE_TIME_  | _NUMBER_      | A new _DATE_TIME_ with the amount of a duration in milliseconds added. |

All other combinations of _DATE_TIME_ and _DURATION_ with other types will do a string concatenation.

Example. Adding a duration to a date-time:
```java
Instant start = Instant.parse("2023-12-03T23:15:30.00Z");
Duration duration = Duration.ofHours(3);

Expression expression = new Expression("start + duration");
EvaluationValue result =
    expression
        .with("start", start)
        .and("duration", duration)
        .evaluate();
System.out.println(result); // will print "EvaluationValue(value=2023-12-04T02:15:30Z, dataType=DATE_TIME)"
```

#### Subtraction
| Left Operand | Right Operand | Result                                                                      |
|--------------|---------------|-----------------------------------------------------------------------------|
| _DATE_TIME_  | _DATE_TIME_   | A duration which reflects the difference between the two date-times.        |
| _DATE_TIME_  | _DURATION_    | A new _DATE_TIME_ where the duration is subtracted from the date.           |
| _DURATION_   | _DURATION_    | A new duration, which is the difference of both durations.                  |
| _DATE_TIME_  | _NUMBER_      | A new _DATE_TIME_ with the amount of a duration in milliseconds subtracted. |

All other combinations of _DATE_TIME_ and _DURATION_ with other types will throw an _EvaluationException_.

Example. Find out the duration between two date-times:
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

The string representation of a duration is here in SO format, meaning 12 hours, 4 minutes and 30 seconds.

### Passing other Date-Time Types as variables

Instead of passing _java.time.Instant_ values for _DATE_TIME_ values, you can pass also the following Java data types.
They will be converted automatically to _java.time.Instant_ values.

| Input type     | Conversion note                                                                                                           |
|----------------|---------------------------------------------------------------------------------------------------------------------------|
| ZonedDateTime  | Directly converted.                                                                                                       |
| OffsetDateTime | Directly converted.                                                                                                       |
| LocalDate      | Converted using the configured time zone. Defaults to the systems time zone.<br/>The time is set to beginning of the day. |
| LocalDateTime  | Converted using the configured time zone. Defaults to the systems time zone.                                              |
| Date           | Directly converted.                                                                                                       |
| Calendar       | Directly converted.                                                                                                       |

### New Date-Time Functions

In addition to the possibility to add and subtract with _DATE_TIME_ and _DURATION_ values, there are also several new
functions to work with date-times. Most of them allow to create, parse and format date-time and duration values.

See Chapter [Date Time Functions](../references/functions.html#date-time-Functions)

### Configuration Changes