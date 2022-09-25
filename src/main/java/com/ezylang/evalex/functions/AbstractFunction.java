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
package com.ezylang.evalex.functions;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of the {@link FunctionIfc}, used as base class for function
 * implementations.
 */
public abstract class AbstractFunction implements FunctionIfc {

  private final List<FunctionParameterDefinition> functionParameterDefinitions = new ArrayList<>();

  private final boolean hasVarArgs;

  /**
   * Creates a new function and uses the {@link FunctionParameter} annotations to create the
   * parameter definitions.
   */
  protected AbstractFunction() {
    FunctionParameter[] parameterAnnotations =
        getClass().getAnnotationsByType(FunctionParameter.class);

    boolean varArgParameterFound = false;

    for (FunctionParameter parameter : parameterAnnotations) {
      if (varArgParameterFound) {
        throw new IllegalArgumentException(
            "Only last parameter may be defined as variable argument");
      }
      if (parameter.isVarArg()) {
        varArgParameterFound = true;
      }
      functionParameterDefinitions.add(
          FunctionParameterDefinition.builder()
              .name(parameter.name())
              .isVarArg(parameter.isVarArg())
              .isLazy(parameter.isLazy())
              .nonZero(parameter.nonZero())
              .nonNegative(parameter.nonNegative())
              .build());
    }

    hasVarArgs = varArgParameterFound;
  }

  @Override
  public void validatePreEvaluation(Token token, EvaluationValue... parameterValues)
      throws EvaluationException {

    for (int i = 0; i < parameterValues.length; i++) {
      FunctionParameterDefinition definition = getParameterDefinitionForParameter(i);
      if (definition.isNonZero() && parameterValues[i].getNumberValue().equals(BigDecimal.ZERO)) {
        throw new EvaluationException(token, "Parameter must not be zero");
      }
      if (definition.isNonNegative() && parameterValues[i].getNumberValue().signum() < 0) {
        throw new EvaluationException(token, "Parameter must not be negative");
      }
    }
  }

  @Override
  public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
    return functionParameterDefinitions;
  }

  @Override
  public boolean hasVarArgs() {
    return hasVarArgs;
  }

  private FunctionParameterDefinition getParameterDefinitionForParameter(int index) {

    if (hasVarArgs && index >= functionParameterDefinitions.size()) {
      index = functionParameterDefinitions.size() - 1;
    }

    return functionParameterDefinitions.get(index);
  }
}
