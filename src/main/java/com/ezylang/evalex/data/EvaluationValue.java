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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.conversion.DefaultEvaluationValueConverter;
import com.ezylang.evalex.parser.ASTNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Value;

/**
 * The representation of the final or intermediate evaluation result value. The representation
 * consists of a data type and data value. Depending on the type, the value will be stored in a
 * corresponding object type.
 */
@Value
public class EvaluationValue implements Comparable<EvaluationValue> {

  /** Return value for a null {@link DataType#BOOLEAN}. */
  private static final Boolean NULL_BOOLEAN = null;

  /** Return value for a null {@link DataType#ARRAY}. */
  private static final List<EvaluationValue> NULL_ARRAY = null;

  /** Return value for a null {@link DataType#STRUCTURE}. */
  private static final Map<String, EvaluationValue> NULL_STRUCTURE = null;

  /** The supported data types. */
  public enum DataType {
    /** A string of characters, stored as {@link String}. */
    STRING,
    /** Any number, stored as {@link BigDecimal}. */
    NUMBER,
    /** A boolean, stored as {@link Boolean}. */
    BOOLEAN,
    /** A date time value, stored as {@link java.time.Instant}. */
    DATE_TIME,
    /** A period value, stored as {@link java.time.Duration}. */
    DURATION,
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
    EXPRESSION_NODE,
    /** A null value */
    NULL
  }

  Object value;

  DataType dataType;

  /**
   * Creates a new evaluation value by using the default converter and configuration.
   *
   * @param value Any object that the default converter can convert.
   * @throws IllegalArgumentException if the data type can't be mapped.
   * @see DefaultEvaluationValueConverter
   * @deprecated Use {@link EvaluationValue(Object, ExpressionConfiguration)} instead.
   */
  @Deprecated(since = "3.1.0", forRemoval = true)
  public EvaluationValue(Object value) {
    this(value, ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Creates a new evaluation value by using the configured converter and configuration.
   *
   * @param value One of the supported data types.
   * @param configuration The expression configuration to use.
   * @throws IllegalArgumentException if the data type can't be mapped.
   * @see ExpressionConfiguration#getEvaluationValueConverter()
   */
  public EvaluationValue(Object value, ExpressionConfiguration configuration) {

    EvaluationValue converted =
        configuration.getEvaluationValueConverter().convertObject(value, configuration);

    this.value = converted.getValue();
    this.dataType = converted.getDataType();
  }

  /**
   * Private constructor to directly create an instance with a given type and value.
   *
   * @param value The value to set, no conversion will be done.
   * @param dataType The data type to set.
   */
  private EvaluationValue(Object value, DataType dataType) {
    this.dataType = dataType;
    this.value = value;
  }

  /**
   * Creates a new null value.
   *
   * @return A new null value.
   */
  public static EvaluationValue nullValue() {
    return new EvaluationValue(null, DataType.NULL);
  }

  /**
   * Creates a new number value.
   *
   * @param value The BigDecimal value to use.
   * @return the new number value.
   */
  public static EvaluationValue numberValue(BigDecimal value) {
    return new EvaluationValue(value, DataType.NUMBER);
  }

  /**
   * Creates a new string value.
   *
   * @param value The String value to use.
   * @return the new string value.
   */
  public static EvaluationValue stringValue(String value) {
    return new EvaluationValue(value, DataType.STRING);
  }

  /**
   * Creates a new boolean value.
   *
   * @param value The Boolean value to use.
   * @return the new boolean value.
   */
  public static EvaluationValue booleanValue(Boolean value) {
    return new EvaluationValue(value, DataType.BOOLEAN);
  }

  /**
   * Creates a new date-time value.
   *
   * @param value The Instant value to use.
   * @return the new date-time value.
   */
  public static EvaluationValue dateTimeValue(Instant value) {
    return new EvaluationValue(value, DataType.DATE_TIME);
  }

  /**
   * Creates a new duration value.
   *
   * @param value The Duration value to use.
   * @return the new duration value.
   */
  public static EvaluationValue durationValue(Duration value) {
    return new EvaluationValue(value, DataType.DURATION);
  }

  /**
   * Creates a new expression node value.
   *
   * @param value The ASTNode value to use.
   * @return the new expression node value.
   */
  public static EvaluationValue expressionNodeValue(ASTNode value) {
    return new EvaluationValue(value, DataType.EXPRESSION_NODE);
  }

  /**
   * Creates a new array value.
   *
   * @param value The List value to use.
   * @return the new array value.
   */
  public static EvaluationValue arrayValue(List<?> value) {
    return new EvaluationValue(value, DataType.ARRAY);
  }

  /**
   * Creates a new structure value.
   *
   * @param value The Map value to use.
   * @return the new structure value.
   */
  public static EvaluationValue structureValue(Map<?, ?> value) {
    return new EvaluationValue(value, DataType.STRUCTURE);
  }

  /**
   * Creates a new evaluation value from a double value using the specified {@link MathContext}.
   *
   * @param value The double value.
   * @param mathContext The math context to use.
   * @deprecated since 3.1.0 - Use {@link EvaluationValue(Object, ExpressionConfiguration)}.
   */
  @Deprecated(since = "3.1.0", forRemoval = true)
  public EvaluationValue(double value, MathContext mathContext) {
    this.dataType = DataType.NUMBER;
    this.value = new BigDecimal(Double.toString(value), mathContext);
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
   * Checks if the value is of type {@link DataType#DATE_TIME}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isDateTimeValue() {
    return getDataType() == DataType.DATE_TIME;
  }

  /**
   * Checks if the value is of type {@link DataType#DURATION}.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isDurationValue() {
    return getDataType() == DataType.DURATION;
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

  public boolean isNullValue() {
    return getDataType() == DataType.NULL;
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
      return EvaluationValue.numberValue(new BigDecimal(hexToInteger, mathContext));
    } else {
      return EvaluationValue.numberValue(new BigDecimal(value, mathContext));
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
      case NULL:
        return null;
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
    switch (getDataType()) {
      case NUMBER:
        return ((BigDecimal) value).toPlainString();
      case NULL:
        return null;
      default:
        return value.toString();
    }
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
        return ((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0;
      case BOOLEAN:
        return (Boolean) value;
      case STRING:
        return Boolean.parseBoolean((String) value);
      case NULL:
        return NULL_BOOLEAN;
      default:
        return false;
    }
  }

  /**
   * Gets a {@link Instant} representation of the value. If possible and needed, a conversion will
   * be made.
   *
   * <ul>
   *   <li>Any number value will return the instant from the epoc value.
   *   <li>Any string with the string representation of a LocalDateTime (ex: <code>
   *       "2018-11-30T18:35:24.00"</code>) (case ignored) will return the current LocalDateTime.
   *   <li>The date {@link Instant#EPOCH} will return if a conversion error occurs or in all other
   *       cases.
   * </ul>
   *
   * @return The {@link Instant} representation of the value.
   */
  public Instant getDateTimeValue() {
    try {
      switch (getDataType()) {
        case NUMBER:
          return Instant.ofEpochMilli(((BigDecimal) value).longValue());
        case DATE_TIME:
          return (Instant) value;
        case STRING:
          return Instant.parse((String) value);
        default:
          return Instant.EPOCH;
      }
    } catch (DateTimeException ex) {
      return Instant.EPOCH;
    }
  }

  /**
   * Gets a {@link Duration} representation of the value. If possible and needed, a conversion will
   * be made.
   *
   * <ul>
   *   <li>Any non-zero number value will return the duration from the millisecond.
   *   <li>Any string with the string representation of an {@link Duration} (ex: <code>
   *       "PnDTnHnMn.nS"</code>) (case ignored) will return the current instant.
   *   <li>The {@link Duration#ZERO} will return if a conversion error occurs or in all other cases.
   * </ul>
   *
   * @return The {@link Duration} representation of the value.
   */
  public Duration getDurationValue() {
    try {
      switch (getDataType()) {
        case NUMBER:
          return Duration.ofMillis(((BigDecimal) value).longValue());
        case DURATION:
          return (Duration) value;
        case STRING:
          return Duration.parse((String) value);
        default:
          return Duration.ZERO;
      }
    } catch (DateTimeException ex) {
      return Duration.ZERO;
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
    } else if (isNullValue()) {
      return NULL_ARRAY;
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
    } else if (isNullValue()) {
      return NULL_STRUCTURE;
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
      case NULL:
        throw new NullPointerException("Can not compare a null value");
      case DATE_TIME:
        return getDateTimeValue().compareTo(toCompare.getDateTimeValue());
      case DURATION:
        return getDurationValue().compareTo(toCompare.getDurationValue());
      default:
        return getStringValue().compareTo(toCompare.getStringValue());
    }
  }
}
