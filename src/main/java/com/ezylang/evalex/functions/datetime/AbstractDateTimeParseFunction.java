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

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.parser.Token;
import java.time.Instant;
import java.time.ZoneId;

public abstract class AbstractDateTimeParseFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
    Instant instant;

    if (parameterValues.length < 2) {
      instant = parse(parameterValues[0].getStringValue(), null, zoneId);
    } else {
      instant =
          parse(parameterValues[0].getStringValue(), parameterValues[1].getStringValue(), zoneId);
    }
    return new EvaluationValue(instant);
  }

  protected abstract Instant parse(String value, String format, ZoneId zoneId);
}
