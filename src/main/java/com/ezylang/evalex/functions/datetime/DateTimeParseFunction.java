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
package com.ezylang.evalex.functions.datetime;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@FunctionParameter(name = "value", isVarArg = true)
public class DateTimeParseFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    Instant instant;
    if (parameterValues.length < 2) {
      instant = Instant.parse(parameterValues[0].getStringValue());
    } else {
      DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern(parameterValues[1].getStringValue());
      try {
        LocalDateTime localDateTime =
            LocalDateTime.parse(parameterValues[0].getStringValue(), formatter);
        instant = localDateTime.toInstant(ZoneOffset.UTC);
      } catch (DateTimeParseException ex) {
        LocalDate localDate = LocalDate.parse(parameterValues[0].getStringValue(), formatter);
        instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
      }
    }
    return new EvaluationValue(instant);
  }
}
