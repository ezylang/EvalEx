/*
  Copyright 2012-2022 Udo Klimaschewski

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
package com.ezylang.evalex;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BaseExceptionTest {

  @Test
  void testCreation() {
    BaseException exception = new BaseException(2, 4, "test", "message");

    assertThat(exception.getStartPosition()).isEqualTo(2);
    assertThat(exception.getEndPosition()).isEqualTo(4);
    assertThat(exception.getTokenString()).isEqualTo("test");
    assertThat(exception.getMessage()).isEqualTo("message");
  }

  @Test
  void testToString() {
    BaseException exception = new BaseException(2, 4, "test", "message");

    assertThat(exception)
        .hasToString(
            "BaseException(startPosition=2, endPosition=4, tokenString=test, message=message)");
  }
}
