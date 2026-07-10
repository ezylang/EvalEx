/*
  Copyright 2012-2026 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex.functions.string.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.experimental.UtilityClass;

/**
 * Utility class providing timeout-based protection for regular expression evaluations.
 *
 * <p><strong>Note:</strong> This utility does not modify the underlying Java regex engine. It
 * forces a strict runtime boundary by intercepting character sequence access. If a regex evaluation
 * runs without bounds due to complex backtracking or malicious inputs, execution is aborted once
 * the timeout threshold is passed.
 *
 * <p>Inspired by https://stackoverflow.com/a/11348374 (posted by Andreas, modified by the
 * community)
 *
 * @author oswaldo.bapvic.jr
 * @since 3.7.0
 */
@UtilityClass
public class RegularExpressionUtils {

  /**
   * Creates a {@link Matcher} using a timeout-monitored input sequence.
   *
   * <p>This method calculates an absolute deadline using {@link System#nanoTime()} and wraps the
   * input sequence. The underlying regex engine will still backtrack normally, but the wrapper will
   * abort the operation if evaluation exceeds the allowed duration.
   *
   * @param expression the expression, where this utility is executed, to access the expression
   *     configuration.
   * @param token The current token from the parsed expression, for reporting purposes
   * @param string the character sequence to be searched
   * @param regex the regular expression string to be compiled
   * @return a {@link Matcher} configured to throw an exception if the evaluation timeout is reached
   * @throws EvaluationException if the regular expression's syntax is invalid
   */
  private static Matcher createMatcher(
      Expression expression, Token token, String string, String regex) throws EvaluationException {

    try {
      Pattern pattern = Pattern.compile(regex);
      int timeoutMillis = expression.getConfiguration().getRegexTimeoutMillis();

      CharSequence charSequence =
          timeoutMillis > 0
              ? TimeoutRegexCharSequence.withTimeoutDelta(string, timeoutMillis)
              : string;

      return pattern.matcher(charSequence);
    } catch (PatternSyntaxException e) {
      throw new EvaluationException(token, e.getClass().getCanonicalName() + ": " + e.getMessage());
    }
  }

  /**
   * Evaluates whether a given string matches a regular expression, bounding the total execution
   * time.
   *
   * <p>If the matching engine enters an unacceptably long evaluation path (e.g., due to
   * catastrophic backtracking), the process is aborted via an exception rather than running
   * indefinitely.
   *
   * @param expression the expression, where this utility is executed, to access the expression
   *     configuration.
   * @param token The current token from the parsed expression, for reporting purposes
   * @param string the character sequence to be searched
   * @param regex the regular expression pattern to be compiled
   * @return {@link EvaluationValue#TRUE} if the string matches the specified regular expression;
   *     {@link EvaluationValue#FALSE}, otherwise
   * @throws EvaluationException if the regex is invalid or the evaluation runtime exceeds the
   *     maximum configured timeout
   */
  public static EvaluationValue matches(
      Expression expression, Token token, String string, String regex) throws EvaluationException {
    Matcher matcher = createMatcher(expression, token, string, regex);
    try {
      return expression.convertValue(matcher.matches());
    } catch (IllegalStateException e) {
      throw new EvaluationException(token, "RegEx matching timed out");
    }
  }

  /**
   * A wrapper for {@link CharSequence} that enforces a runtime boundary during regex evaluation.
   *
   * <p>This class intercepts character access via {@link #charAt(int)} to check elapsed monotonic
   * time. It serves as a passive circuit breaker: it does not optimize or change the backtracking
   * behavior of the regex engine, but stops it from running indefinitely if it gets stuck in a
   * runaway loop.
   */
  static class TimeoutRegexCharSequence implements CharSequence {

    private final CharSequence inner;
    private final long timeoutNano;

    /**
     * Constructs a new {@code TimeoutRegexCharSequence} wrapper with an absolute nanosecond
     * deadline.
     *
     * @param inner the underlying character sequence to delegate to
     * @param timeoutNano the absolute System.nanoTime() marker when execution must abort
     */
    TimeoutRegexCharSequence(CharSequence inner, long timeoutNano) {
      this.inner = inner;
      this.timeoutNano = timeoutNano;
    }

    /**
     * Static factory method to create a wrapper using a relative duration (delta) in milliseconds.
     *
     * <p>This method computes the absolute nanosecond deadline starting from the moment it is
     * called, making it ideal for the initial instantiation entry point.
     *
     * @param inner the underlying character sequence to delegate to
     * @param timeoutMillisDelta the maximum allowed duration for evaluation in milliseconds
     * @return a new {@code TimeoutRegexCharSequence} instance initialized with the calculated
     *     deadline
     */
    public static TimeoutRegexCharSequence withTimeoutDelta(
        CharSequence inner, int timeoutMillisDelta) {
      long absoluteTimeoutNano =
          System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillisDelta);
      return new TimeoutRegexCharSequence(inner, absoluteTimeoutNano);
    }

    /**
     * Returns the character at the specified index, checking if the evaluation has timed out.
     *
     * @param index the index of the character to return
     * @return the character at the specified index
     * @throws IllegalStateException if the evaluation runtime has exceeded the allowed deadline
     * @throws IndexOutOfBoundsException if the index is negative or not less than the length
     */
    @Override
    public char charAt(int index) {
      if (System.nanoTime() > timeoutNano) {
        throw new IllegalStateException("RegEx matching timed out");
      }
      return inner.charAt(index);
    }

    /**
     * Returns the length of this character sequence.
     *
     * @return the number of characters in the underlying sequence
     */
    @Override
    public int length() {
      return inner.length();
    }

    /**
     * Returns a new {@code TimeoutRegexCharSequence} sharing the exact same expiration deadline.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the specified subsequence wrapped in a timeout-monitored sequence
     * @throws IndexOutOfBoundsException if start or end are invalid relative to the length
     */
    @Override
    public CharSequence subSequence(int start, int end) {
      return new TimeoutRegexCharSequence(inner.subSequence(start, end), this.timeoutNano);
    }

    @Override
    public String toString() {
      return inner.toString();
    }
  }
}
