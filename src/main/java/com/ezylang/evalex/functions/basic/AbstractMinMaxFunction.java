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

import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import java.math.BigDecimal;

@FunctionParameter(name = "value", isVarArg = true)
public abstract class AbstractMinMaxFunction extends AbstractFunction {
  BigDecimal findMinOrMax(BigDecimal current, EvaluationValue parameter, boolean findMin) {
    if (parameter.isArrayValue()) {
      for (EvaluationValue element : parameter.getArrayValue()) {
        current = findMinOrMax(current, element, findMin);
      }
    } else {
      current = compareAndAssign(current, parameter.getNumberValue(), findMin);
    }
    return current;
  }

  BigDecimal compareAndAssign(BigDecimal current, BigDecimal newValue, boolean findMin) {
    if (current == null
        || (findMin ? newValue.compareTo(current) < 0 : newValue.compareTo(current) > 0)) {
      current = newValue;
    }
    return current;
  }
}
