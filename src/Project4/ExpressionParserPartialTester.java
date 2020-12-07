import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 4; you should definitely add more tests!
 */
public class ExpressionParserPartialTester {
	private ExpressionParser _parser;

	@Before
	/**
	 * Instantiates the actors and movies graphs
	 */
	public void setUp () throws IOException {
		_parser = new SimpleExpressionParser();
	}

	@Test
	/**
	 * Just verifies that the SimpleExpressionParser could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpression1 () throws ExpressionParseException {
		final String expressionStr = "x+x";
		final String parseTreeStr = "+\n\tx\n\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpression2 () throws ExpressionParseException {
		final String expressionStr = "13*x";
		final String parseTreeStr = "*\n\t13.0\n\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}


	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpression3 () throws ExpressionParseException {
		final String expressionStr = "4*(x-5*x)";
		final String parseTreeStr = "*\n\t4.0\n\t()\n\t\t-\n\t\t\tx\n\t\t\t*\n\t\t\t\t5.0\n\t\t\t\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}

	@Test
	public void testExpression4() throws ExpressionParseException {
		final String expressionStr = "6/2*(1+2)";
		assertEquals(_parser.parse(expressionStr).evaluate(0), 9, 0.3);
	}

	@Test
	public void testExpression5() throws ExpressionParseException {
		final String expressionStr = "9-3/(1/3)+1";
		assertEquals(_parser.parse(expressionStr).evaluate(0), 1, 0.3);
	}

	@Test
	public void testExpression6() throws ExpressionParseException {
		final String expressionStr = "10*4-2*(4^2/4)/2/(1/2)+9";
		assertEquals(_parser.parse(expressionStr).evaluate(0), 41, 0.3);
	}

	@Test
	public void testExpression7() throws ExpressionParseException {
		final String expressionStr = "-10/(20/2^2*5/5)*8-2";
		assertEquals(_parser.parse(expressionStr).evaluate(0), -18, 0.3);
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluate4 () throws ExpressionParseException {
		final String expressionStr = "(3)*(5)";
		assertEquals(15, (int) _parser.parse(expressionStr).evaluate(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluate5 () throws ExpressionParseException {
		final String expressionStr = "x^(7)";
		assertEquals(128, (int) _parser.parse(expressionStr).evaluate(2));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluate6 () throws ExpressionParseException {
		final String expressionStr = "x^(7)-3";
		assertEquals(125, (int) _parser.parse(expressionStr).evaluate(2));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluate7 () throws ExpressionParseException {
		final String expressionStr = "x^(7-3*x+(4/x))-3";
		assertEquals(5, (int) _parser.parse(expressionStr).evaluate(2));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluate8 () throws ExpressionParseException {
		final String expressionStr = "0+-2";
		assertEquals(-2, (int) _parser.parse(expressionStr).evaluate(2));
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	 public void testExpression19 () throws ExpressionParseException {
	 final String expressionStr = "x^(7-3*x+(4/x))-3";
	 final String parseTreeStr = "-\n\t^\n\t\tx\n\t\t()\n\t\t\t+\n\t\t\t\t-\n\t\t\t\t\t7.0\n\t\t\t\t\t*" +
	 "\n\t\t\t\t\t\t3.0\n\t\t\t\t\t\tx\n\t\t\t\t()\n\t\t\t\t\t/\n\t\t\t\t\t\t4.0\n\t\t\t\t\t\tx\n\t3.0\n";
	 assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	 }

	@Test(expected = ExpressionParseException.class) 
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testException1 () throws ExpressionParseException {
		final String expressionStr = "1+2+";
		_parser.parse(expressionStr);
	}

	@Test(expected = ExpressionParseException.class) 
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testException2 () throws ExpressionParseException {
		final String expressionStr = "((()))";
		_parser.parse(expressionStr);
	}

	@Test(expected = ExpressionParseException.class) 
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testException3 () throws ExpressionParseException {
		final String expressionStr = "()()";
		_parser.parse(expressionStr);
	}

        @Test
        /**
         * Verifies that a specific expression is evaluated correctly.
         */
        public void testEvaluate1 () throws ExpressionParseException {
                final String expressionStr = "4*(x+5*x)";
                System.out.println((int) _parser.parse(expressionStr).evaluate(3));
                assertEquals(72, (int) _parser.parse(expressionStr).evaluate(3));
        }

        @Test
        /**
         * Verifies that a specific expression is evaluated correctly.
         */
        public void testEvaluate2 () throws ExpressionParseException {
                final String expressionStr = "x";
                assertEquals(1, (int) _parser.parse(expressionStr).evaluate(1));
        }
}
