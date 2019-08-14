/*
 * Copyright 2018 Udo Klimaschewski
 *
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.udojava.evalex;

import com.udojava.evalex.Expression.LazyNumber;
import java.math.BigDecimal;
import java.util.List;

/**
 * Lazy Number for IF function created for lazily evaluated IF condition
 */
public class LazyIfNumber implements LazyNumber {

  private List<LazyNumber> lazyParams;

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
    return lazyParams.get(0).getString();
  }

  private void assertNotNull(BigDecimal v1) {
    if (v1 == null) {
      throw new ArithmeticException("Operand may not be null");
    }
  }
}
