package com.udojava.evalex;

import com.udojava.evalex.Expression.TokenType;
import com.udojava.evalex.Expression.Token;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;


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
}
