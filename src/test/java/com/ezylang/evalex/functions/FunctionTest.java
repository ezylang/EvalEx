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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.Token;
import org.junit.jupiter.api.Test;

class FunctionTest {

  @Test
  void testParameterDefinition() {
    FunctionIfc function = new CorrectFunctionDefinitionFunction();

    assertThat(function.getFunctionParameterDefinitions().get(0).getName()).isEqualTo("default");
    assertThat(function.getFunctionParameterDefinitions().get(0).isLazy()).isFalse();
    assertThat(function.getFunctionParameterDefinitions().get(0).isVarArg()).isFalse();

    assertThat(function.getFunctionParameterDefinitions().get(1).getName()).isEqualTo("lazy");
    assertThat(function.getFunctionParameterDefinitions().get(1).isLazy()).isTrue();
    assertThat(function.getFunctionParameterDefinitions().get(1).isVarArg()).isFalse();

    assertThat(function.getFunctionParameterDefinitions().get(2).getName()).isEqualTo("vararg");
    assertThat(function.getFunctionParameterDefinitions().get(2).isLazy()).isFalse();
    assertThat(function.getFunctionParameterDefinitions().get(2).isVarArg()).isTrue();
  }

  @Test
  void testParameterIsLazy() {
    FunctionIfc function = new CorrectFunctionDefinitionFunction();

    assertThat(function.isParameterLazy(0)).isFalse();
    assertThat(function.isParameterLazy(1)).isTrue();
    assertThat(function.isParameterLazy(2)).isFalse();
    assertThat(function.isParameterLazy(3)).isFalse();
    assertThat(function.isParameterLazy(4)).isFalse();
  }

  @Test
  void testVarargNotAllowed() {
    assertThatThrownBy(WrongVarargFunctionDefinitionFunction::new)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Only last parameter may be defined as variable argument");
  }

  @FunctionParameter(name = "default")
  @FunctionParameter(name = "lazy", isLazy = true)
  @FunctionParameter(name = "vararg", isVarArg = true)
  private static class CorrectFunctionDefinitionFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token functionToken, EvaluationValue... parameterValues) {
      return new EvaluationValue("OK");
    }
  }

  @FunctionParameter(name = "default")
  @FunctionParameter(name = "vararg", isVarArg = true)
  @FunctionParameter(name = "another")
  private static class WrongVarargFunctionDefinitionFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token functionToken, EvaluationValue... parameterValues) {
      return new EvaluationValue("OK");
    }
  }
}
