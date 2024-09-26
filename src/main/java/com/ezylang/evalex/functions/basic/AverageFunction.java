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
package com.ezylang.evalex.functions.basic;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Returns the average (arithmetic mean) of the numeric arguments, with recursive support for
 * arrays.
 *
 * @author oswaldo.bapvic.jr
 */
@FunctionParameter(name = "firstValue")
@FunctionParameter(name = "additionalValues", isVarArg = true)
public class AverageFunction extends AbstractMinMaxFunction {
  @Override
  public EvaluationValue evaluate(
      Expression expression, Token functionToken, EvaluationValue... parameterValues) {
    MathContext mathContext = expression.getConfiguration().getMathContext();
    BigDecimal average = average(mathContext, parameterValues);
    return expression.convertValue(average);
  }

  private BigDecimal average(MathContext mathContext, EvaluationValue... parameterValues) {
    SumAndCount aux = new SumAndCount();
    for (EvaluationValue parameter : parameterValues) {
      aux = aux.plus(recursiveSumAndCount(parameter));
    }

    return aux.sum.divide(aux.count, mathContext);
  }

  private SumAndCount recursiveSumAndCount(EvaluationValue parameter) {
    SumAndCount aux = new SumAndCount();
    if (parameter.isArrayValue()) {
      for (EvaluationValue element : parameter.getArrayValue()) {
        aux = aux.plus(recursiveSumAndCount(element));
      }
      return aux;
    }
    return new SumAndCount(parameter.getNumberValue(), BigDecimal.ONE);
  }

  private final class SumAndCount {
    private final BigDecimal sum;
    private final BigDecimal count;

    private SumAndCount() {
      this.sum = BigDecimal.ZERO;
      this.count = BigDecimal.ZERO;
    }

    private SumAndCount(BigDecimal sum, BigDecimal count) {
      this.sum = sum;
      this.count = count;
    }

    private SumAndCount plus(SumAndCount other) {
      return new SumAndCount(sum.add(other.sum), count.add(other.count));
    }
  }
}
