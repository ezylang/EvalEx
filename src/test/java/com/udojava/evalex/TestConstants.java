package com.udojava.evalex;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TestConstants {
  private final String     constantName;
  private final BigDecimal constantValue;

  public TestConstants(String constantName, BigDecimal value) {
    this.constantName  = constantName;
    this.constantValue = value;
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
      new Object[]{"e",     Expression.e},
      new Object[]{"PI",    Expression.PI},
      new Object[]{"PHI",   Expression.PHI},
      new Object[]{"TRUE",  BigDecimal.ONE},
      new Object[]{"FALSE", BigDecimal.ZERO},
      new Object[]{"NULL",  null}
      );
  }

  @Test
  public void testEvaluateConstants() {
    assertEquals(
      constantValue,
      new Expression(constantName)
        .setPrecision(MathContext.UNLIMITED.getPrecision())
        .eval()
    );
  }

  @Test
  public void testConstantsAreNotVars() {
    assertTrue(new Expression(constantName).getUsedVariables().isEmpty());
  }
}
