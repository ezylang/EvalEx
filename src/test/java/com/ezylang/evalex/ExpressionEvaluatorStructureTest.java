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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.parser.ParseException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorStructureTest extends BaseExpressionEvaluatorTest {

  @Test
  void testSimpleStructure() throws ParseException, EvaluationException {
    Map<String, BigDecimal> structure =
        new HashMap<>() {
          {
            put("b", new BigDecimal(99));
          }
        };

    Expression expression = createExpression("a.b").with("a", structure);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("99");
  }

  @Test
  void testTripleStructure() throws ParseException, EvaluationException {
    Map<String, Map<String, BigDecimal>> structure = new HashMap<>();

    Map<String, BigDecimal> subStructure =
        new HashMap<>() {
          {
            put("c", new BigDecimal(95));
          }
        };

    structure.put("b", subStructure);

    Expression expression = createExpression("a.b.c").with("a", structure);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("95");
  }

  @Test
  void testThrowsUnsupportedDataTypeForStructure() {
    assertThatThrownBy(() -> createExpression("a.b").with("a", "aString").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Unsupported data types in operation");
  }
}
