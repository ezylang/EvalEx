package com.udojava.evalex;

import com.udojava.evalex.Expression.Token;
import com.udojava.evalex.Expression.TokenType;
import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class TestTokenizer {

  private void assertToken(String surface, Expression.TokenType type, Expression.Token actual) {
    assertEquals(surface, actual.surface);
    assertEquals(type, actual.type);
  }

  @Test
  public void testSpacesFunctions() {
    Expression e;
    Iterator<Token> i;

    e = new Expression("sin (30)");
    i = e.getExpressionTokenizer();
    assertToken("sin", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("30", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertFalse(i.hasNext());
  }

  @Test
  public void testSpacesFunctionsVariablesOperators() {
    Expression e;
    Iterator<Token> i;

    e = new Expression("   sin   (   30   +   x   )   ");
    i = e.getExpressionTokenizer();
    assertToken("sin", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("30", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("x", TokenType.VARIABLE, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertFalse(i.hasNext());
  }

  @Test
  public void testNumbers() {
    Expression e;
    Iterator<Token> i;

    e = new Expression("1");
    i = e.getExpressionTokenizer();
    assertToken("1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("-1");
    i = e.getExpressionTokenizer();
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("123");
    i = e.getExpressionTokenizer();
    assertToken("123", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("-123");
    i = e.getExpressionTokenizer();
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("123", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("123.4");
    i = e.getExpressionTokenizer();
    assertToken("123.4", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("-123.456");
    i = e.getExpressionTokenizer();
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("123.456", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression(".1");
    i = e.getExpressionTokenizer();
    assertToken(".1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("-.1");
    i = e.getExpressionTokenizer();
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken(".1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());
  }

  @Test
  public void testTokenizerExtraSpaces() {
    Expression e = new Expression("1 ");
    Iterator<Token> i = e.getExpressionTokenizer();
    assertTrue(i.hasNext());
    assertToken("1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("       ");
    i = e.getExpressionTokenizer();
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("   1      ");
    i = e.getExpressionTokenizer();
    assertTrue(i.hasNext());
    assertToken("1", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());

    e = new Expression("  1   +   2    ");
    i = e.getExpressionTokenizer();
    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2", TokenType.LITERAL, i.next());
    assertFalse(i.hasNext());
    assertNull(i.next());
  }

  @Test
  public void testTokenizer1() {
    Expression e = new Expression("1+2");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizer2() {
    Expression e = new Expression("1 + 2");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizer3() {
    Expression e = new Expression(" 1 + 2 ");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizer4() {
    Expression e = new Expression("1+2-3/4*5");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2", TokenType.LITERAL, i.next());
    assertToken("-", TokenType.OPERATOR, i.next());
    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("4", TokenType.LITERAL, i.next());
    assertToken("*", TokenType.OPERATOR, i.next());
    assertToken("5", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizer5() {
    Expression e = new Expression("1+2.1-3.45/4.982*5.0");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2.1", TokenType.LITERAL, i.next());
    assertToken("-", TokenType.OPERATOR, i.next());
    assertToken("3.45", TokenType.LITERAL, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("4.982", TokenType.LITERAL, i.next());
    assertToken("*", TokenType.OPERATOR, i.next());
    assertToken("5.0", TokenType.LITERAL, i.next());

  }

  @Test
  public void testTokenizer6() {
    Expression e = new Expression("-3+4*-1");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("4", TokenType.LITERAL, i.next());
    assertToken("*", TokenType.OPERATOR, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("1", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizer7() {
    Expression e = new Expression("(-3+4)*-1/(7-(5*-8))");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("4", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());

    assertToken("*", TokenType.OPERATOR, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("1", TokenType.LITERAL, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("7", TokenType.LITERAL, i.next());
    assertToken("-", TokenType.OPERATOR, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("5", TokenType.LITERAL, i.next());
    assertToken("*", TokenType.OPERATOR, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("8", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
  }

  @Test
  public void testTokenizer8() {
    Expression e = new Expression("(1.9+2.8)/4.7");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("1.9", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("2.8", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("4.7", TokenType.LITERAL, i.next());

  }

  @Test
  public void testTokenizerFunction1() {
    Expression e = new Expression("ABS(3.5)");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("ABS", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("3.5", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
  }

  @Test
  public void testTokenizerFunction2() {
    Expression e = new Expression("3-ABS(3.5)/9");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("-", TokenType.OPERATOR, i.next());
    assertToken("ABS", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("3.5", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("9", TokenType.LITERAL, i.next());

  }

  @Test

  public void testTokenizerFunction3() {
    Expression e = new Expression("MAX(3.5,5.2)");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("MAX", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("3.5", TokenType.LITERAL, i.next());
    assertToken(",", TokenType.COMMA, i.next());
    assertToken("5.2", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
  }

  @Test
  public void testTokenizerFunction4() {
    Expression e = new Expression("3-MAX(3.5,5.2)/9");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("-", TokenType.OPERATOR, i.next());
    assertToken("MAX", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("3.5", TokenType.LITERAL, i.next());
    assertToken(",", TokenType.COMMA, i.next());
    assertToken("5.2", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("9", TokenType.LITERAL, i.next());
  }

  @Test
  public void testTokenizerFunction5() {
    Expression e = new Expression("3/MAX(-3.5,-5.2)/9");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("MAX", TokenType.FUNCTION, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("3.5", TokenType.LITERAL, i.next());
    assertToken(",", TokenType.COMMA, i.next());
    assertToken("-u", TokenType.UNARY_OPERATOR, i.next());
    assertToken("5.2", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
    assertToken("/", TokenType.OPERATOR, i.next());
    assertToken("9", TokenType.LITERAL, i.next());
  }

  @Test
  public void testInsertImplicitMultiplication() {
    Expression e = new Expression("22(3+1)");
    Iterator<Token> i = e.getExpressionTokenizer();

    assertToken("22", TokenType.LITERAL, i.next());
    assertToken("(", TokenType.OPEN_PAREN, i.next());
    assertToken("3", TokenType.LITERAL, i.next());
    assertToken("+", TokenType.OPERATOR, i.next());
    assertToken("1", TokenType.LITERAL, i.next());
    assertToken(")", TokenType.CLOSE_PAREN, i.next());
  }

  @Test
  public void testBracesCustomOperatorAndInIf() {
    Expression e = new Expression("if( (a=0) and (b=0), 0, 1)");
    e.addOperator(new AbstractOperator("AND", Expression.OPERATOR_PRECEDENCE_AND, false, true) {
      @Override
      public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
        boolean b1 = v1.compareTo(BigDecimal.ZERO) != 0;
        if (!b1) {
          return BigDecimal.ZERO;
        }
        boolean b2 = v2.compareTo(BigDecimal.ZERO) != 0;
        return b2 ? BigDecimal.ONE : BigDecimal.ZERO;
      }
    });

    BigDecimal result = e.with("a", "0").and("b", "0").eval();
    assertEquals("0", result.toPlainString());
  }

  @Test
  public void testStringLiterals() {
    Iterator<Token> i = new Expression("\"foo\"").getExpressionTokenizer();
    assertEquals(TokenType.STRINGPARAM, i.next().type);
    assertFalse(i.hasNext());
  }

  @Test
  public void testUnterminatedStringLiterals() {
    TokenizerException ex = assertThrows(TokenizerException.class, new ThrowingRunnable() {
      @Override
      public void run() {
        new Expression("\"foo").getExpressionTokenizer().next();
      }
    });

    assertEquals("unterminated string literal at character position 0", ex.getMessage());
  }

}
