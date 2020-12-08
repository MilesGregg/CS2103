/**
 * This class represents a binary expression, which is an expression with a binary operation
 * (such as addition, subtraction, multiplication, division, and exponentiation)
 */
public class BinaryExpression implements Expression {
    private final Expression left; // the left expression
    private final Expression right; // the right expression
    private final char op; // operation being applied to the left and right expressions

    /**
     * Constructor for a binary expression
     * @param left - the left expression
     * @param right - the right expression
     * @param op - a char representing the operation being applied to the left and right expressions
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
        final double leftEval = left.evaluate(x);
        final double rightEval = right.evaluate(x);
        // for each character, applies the corresponding operation on the 2 evaluated expressions
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
                // if the operation character is not any of the 5 listed above, something has gone wrong
                // and an error is thrown (this should never happen)
                throw new RuntimeException("Invalid operation");
        }
    }

    /**
     * convert the string to a expression
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        // indents the operation character at the current indent level
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append(op).append("\n");
        // indents the 2 expressions at one indent level further
        indentLevel++;
        stringBuilder.append(left.convertToString(indentLevel));
        stringBuilder.append(right.convertToString(indentLevel));
    }
}
