import java.util.function.*;

/**
 * TODO: Implement this class.
 */
public class SimpleExpressionParser implements ExpressionParser {
	/*
	 * Grammar:
	 * S -> A | P
	 * A -> A+M | A-M | M
	 * M -> M*E | M/E | E
	 * E -> P^E | P
	 * P -> (S) | L | V
	 * L -> <float>
	 * V -> x
	 */

	/**
	 * parse a string into a {@link Expression}
	 * @param str the string to parse into an expression tree
	 * @return - the {@link Expression}
	 * @throws ExpressionParseException if the string cannot be parsed into a expression
	 */
	public Expression parse (String str) throws ExpressionParseException {
		// remove all spaces is the string to help with parsing
		str = str.replaceAll(" ", "");
		// start CFG (context free grammars) parsing expression
		Expression expression = parseStartExpression(str);
		if (expression == null) {
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}
		// return the parsed expression
		return expression;
	}

	/**
	 * start the parsing according to the CFG (context free grammars)
	 * @param str - the string to be parse into the expression tree
	 * @return - the expression of S -> A | P
	 */
	protected Expression parseStartExpression (String str) {
		Expression expression = null;
		// TODO implement this method, helper methods, classes that implement Expression, etc.
		// start at parsing A according to CFG (context free grammars)
		 expression = parseA(str);
		 if (expression == null) {
		 	expression = parseParentheticalExpression(str);
		 }

		return expression;
	}

	/**
	 * parse A according to the CFG (context free grammars) rules.
	 * @param str - the string to be parsed into the expression tree
	 * @return - the expression of A -> A+M | A-M | M
	 */
	private Expression parseA (String str) {
		Expression expression = null;
		final String addRegex = ".+\\+.+";
		final String subRegex = ".+-.+";
		// check if addRegex is in the string
		if (str.matches(addRegex)) {
			expression = parseHelper(str, '+', this::parseA, this::parseM);
		}
		// check if subRegex is in the string
		if (str.matches(subRegex) && expression == null) {
			expression = parseHelper(str, '-', this::parseA, this::parseM);
		}
		// if we can't find a expression then check M
		if (expression == null) {
			expression = parseM(str);
		}
		// the output expression for A
		return expression;
	}

	/**
	 * parse M according to the CFG (context free grammars) rules.
	 * @param str - the string to be parsed into the expression tree
	 * @return - the expression of M -> M*E | M/E | E
	 */
	private Expression parseM (String str) {
		Expression expression = null;
		final String multRegex = ".+\\*.+";
		final String divRegex = ".+/.+";
		// check if multRegex is in the string
		if (str.matches(multRegex)) {
			expression = parseHelper(str, '*', this::parseM, this::parseE);
		}
		// check if divRegex is in the string
		if (str.matches(divRegex) && expression == null) {
			expression = parseHelper(str, '/', this::parseM, this::parseE);
		}
		// if we can't find a expression then check E
		if (expression == null) {
			expression = parseE(str);
		}
		// the output expression for M
		return expression;
	}

	/**
	 * parse E according to the CFG (context free grammars) rules.
	 * @param str - the string to be parsed into the expression tree
	 * @return - the expression of E -> P^E | P
	 */
	protected Expression parseE (String str) {
		Expression expression = null;
		final String exponentRegex = ".+\\^.+";
		// check if exponentRegex is in the string
		if (str.matches(exponentRegex)) {
			expression = parseHelper(str, '^', this::parseParentheticalExpression, this::parseE);
		}
		// if we can't find a expression then check E
		if(expression == null){
			expression = parseParentheticalExpression(str);
		}
		// the output expression for E
		return expression;
	}

	/**
	 * Helper function for CFG (context free grammars). We look at different operations to be parsed for the CFG (context free grammars).
	 * @param string - the string to be parsed into a actual {@link Expression}
	 * @param op - is the operation to check when parsing the string
	 * @param m1 - the first method to check on the left side of the operation
	 * @param m2 - the second method to check on the right side of the operation
	 * @return - the {@link Expression} or null if the string couldn't be parsed with the operation
	 */
	private Expression parseHelper (String string,
								   char op,
								   Function<String, Expression> m1,
								   Function<String, Expression> m2) {
		// first set the output expression to null
		Expression output = null;
		for (int i = 0; i < string.length(); i++) {
			// check if the operation char is at the current index in the for loop
			if (string.charAt(i) == op) {
				// apply the current method to the expression and look at the left and right of the expression
				Expression left = m1.apply(string.substring(0, i));
				Expression right = m2.apply(string.substring(i + 1));
				if(left == null || right == null) continue;
				// check the operation with the respective class
				if (op == '+') {
					output = new AdditiveExpression(left, right);
				} else if (op == '-') {
					output = new SubtractiveExpression(left, right);
				} else if (op == '*') {
					output = new MultiplicativeExpression(left, right);
				} else if (op == '/') {
					output = new DivisionExpression(left, right);
				} else if (op == '^') {
					output = new ExponentExpression(left, right);
				}
			}
		}
		// output the expression
		return output;
	}

	protected Expression parseParentheticalExpression(String str) {
		Expression expression = null;
		final String parenRegex = "\\(.+\\)";
		if(str.matches(parenRegex)){
			Expression expr = parseStartExpression(str.substring(1, str.length()-1));
			if(expr != null)
				expression = new ParentheticalExpression(expr);
		}
		if(expression == null){
			expression = parseLiteralExpression(str);
		}
		if (expression == null){
			expression = parseVariableExpression(str);
		}
		return expression;
	}
	
	protected VariableExpression parseVariableExpression (String str) {
		if (str.equals("x")) {
			// TODO implement the VariableExpression class and uncomment line below
			return new VariableExpression();
		}
		return null;
	}

	protected LiteralExpression parseLiteralExpression (String str) {
		// From https://stackoverflow.com/questions/3543729/how-to-check-that-a-string-is-parseable-to-a-double/22936891:
		final String Digits     = "(\\p{Digit}+)";
		final String HexDigits  = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally 
		// signed decimal integer.
		final String Exp        = "[eE][+-]?"+Digits;
		final String fpRegex    =
		    ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
		    "[+-]?(" +         // Optional sign character
		    "NaN|" +           // "NaN" string
		    "Infinity|" +      // "Infinity" string

		    // A decimal floating-point string representing a finite positive
		    // number without a leading sign has at most five basic pieces:
		    // Digits . Digits ExponentPart FloatTypeSuffix
		    // 
		    // Since this method allows integer-only strings as input
		    // in addition to strings of floating-point literals, the
		    // two sub-patterns below are simplifications of the grammar
		    // productions from the Java Language Specification, 2nd 
		    // edition, section 3.10.2.

		    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
		    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

		    // . Digits ExponentPart_opt FloatTypeSuffix_opt
		    "(\\.("+Digits+")("+Exp+")?)|"+

		    // Hexadecimal strings
		    "((" +
		    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
		    "(0[xX]" + HexDigits + "(\\.)?)|" +

		    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
		    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

		    ")[pP][+-]?" + Digits + "))" +
		    "[fFdD]?))" +
		    "[\\x00-\\x20]*");// Optional trailing "whitespace"

		if (str.matches(fpRegex)) {
			// TODO implement the LiteralExpression class and uncomment line below
			return new LiteralExpression(str);
		}
		return null;
	}
}
