interface ExpressionParser {
	/**
	 * Attempts to create an expression tree from the specified String.
	 * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @return the Expression object representing the parsed expression tree
	 */
	Expression parse (String str) throws ExpressionParseException;
}
