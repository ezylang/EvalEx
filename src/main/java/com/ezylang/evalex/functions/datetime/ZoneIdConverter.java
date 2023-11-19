/*
  Copyright 2012-2023 Udo Klimaschewski

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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.Token;
import java.time.DateTimeException;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Validates and converts a zone ID. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZoneIdConverter {

  /**
   * Converts a zone ID string to a {@link ZoneId}. Throws an {@link EvaluationException} if
   * conversion fails.
   *
   * @param referenceToken The token for the error message, usually the function token.
   * @param zoneIdString The zone IDS string to convert.
   * @return The converted {@link ZoneId}.
   * @throws EvaluationException In case the zone ID can't be converted.
   */
  public static ZoneId convert(Token referenceToken, String zoneIdString)
      throws EvaluationException {
    try {
      return ZoneId.of(zoneIdString);
    } catch (DateTimeException exception) {
      throw new EvaluationException(
          referenceToken,
          String.format(
              "Unable to convert zone string '%s' to a zone ID: %s",
              referenceToken.getValue(), exception.getMessage()));
    }
  }
}
