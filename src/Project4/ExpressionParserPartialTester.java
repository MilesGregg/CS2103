import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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

	@Test
	public void testEvaluate3() throws ExpressionParseException {
		//final String expressionStr = "((((3 + x) / 2 ^ (x + 2)) + -8))";
		final String expressionStr = "((((3 + x) / 2 ^ (x + 2)) + -8))";
		//final String expressionStr = "3^(x+2)";
		//System.out.println((int) _parser.parse(expressionStr).evaluate(4));
		//System.out.println((int) _parser.parse(expressionStr).evaluate(3));
		assertEquals(-7, (int) _parser.parse(expressionStr).evaluate(4));
	}

	@Test
	public void testEval4() throws ExpressionParseException {
		final String expr = "45/(8*(5-4)-3) + 3*(2^2)/(5-3)";
		assertEquals(15, _parser.parse(expr).evaluate(0), 0.3);
	}

	@Test
	public void testEval5() throws ExpressionParseException {
		final String expr = "((3-2)+(1+2)^2)/(5+(4-1))";
		assertEquals(1.25, _parser.parse(expr).evaluate(0), 0.3);
	}

	@Test
	public void testEval6() throws ExpressionParseException {
		final String expr = "-1*(2*x-(3-(4-3*x))+6*x)";
		assertEquals(-1, _parser.parse(expr).evaluate(0), 0.3);
		assertEquals(-6, _parser.parse(expr).evaluate(1), 0.3);
		assertEquals(-11, _parser.parse(expr).evaluate(2), 0.3);
	}

	@Test
	public void testEval7() throws ExpressionParseException {
		final String expr = "1/(3*x^2+4*x)*(4*x+5)";
		assertEquals(_parser.parse(expr).convertToString(0), "*\n" +
				"\t/\n" +
				"\t\t1.0\n" +
				"\t\t()\n" +
				"\t\t\t+\n" +
				"\t\t\t\t*\n" +
				"\t\t\t\t\t3.0\n" +
				"\t\t\t\t\t^\n" +
				"\t\t\t\t\t\tx\n" +
				"\t\t\t\t\t\t2.0\n" +
				"\t\t\t\t*\n" +
				"\t\t\t\t\t4.0\n" +
				"\t\t\t\t\tx\n" +
				"\t()\n" +
				"\t\t+\n" +
				"\t\t\t*\n" +
				"\t\t\t\t4.0\n" +
				"\t\t\t\tx\n" +
				"\t\t\t5.0\n");
		assertEquals(0.328125, _parser.parse(expr).evaluate(4), 0.0001);
		assertEquals(0.65, _parser.parse(expr).evaluate(2), 0.0001);
		assertEquals(1.2857, _parser.parse(expr).evaluate(1), 0.0001);
		assertEquals(-0.01534, _parser.parse(expr).evaluate(-87), 0.0001);
		assertEquals(0.01332, _parser.parse(expr).evaluate(100), 0.0001);
		System.out.println(_parser.parse(expr).convertToString(0));
	}

	@Test
	public void testEval8() throws ExpressionParseException {
		String expr = "3*(4+x)/(7+x+2^(x+3)) - 4*(6+3^x)/(7+2*x^9)";
		assertEquals(_parser.parse(expr).convertToString(0), "-\n" +
				"\t/\n" +
				"\t\t*\n" +
				"\t\t\t3.0\n" +
				"\t\t\t()\n" +
				"\t\t\t\t+\n" +
				"\t\t\t\t\t4.0\n" +
				"\t\t\t\t\tx\n" +
				"\t\t()\n" +
				"\t\t\t+\n" +
				"\t\t\t\t+\n" +
				"\t\t\t\t\t7.0\n" +
				"\t\t\t\t\tx\n" +
				"\t\t\t\t^\n" +
				"\t\t\t\t\t2.0\n" +
				"\t\t\t\t\t()\n" +
				"\t\t\t\t\t\t+\n" +
				"\t\t\t\t\t\t\tx\n" +
				"\t\t\t\t\t\t\t3.0\n" +
				"\t/\n" +
				"\t\t*\n" +
				"\t\t\t4.0\n" +
				"\t\t\t()\n" +
				"\t\t\t\t+\n" +
				"\t\t\t\t\t6.0\n" +
				"\t\t\t\t\t^\n" +
				"\t\t\t\t\t\t3.0\n" +
				"\t\t\t\t\t\tx\n" +
				"\t\t()\n" +
				"\t\t\t+\n" +
				"\t\t\t\t7.0\n" +
				"\t\t\t\t*\n" +
				"\t\t\t\t\t2.0\n" +
				"\t\t\t\t\t^\n" +
				"\t\t\t\t\t\tx\n" +
				"\t\t\t\t\t\t9.0\n");
		assertEquals(0.171998, _parser.parse(expr).evaluate(4), 0.0001);
		assertEquals(0.3808284, _parser.parse(expr).evaluate(2), 0.0001);
		assertEquals(-3.375, _parser.parse(expr).evaluate(1), 0.0001);
		assertEquals(3.1125, _parser.parse(expr).evaluate(-87), 0.0001);
		assertEquals(-3.2, _parser.parse(expr).evaluate(0), 0.0001);
		System.out.println(_parser.parse(expr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpression4 () throws ExpressionParseException {
		final String expressionStr = "x^(7-3*x+(4/x))-3";
		final String parseTreeStr = "-\n\t^\n\t\tx\n\t\t()\n\t\t\t+\n\t\t\t\t-\n\t\t\t\t\t7.0\n\t\t\t\t\t*" +
				"\n\t\t\t\t\t\t3.0\n\t\t\t\t\t\tx\n\t\t\t\t()\n\t\t\t\t\t/\n\t\t\t\t\t\t4.0\n\t\t\t\t\t\tx\n\t3.0\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
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
}
