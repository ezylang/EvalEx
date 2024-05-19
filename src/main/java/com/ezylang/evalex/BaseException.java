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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** Base exception class used in EvalEx. */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@Getter
public class BaseException extends Exception {

  @EqualsAndHashCode.Include private final int startPosition;
  @EqualsAndHashCode.Include private final int endPosition;
  @EqualsAndHashCode.Include private final String tokenString;
  @EqualsAndHashCode.Include private final String message;

  public BaseException(int startPosition, int endPosition, String tokenString, String message) {
    super(message);
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.tokenString = tokenString;
    this.message = super.getMessage();
  }
}
