/**
 * Represents a Variable Expression (the only variable that can be used is "x")
 */
public class VariableExpression implements Expression {

    // Because Variable Expressions are always in the form "x", there is no need to store any instance
    // variables in this class.

    /**
     * Evaluates the expression at the given value of x (by just returning x itself)
     * @param x the given value of x
     * @return the value of the expression (the value of x)
     */
    @Override
    public double evaluate(double x) {
        return x;
    }

    /**
     * convert the string to a expression
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append("x").append("\n");
    }

}
