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
package com.ezylang.evalex.parser;

import org.junit.jupiter.api.Test;

class ShuntingYardArrayTest extends BaseParserTest {

  @Test
  void testSimpleArray() throws ParseException {
    assertASTTreeIsEqualTo(
        "a[0]",
        "{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"a\"},{\"type\":\"NUMBER_LITERAL\",\"value\":\"0\"}]}");
  }

  @Test
  void testArrayExpression() throws ParseException {
    assertASTTreeIsEqualTo(
        "a[x+y]",
        "{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"a\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"+\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"x\"},{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"y\"}]}]}");
  }

  @Test
  void testArrayNested() throws ParseException {
    assertASTTreeIsEqualTo(
        "a[b[1]]",
        "{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"a\"},{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"b\"},{\"type\":\"NUMBER_LITERAL\",\"value\":\"1\"}]}]}");
  }

  @Test
  void testComplex() throws ParseException {
    assertASTTreeIsEqualTo(
        "a[b[100*(a+b)]-c[2+d[x+y]]]",
        "{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"a\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"-\",\"children\":[{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"b\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"*\",\"children\":[{\"type\":\"NUMBER_LITERAL\",\"value\":\"100\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"+\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"a\"},{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"b\"}]}]}]},{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"c\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"+\",\"children\":[{\"type\":\"NUMBER_LITERAL\",\"value\":\"2\"},{\"type\":\"ARRAY_INDEX\",\"value\":\"[\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"d\"},{\"type\":\"INFIX_OPERATOR\",\"value\":\"+\",\"children\":[{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"x\"},{\"type\":\"VARIABLE_OR_CONSTANT\",\"value\":\"y\"}]}]}]}]}]}]}");
  }
}
