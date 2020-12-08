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
	public Expression parse (String str) throws ExpressionParseException {
		str = str.replaceAll(" ", "");
		Expression expression = parseStartExpression(str);
		if (expression == null) {
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}

		return expression;
	}
	
	protected Expression parseStartExpression (String str) {
		Expression expression = null;
		// TODO implement this method, helper methods, classes that implement Expression, etc.
		 expression = parseA(str);
		 if (expression == null) {
		 	expression = parseP(str);
		 }

		return expression;
	}

	private Expression parseA(String str) {
		System.out.println("Parse A String: " + str);
		Expression expression = null;
		final String addRegex = ".+\\+.+";
		final String subRegex = ".+-.+";
		if (str.matches(addRegex)) {
			expression = parseHelper(str, '+', this::parseA, this::parseM);
		}
		if (str.matches(subRegex) && expression == null) {
			expression = parseHelper(str, '-', this::parseA, this::parseM);
		}
		if (expression == null) {
			expression = parseM(str);
		}

		return expression;
	}

	private Expression parseM(String str) {
		Expression expression = null;
		final String multRegex = ".+\\*.+";
		final String divRegex = ".+/.+";
		if (str.matches(multRegex)) {
			expression = parseHelper(str, '*', this::parseM, this::parseE);
		}
		if (str.matches(divRegex) && expression == null) {
			expression = parseHelper(str, '/', this::parseM, this::parseE);
		}
		if (expression == null) {
			expression = parseE(str);
		}

		return expression;
	}

	protected Expression parseE(String str) {
		Expression expression = null;
		final String exponentRegex = ".+\\^.+";
		if (str.matches(exponentRegex)) {
			expression = parseHelper(str, '^', this::parseP, this::parseE);
		}
		if(expression == null){
			expression = parseP(str);
		}
		return expression;
	}

	private Expression parseHelper(String string,
								   char op,
								   Function<String, Expression> m1,
								   Function<String, Expression> m2) {
		Expression output = null;
		System.out.println("String: " + string);
		System.out.println("Op: " + op);
		for (int i = 0; i < string.length(); i++) {

			if (string.charAt(i) == op) {
				Expression left = m1.apply(string.substring(0, i));
				Expression right = m2.apply(string.substring(i + 1));
				if(left == null || right == null) continue;
				output = new BinaryExpression(left, right, op);
			}
		}
		return output;
	}

	protected Expression parseP(String str) {
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





	protected VariableExpression parseVariableExpression (String str) {
		if (str.equals("x")) {
			System.out.println("VARIABLE x");
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
			System.out.println("LITERAL: "+str);
			// TODO implement the LiteralExpression class and uncomment line below
			return new LiteralExpression(str);
		}
		return null;
	}
}
