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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * Utilities for working safely with Regular Expressions.
 *
 * <p>Inspired by https://stackoverflow.com/a/11348374 (posted by Andreas, modified by the
 * community)
 *
 * @author oswaldo.bapvic.jr
 * @since 3.6.3
 */
@UtilityClass
public class RegularExpressionUtils {

  /**
   * Creates a {@link Matcher} with a specific execution timeout using a regular expression string.
   *
   * <p>This method compiles the provided regular expression and delegates the creation to {@link
   * #createMatcherWithTimeout(String, Pattern, int)}.
   *
   * @param string the character sequence to be searched
   * @param regex the regular expression string to be compiled
   * @param timeoutMillis the maximum time allowed for the matching operation in milliseconds
   * @return a {@link Matcher} configured to interrupt execution if the timeout is reached
   * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
   */
  public static Matcher createMatcherWithTimeout(String string, String regex, int timeoutMillis) {
    Pattern pattern = Pattern.compile(regex);
    return createMatcherWithTimeout(string, pattern, timeoutMillis);
  }

  /**
   * Creates a {@link Matcher} with a specific execution timeout using a pre-compiled {@link
   * Pattern}.
   *
   * <p>The timeout mechanism is enforced by wrapping the target string inside a {@code
   * TimeoutRegexCharSequence}, which monitors elapsed time during evaluation.
   *
   * @param string the character sequence to be searched
   * @param pattern the pre-compiled {@link Pattern} object
   * @param timeoutMillis the maximum time allowed for the matching operation in milliseconds
   * @return a {@link Matcher} configured to interrupt execution if the timeout is reached
   */
  public static Matcher createMatcherWithTimeout(
      String string, Pattern pattern, int timeoutMillis) {
    CharSequence charSequence = new TimeoutRegexCharSequence(string, timeoutMillis);
    return pattern.matcher(charSequence);
  }

  /**
   * A wrapper for {@link CharSequence} that enforces a processing time limit during regex matching.
   *
   * <p>This class intercepts character access via {@link #charAt(int)} to check if the elapsed time
   * exceeds the configured timeout threshold. If the limit is exceeded, it aborts execution.
   */
  static class TimeoutRegexCharSequence implements CharSequence {

    private final CharSequence inner;

    private final int timeoutMillis;

    private final long timeoutTime;

    /**
     * Constructs a new {@code TimeoutRegexCharSequence} wrapper.
     *
     * @param inner the underlying character sequence to delegate to
     * @param timeoutMillis the maximum allowed execution duration in milliseconds
     */
    public TimeoutRegexCharSequence(CharSequence inner, int timeoutMillis) {
      this.inner = inner;
      this.timeoutMillis = timeoutMillis;
      timeoutTime = System.currentTimeMillis() + timeoutMillis;
    }

    /**
     * Returns the character at the specified index, checking for execution timeout first.
     *
     * @param index the index of the character to return
     * @return the character at the specified index
     * @throws IllegalStateException if the elapsed time exceeds the configured {@code
     *     timeoutMillis}
     * @throws IndexOutOfBoundsException if the index is negative or not less than the length
     */
    @Override
    public char charAt(int index) {
      if (System.currentTimeMillis() > timeoutTime) {
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
     * Returns a new {@code TimeoutRegexCharSequence} that is a subsequence of this sequence.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the specified subsequence wrapped in a new timeout-monitored sequence
     * @throws IndexOutOfBoundsException if start or end are invalid relative to the length
     */
    @Override
    public CharSequence subSequence(int start, int end) {
      return new TimeoutRegexCharSequence(inner.subSequence(start, end), timeoutMillis);
    }

    @Override
    public String toString() {
      return inner.toString();
    }
  }
}
