package com.udojava.evalex;

import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;
import java.util.List;

public class LazyIfNumber implements LazyNumber {

  List<LazyNumber> lazyParams;

  public LazyIfNumber(List<LazyNumber> lazyParams) {
    this.lazyParams = lazyParams;
  }

  @Override
  public BigDecimal eval() {
    BigDecimal result = lazyParams.get(0).eval();
    assertNotNull(result);
    boolean isTrue = result.compareTo(BigDecimal.ZERO) != 0;
    return isTrue ? lazyParams.get(1).eval() : lazyParams.get(2).eval();
  }

  @Override
  public String getString() {
    return "IF";
  }

  private void assertNotNull(BigDecimal v1) {
    if (v1 == null) {
      throw new ArithmeticException("Operand may not be null");
    }
  }
}
