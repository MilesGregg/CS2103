public class BinaryExpression implements Expression {
    private final Expression left; // left expression
    private final Expression right; // right expression
    private final char op; // current operation we are trying

    /**
     * Binary Expression for all operations
     * @param left - left expression
     * @param right - right expression
     * @param op - current operation we are recursing on
     */
    public BinaryExpression(Expression left, Expression right, char op){
        this.left = left;
        this.right = right;
        this.op = op;
    }

    /**
     * Evaluate the expression of x depending on operation we are trying to user
     * @param x the given value of x
     * @return - the output for the operation
     */
    @Override
    public double evaluate(double x) {
        double leftEval = left.evaluate(x);
        double rightEval = right.evaluate(x);
        switch (op) {
            case '+':
                return leftEval + rightEval;
            case '-':
                return leftEval - rightEval;
            case '*':
                return leftEval * rightEval;
            case '/':
                return leftEval / rightEval;
            case '^':
                return Math.pow(leftEval, rightEval);
            default:
                return 0;
        }
    }

    /**
     * convert the string to a expression
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append(op).append("\n");
        stringBuilder.append(left.convertToString(indentLevel));
        stringBuilder.append(right.convertToString(indentLevel));
    }
}
