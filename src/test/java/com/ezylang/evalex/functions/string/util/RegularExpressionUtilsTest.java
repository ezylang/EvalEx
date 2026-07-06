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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.functions.string.util.RegularExpressionUtils.TimeoutRegexCharSequence;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RegularExpressionUtils}.
 *
 * @since 3.6.3
 * @author oswaldo.bapvic.jr
 */
class RegularExpressionUtilsTest {

  @Test
  void createMatcherWithTimeout_StringRegex_ShouldMatchSuccessfully() {
    String input = "Hello, world!";
    String regex = "Hello.*";
    int timeout = 1000;

    Matcher matcher = RegularExpressionUtils.createMatcherWithTimeout(input, regex, timeout);

    assertThat(matcher.matches()).isTrue();
  }

  @Test
  void createMatcherWithTimeout_PatternObject_ShouldMatchSuccessfully() {
    String input = "JUnit5 test";
    Pattern pattern = Pattern.compile("\\d+");
    int timeout = 1000;

    Matcher matcher = RegularExpressionUtils.createMatcherWithTimeout(input, pattern, timeout);

    assertThat(matcher.find()).isTrue();
    assertThat(matcher.group()).isEqualTo("5");
  }

  @Test
  void createMatcherWithTimeout_CatastrophicBacktracking_ShouldTimeoutAndThrowException() {
    // This regex pattern combined with the input creates a classic "catastrophic backtracking"
    // scenario
    String regex = "(a+a+)+y";
    String input = "a".repeat(5000);
    int timeoutMillis = 50;

    // Act
    Matcher matcher = RegularExpressionUtils.createMatcherWithTimeout(input, regex, timeoutMillis);

    // Assert
    assertThatThrownBy(matcher::matches)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("RegEx matching timed out");
  }

  @Nested
  class TimeoutRegexCharSequenceTest {

    @Test
    void charSequence_Length_ShouldReturnCorrectLength() {
      String input = "OpenAI";
      var sequence = new TimeoutRegexCharSequence(input, 1000);

      assertThat(sequence.length()).isEqualTo(6);
    }

    @Test
    void charSequence_ToString_ShouldReturnOriginalStringContent() {
      String input = "Test String";
      var sequence = new TimeoutRegexCharSequence(input, 1000);

      assertThat(sequence.toString()).isEqualTo("Test String");
    }

    @Test
    void charSequence_SubSequence_ShouldReturnValidWrappedSubSequence() {
      // Arrange
      String input = "Beautiful Day";
      var originalSequence = new TimeoutRegexCharSequence(input, 1000);

      // Act
      CharSequence subSeg = originalSequence.subSequence(0, 9);

      // Assert
      assertThat(subSeg.toString()).isEqualTo("Beautiful");
      assertThat(subSeg.length()).isEqualTo(9);
      assertThat(subSeg.charAt(0)).isEqualTo('B');
      assertThat(subSeg).isInstanceOf(TimeoutRegexCharSequence.class);
    }

    @Test
    void charSequence_SubSequence_ShouldInheritTimeoutBehavior() throws InterruptedException {
      // Arrange
      String input = "Short-lived sequence";
      int tightTimeout = 50; // 50 milliseconds
      var originalSequence = new TimeoutRegexCharSequence(input, tightTimeout);

      // Act
      CharSequence subSeg = originalSequence.subSequence(0, 5);

      // Wait out the timeout duration
      Thread.sleep(60);

      // Assert
      // The subsequence should also throw the exception because its deadline has passed
      assertThatThrownBy(() -> subSeg.charAt(0))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("RegEx matching timed out");
    }
  }
}
