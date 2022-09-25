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

import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorCombinedTest extends BaseExpressionEvaluatorTest {

  @Test
  void testOrderPositionExample() throws ParseException, EvaluationException {
    Map<String, Object> order = new HashMap<>();
    order.put("id", 12345);
    order.put("name", "Mary");
    Map<String, Object> position = new HashMap<>();
    position.put("article", 3114);
    position.put("amount", 3);
    position.put("price", new BigDecimal("14.95"));
    order.put("positions", List.of(position));

    Expression expression =
        new Expression("order.positions[x].amount * order.positions[x].price")
            .with("order", order)
            .and("x", 0);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("44.85");
  }
}
