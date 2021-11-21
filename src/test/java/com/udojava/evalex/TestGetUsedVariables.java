package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class TestGetUsedVariables {

  @Test
  public void testVars() {
    Expression ex = new Expression("a/2*PI+MIN(e,b)");
    List<String> usedVars = ex.getUsedVariables();
    assertEquals(2, usedVars.size());
    assertTrue(usedVars.contains("a"));
    assertTrue(usedVars.contains("b"));
  }

  @Test
  public void testVarsLongNames() {
    Expression ex = new Expression("var1/2*PI+MIN(var2,var3)");
    List<String> usedVars = ex.getUsedVariables();
    assertEquals(3, usedVars.size());
    assertTrue(usedVars.contains("var1"));
    assertTrue(usedVars.contains("var2"));
    assertTrue(usedVars.contains("var3"));
  }

  @Test
  public void testVarsNothing() {
    Expression ex = new Expression("1/2");
    List<String> usedVars = ex.getUsedVariables();
    assertEquals(0, usedVars.size());
  }

  @Test
  public void testStringComparison() {
    // test for issue #267 (getUsedVariables() throws NPE in expressions containing string literals)
    Expression ex = new Expression("foo=\"bar\"").setVariable("foo", (BigDecimal) null);
    assertEquals(Collections.singletonList("foo"), ex.getUsedVariables());
  }
}
