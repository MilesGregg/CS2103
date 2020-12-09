/**
 * Represents a parenthetical expression (an expression within parentheses)
 */
public class ParentheticalExpression implements Expression {
    Expression expr; // the expression stored within the parentheses

    /**
     * The constructor for a parenthetical expression
     * @param expr1 the expression stored within the parentheses
     */
    public ParentheticalExpression(Expression expr1) {
        this.expr = expr1;
    }

    /**
     * Evaluates the parenthetical expression (by evaluating the expression stored within the parentheses)
     * @param x the given value of x
     * @return the result of evaluating the inner expression at the given value of x
     */
    @Override
    public double evaluate(double x) {
        return expr.evaluate(x);
    }

    /**
     * convert the string to a expression
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append("()\n");
        stringBuilder.append(expr.convertToString(indentLevel+1));
    }

}
