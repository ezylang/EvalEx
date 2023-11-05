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
import java.util.Arrays;
import java.util.List;

/**
 * The default implementation of the {@link EvaluationValueConverterIfc}, used in the standard
 * configuration.
 *
 * <table>
 *   <tr>
 *     <th>Input type</th><th>Converter used</th>
 *   </tr>
 *   <tr><td>BigDecimal</td><td>NumberConverter</td></tr>
 *   <tr><td>Long, long</td><td>NumberConverter</td></tr>
 *   <tr><td>Integer, int</td><td>NumberConverter</td></tr>
 *   <tr><td>Short, short</td><td>NumberConverter</td></tr>
 *   <tr><td>Byte, byte</td><td>NumberConverter</td></tr>
 *   <tr><td>Double, double</td><td>NumberConverter *</td></tr>
 *   <tr><td>Float, float</td><td>NumberConverter *</td></tr>
 *   <tr><td>CharSequence , String</td><td>StringConverter</td></tr>
 *   <tr><td>Boolean, boolean</td><td>BooleanConverter</td></tr>
 *   <tr><td>Instant</td><td>DateTimeConverter</td></tr>
 *   <tr><td>Date</td><td>DateTimeConverter</td></tr>
 *   <tr><td>Calendar</td><td>DateTimeConverter</td></tr>
 *   <tr><td>ZonedDateTime</td><td>DateTimeConverter</td></tr>
 *   <tr><td>LocalDate</td><td>DateTimeConverter - the configured zone ID will be used for conversion</td></tr>
 *   <tr><td>LocalDateTime</td><td>DateTimeConverter - the configured zone ID will be used for conversion</td></tr>
 *   <tr><td>OffsetDateTime</td><td>DateTimeConverter</td></tr>
 *   <tr><td>Duration</td><td>DurationConverter</td></tr>
 *   <tr><td>ASTNode</td><td>ASTNode</td></tr>
 *   <tr><td>List&lt;?&gt;</td><td>ArrayConverter - each entry will be converted</td></tr>
 *   <tr><td>Map&lt?,?&gt;</td><td>StructureConverter - each entry will be converted.</td></tr>
 * </table>
 *
 * <i>* Be careful with conversion problems when using float or double, which are fractional
 * numbers. A (float)0.1 is e.g. converted to 0.10000000149011612</i>
 */
public class DefaultEvaluationValueConverter implements EvaluationValueConverterIfc {

  static List<ConverterIfc> converters =
      Arrays.asList(
          new NumberConverter(),
          new StringConverter(),
          new BooleanConverter(),
          new DateTimeConverter(),
          new DurationConverter(),
          new ExpressionNodeConverter(),
          new ArrayConverter(),
          new StructureConverter());

  public EvaluationValue convertObject(Object object, ExpressionConfiguration configuration) {

    if (object == null) {
      return EvaluationValue.nullValue();
    }

    if (object instanceof EvaluationValue) {
      return (EvaluationValue) object;
    }

    for (ConverterIfc converter : converters) {
      if (converter.canConvert(object)) {
        return converter.convert(object, configuration);
      }
    }

    throw new IllegalArgumentException(
        "Unsupported data type '" + object.getClass().getName() + "'");
  }
}
