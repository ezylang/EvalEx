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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Converter to convert to the ARRAY data type. */
public class ArrayConverter implements ConverterIfc {
  @Override
  public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
    List<EvaluationValue> list;

    if (object.getClass().isArray()) {
      list = convertArray(object, configuration);
    } else if (object instanceof List) {
      list = convertList((List<?>) object, configuration);
    } else {
      throw illegalArgument(object);
    }

    return EvaluationValue.arrayValue(list);
  }

  @Override
  public boolean canConvert(Object object, ExpressionConfiguration configuration) {
    return object instanceof List || object.getClass().isArray();
  }

  private static List<EvaluationValue> convertList(
      List<?> object, ExpressionConfiguration configuration) {
    return object.stream()
        .map(element -> EvaluationValue.of(element, configuration))
        .collect(Collectors.toList());
  }

  private List<EvaluationValue> convertArray(Object array, ExpressionConfiguration configuration) {
    if (array instanceof int[]) {
      return convertIntArray((int[]) array, configuration);
    } else if (array instanceof long[]) {
      return convertLongArray((long[]) array, configuration);
    } else if (array instanceof double[]) {
      return convertDoubleArray((double[]) array, configuration);
    } else if (array instanceof float[]) {
      return convertFloatArray((float[]) array, configuration);
    } else if (array instanceof short[]) {
      return convertShortArray((short[]) array, configuration);
    } else if (array instanceof char[]) {
      return convertCharArray((char[]) array, configuration);
    } else if (array instanceof byte[]) {
      return convertByteArray((byte[]) array, configuration);
    } else if (array instanceof boolean[]) {
      return convertBooleanArray((boolean[]) array, configuration);
    } else {
      return convertObjectArray((Object[]) array, configuration);
    }
  }

  private List<EvaluationValue> convertIntArray(
      int[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (int i : array) {
      list.add(EvaluationValue.of(i, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertLongArray(
      long[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (long l : array) {
      list.add(EvaluationValue.of(l, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertDoubleArray(
      double[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (double d : array) {
      list.add(EvaluationValue.of(d, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertFloatArray(
      float[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (float f : array) {
      list.add(EvaluationValue.of(f, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertShortArray(
      short[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (short s : array) {
      list.add(EvaluationValue.of(s, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertCharArray(
      char[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (char c : array) {
      list.add(EvaluationValue.of(c, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertByteArray(
      byte[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (byte b : array) {
      list.add(EvaluationValue.of(b, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertBooleanArray(
      boolean[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (boolean b : array) {
      list.add(EvaluationValue.of(b, configuration));
    }
    return list;
  }

  private List<EvaluationValue> convertObjectArray(
      Object[] array, ExpressionConfiguration configuration) {
    List<EvaluationValue> list = new ArrayList<>();
    for (Object o : array) {
      list.add(EvaluationValue.of(o, configuration));
    }
    return list;
  }
}
