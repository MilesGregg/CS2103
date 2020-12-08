/**
 * Represents a literal expression (a double value)
 */
public class LiteralExpression implements Expression {

    double value; // the decimal value of the literal expression

    /**
     * Constructs a literal expression, converting a string into a double and storing its value
     * @param str the double value as a string
     */
    public LiteralExpression(String str){
        value = Double.parseDouble(str);
    }

    /**
     * Evaluates the expression (by returning the value)
     * @param x the given value of x (not actually used in a literal expression)
     * @return the value stored in the expression
     */
    @Override
    public double evaluate(double x) {
        return value;
    }

    /**
     * convert the string to a expression
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append(value).append("\n");
    }

}
