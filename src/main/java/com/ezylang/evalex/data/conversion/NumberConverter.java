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
package com.ezylang.evalex.data.conversion;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import java.math.BigDecimal;

/** Converter to convert to the NUMBER data type. */
public class NumberConverter implements ConverterIfc {

  @Override
  public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
    BigDecimal bigDecimal;

    if (object instanceof BigDecimal) {
      bigDecimal = (BigDecimal) object;
    } else if (object instanceof Double) {
      bigDecimal = new BigDecimal(Double.toString((double) object), configuration.getMathContext());
    } else if (object instanceof Float) {
      bigDecimal = BigDecimal.valueOf((float) object);
    } else if (object instanceof Integer) {
      bigDecimal = BigDecimal.valueOf((int) object);
    } else if (object instanceof Long) {
      bigDecimal = BigDecimal.valueOf((long) object);
    } else if (object instanceof Short) {
      bigDecimal = BigDecimal.valueOf((short) object);
    } else if (object instanceof Byte) {
      bigDecimal = BigDecimal.valueOf((byte) object);
    } else {
      throw illegalArgument(object);
    }

    return EvaluationValue.numberValue(bigDecimal);
  }

  @Override
  public boolean canConvert(Object object) {
    return (object instanceof BigDecimal
        || object instanceof Double
        || object instanceof Float
        || object instanceof Integer
        || object instanceof Long
        || object instanceof Short
        || object instanceof Byte);
  }
}
