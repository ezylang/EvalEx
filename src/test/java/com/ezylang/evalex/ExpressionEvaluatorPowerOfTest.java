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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorPowerOfTest extends BaseExpressionEvaluatorTest {

  @Test
  void testPrecedenceDefault() throws ParseException, EvaluationException {
    assertThat(evaluate("-2^2")).isEqualTo("4");
  }

  @Test
  void testPrecedenceHigher() throws ParseException, EvaluationException {
    ExpressionConfiguration config =
        ExpressionConfiguration.builder()
            .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER_HIGHER)
            .build();

    Expression expression = new Expression("-2^2", config);

    assertThat(expression.evaluate().getStringValue()).isEqualTo("-4");
  }
}
