/*
  Copyright 2012-2024 Udo Klimaschewski

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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.parser.Token;

/**
 * Produces a new DATE_TIME that represents the current date, at midnight (00:00), in the
 * system default time-zone.
 * <p>
 * It is useful for DATE_TIME comparison, when the current time must not be considered.
 * For example, in the expression:
 * <blockquote>{@code IF(expiryDate > DT_DATE_TODAY(), "expired", "valid")}</blockquote>
 *
 * @author oswaldobapvicjr
 */
public class DateTimeTodayFunction extends AbstractFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    Instant today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
    return expression.convertValue(today);
  }
}
