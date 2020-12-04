interface Expression {
	/**
	 * Given the value of the dependent variable x, evaluate this expression.
	 * @param x the given value of x
	 * @return the value of the expression
	 */
	double evaluate (double x);

	/**
	 * Creates a String representation by recursively printing out (using indentation) the
	 * tree represented by this expression, starting at the specified indentation level.
	 * @param stringBuilder the StringBuilder to use for building the String representation
	 * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
	 */	
	void convertToString (StringBuilder stringBuilder, int indentLevel);

	/**
	 * Converts the expression to a string given a starting indentation level.
	 * @param indentLevel the starting indentation level
	 * @return the String representation of this Expression
	 */
	public default String convertToString (int indentLevel) {
		final StringBuilder stringBuilder = new StringBuilder();
		convertToString(stringBuilder, indentLevel);
		return stringBuilder.toString();
	}

	/**
	 * Static helper method to indent a specified number of times from the left margin, by
	 * appending tab characters to the specified StringBuilder.
	 * @param sb the StringBuilder to which to append tab characters.
	 * @param indentLevel the number of tabs to append.
	 */
	public static void indent (StringBuilder sb, int indentLevel) {
		for (int i = 0; i < indentLevel; i++) {
			sb.append('\t');
		}
	}
}
