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
package com.ezylang.evalex.data;

import com.ezylang.evalex.parser.ASTNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Value;

/**
 * The representation of the final or intermediate evaluation result value. The representation
 * consists of a data type and data value. Depending on the type, the value will be stored in a
 * corresponding object type.
 */
@Value
public class EvaluationValue implements Comparable<EvaluationValue> {

  /** The supported data types. */
  public enum DataType {
    /** A string of characters, stored as {@link String}. */
    STRING,
    /** Any number, stored as {@link BigDecimal}. */
    NUMBER,
    /** A boolean, stored as {@link Boolean}. */
    BOOLEAN,
    /** A list evaluation values. Stored as {@link java.util.List<EvaluationValue>}. */
    ARRAY,
    /**
     * A structure with pairs of name/value members. Name is a string and the value is a {@link
     * EvaluationValue}. Stored as a {@link java.util.Map}.
     */
    STRUCTURE,
    /**
     * Used for lazy parameter evaluation, stored as an {@link ASTNode}, which can be evaluated on
     * demand.
     */
    EXPRESSION_NODE
  }

  Object value;

  DataType dataType;

  /**
   * Creates a new evaluation value by taking a good guess on the provided Java class and converting
   * it to one of the supported types.
   *
   * <table>
   *   <tr>
   *     <th>Input type</th><th>Storage Type</th>
   *   </tr>
   *   <tr><td>BigDecimal</td><td>BigDecimal</td></tr>
   *   <tr><td>Long, long</td><td>BigDecimal</td></tr>
   *   <tr><td>Integer, int</td><td>BigDecimal</td></tr>
   *   <tr><td>Short, short</td><td>BigDecimal</td></tr>
   *   <tr><td>Byte, byte</td><td>BigDecimal</td></tr>
   *   <tr><td>Double, double</td><td>BigDecimal *</td></tr>
   *   <tr><td>Float, float</td><td>BigDecimal *</td></tr>
   *   <tr><td>CharSequence , String</td><td>String</td></tr>
   *   <tr><td>Boolean, boolean</td><td>Boolean</td></tr>
   *   <tr><td>ASTNode</td><td>ASTNode</td></tr>
   *   <tr><td>List&lt;?&gt;</td><td>List&lt;EvaluationValue&gt; - each entry will be converted</td></tr>
   *   <tr><td>Map&lt?,?&gt;</td><td>Map&lt;String&gt;&lt;EvaluationValue&gt; - each entry will be converted.</td></tr>
   * </table>
   *
   * <i>* Be careful with conversion problems when using float or double, which are fractional
   * numbers. A (float)0.1 is e.g. converted to 0.10000000149011612</i>
   *
   * @param value One of the supported data types.
   * @throws IllegalArgumentException if the data type can't be mapped.
   */
  public EvaluationValue(Object value) {
    BigDecimal number = convertToBigDecimal(value);
    if (number != null) {
      this.dataType = DataType.NUMBER;
      this.value = number;
    } else if (value instanceof CharSequence) {
      this.dataType = DataType.STRING;
      this.value = ((CharSequence) value).toString();
    } else if (value instanceof Character) {
      this.dataType = DataType.STRING;
      this.value = ((Character) value).toString();
    } else if (value instanceof Boolean) {
      this.dataType = DataType.BOOLEAN;
      this.value = value;
    } else if (value instanceof ASTNode) {
      this.dataType = DataType.EXPRESSION_NODE;
      this.value = value;
    } else if (value instanceof List) {
      this.dataType = DataType.ARRAY;
      this.value = convertToList((List<?>) value);
    } else if (value instanceof Map) {
      this.dataType = DataType.STRUCTURE;
      this.value = convertMapStructure((Map<?, ?>) value);
    } else if (value instanceof EvaluationValue) {
      this.dataType = ((EvaluationValue) value).getDataType();
      this.value = ((EvaluationValue) value).getValue();
    } else {
      throw new IllegalArgumentException(
          "Unsupported data type '" + value.getClass().getName() + "'");
    }
  }

  public EvaluationValue(double value, MathContext mathContext) {
    this.dataType = DataType.NUMBER;
    this.value = new BigDecimal(Double.toString(value), mathContext);
  }

  /**
   * Converts a {@link Map} of objects to a {@link Map} of {@link EvaluationValue} values.
   *
   * @return A {@link Map} of {@link EvaluationValue} values.
   */
  private Map<String, EvaluationValue> convertMapStructure(Map<?, ?> value) {
    Map<String, EvaluationValue> structure = new HashMap<>();
    for (Entry<?, ?> entry : value.entrySet()) {
      String name = entry.getKey().toString();
      structure.put(name, new EvaluationValue(entry.getValue()));
    }
    return structure;
  }

  /**
   * Converts a {@link List} of objects to a {@link List} of {@link EvaluationValue} values.
   *
   * @return A {@link List} of {@link EvaluationValue} values.
   */
  private List<EvaluationValue> convertToList(List<?> value) {
    List<EvaluationValue> array = new ArrayList<>();
    value.forEach(element -> array.add(new EvaluationValue(element)));
    return array;
  }

  /**
   * Check and convert, if an {@link Object} can be converted to a {@link BigDecimal} value.
   *
   * @return A {@link BigDecimal} value of the object, or <code>null</code> if it can't be
   *     converted.
   */
  private BigDecimal convertToBigDecimal(Object value) {
    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else if (value instanceof Double) {
      return BigDecimal.valueOf((double) value);
    } else if (value instanceof Float) {
      return BigDecimal.valueOf((float) value);
    } else if (value instanceof Integer) {
      return BigDecimal.valueOf((int) value);
    } else if (value instanceof Long) {
      return BigDecimal.valueOf((long) value);
    } else if (value instanceof Short) {
      return BigDecimal.valueOf((short) value);
    } else if (value instanceof Byte) {
      return BigDecimal.valueOf((byte) value);
    } else {
      return null;
    }
  }

  /**
   * Checks if the value is of type {@link DataType#NUMBER}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isNumberValue() {
    return getDataType() == DataType.NUMBER;
  }

  /**
   * Checks if the value is of type {@link DataType#STRING}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isStringValue() {
    return getDataType() == DataType.STRING;
  }

  /**
   * Checks if the value is of type {@link DataType#BOOLEAN}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isBooleanValue() {
    return getDataType() == DataType.BOOLEAN;
  }

  /**
   * Checks if the value is of type {@link DataType#ARRAY}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isArrayValue() {
    return getDataType() == DataType.ARRAY;
  }

  /**
   * Checks if the value is of type {@link DataType#STRUCTURE}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isStructureValue() {
    return getDataType() == DataType.STRUCTURE;
  }

  /**
   * Checks if the value is of type {@link DataType#EXPRESSION_NODE}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isExpressionNode() {
    return getDataType() == DataType.EXPRESSION_NODE;
  }

  /**
   * Creates a {@link DataType#NUMBER} value from a {@link String}.
   *
   * @param value The {@link String} value.
   * @param mathContext The math context to use for creation of the {@link BigDecimal} storage.
   */
  public static EvaluationValue numberOfString(String value, MathContext mathContext) {
    if (value.startsWith("0x") || value.startsWith("0X")) {
      BigInteger hexToInteger = new BigInteger(value.substring(2), 16);
      return new EvaluationValue(new BigDecimal(hexToInteger, mathContext));
    } else {
      return new EvaluationValue(new BigDecimal(value, mathContext));
    }
  }

  /**
   * Gets a {@link BigDecimal} representation of the value. If possible and needed, a conversion
   * will be made.
   *
   * <ul>
   *   <li>Boolean <code>true</code> will return a {@link BigDecimal#ONE}, else {@link
   *       BigDecimal#ZERO}.
   * </ul>
   *
   * @return The {@link BigDecimal} representation of the value, or {@link BigDecimal#ZERO} if
   *     conversion is not possible.
   */
  public BigDecimal getNumberValue() {
    switch (getDataType()) {
      case NUMBER:
        return (BigDecimal) value;
      case BOOLEAN:
        return (Boolean.TRUE.equals(value) ? BigDecimal.ONE : BigDecimal.ZERO);
      case STRING:
        return Boolean.parseBoolean((String) value) ? BigDecimal.ONE : BigDecimal.ZERO;
      default:
        return BigDecimal.ZERO;
    }
  }

  /**
   * Gets a {@link String} representation of the value. If possible and needed, a conversion will be
   * made.
   *
   * <ul>
   *   <li>Number values will be returned as {@link BigDecimal#toPlainString()}.
   *   <li>The {@link Object#toString()} will be used in all other cases.
   * </ul>
   *
   * @return The {@link String} representation of the value.
   */
  public String getStringValue() {
    if (getDataType() == DataType.NUMBER) {
      return ((BigDecimal) value).toPlainString();
    }
    return value.toString();
  }

  /**
   * Gets a {@link Boolean} representation of the value. If possible and needed, a conversion will
   * be made.
   *
   * <ul>
   *   <li>Any non-zero number value will return true.
   *   <li>Any string with the value <code>"true"</code> (case ignored) will return true.
   * </ul>
   *
   * @return The {@link Boolean} representation of the value.
   */
  public Boolean getBooleanValue() {
    switch (getDataType()) {
      case NUMBER:
        return !value.equals(BigDecimal.ZERO);
      case BOOLEAN:
        return (Boolean) value;
      case STRING:
        return Boolean.parseBoolean((String) value);
      default:
        return false;
    }
  }

  /**
   * Gets a {@link List<EvaluationValue>} representation of the value.
   *
   * @return The {@link List<EvaluationValue>} representation of the value or an empty list, if no
   *     conversion is possible.
   */
  @SuppressWarnings("unchecked")
  public List<EvaluationValue> getArrayValue() {
    if (isArrayValue()) {
      return (List<EvaluationValue>) value;
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Gets a {@link Map} representation of the value.
   *
   * @return The {@link Map} representation of the value or an empty list, if no conversion is
   *     possible.
   */
  @SuppressWarnings("unchecked")
  public Map<String, EvaluationValue> getStructureValue() {
    if (isStructureValue()) {
      return (Map<String, EvaluationValue>) value;
    } else {
      return Collections.emptyMap();
    }
  }

  /**
   * Gets the expression node, if this value is of type {@link DataType#EXPRESSION_NODE}.
   *
   * @return The expression node, or null for any other data type.
   */
  public ASTNode getExpressionNode() {
    return isExpressionNode() ? ((ASTNode) getValue()) : null;
  }

  @Override
  public int compareTo(EvaluationValue toCompare) {
    switch (getDataType()) {
      case NUMBER:
        return getNumberValue().compareTo(toCompare.getNumberValue());
      case BOOLEAN:
        return getBooleanValue().compareTo(toCompare.getBooleanValue());
      default:
        return getStringValue().compareTo(toCompare.getStringValue());
    }
  }
}
