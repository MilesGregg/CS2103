public class BinaryExpression implements Expression {
    Expression left;
    Expression right;
    char op;

    public BinaryExpression(Expression left, Expression right, char op){
        this.left = left;
        this.right = right;
        this.op = op;
    }


    @Override
    public double evaluate(double x) {
        double leftEval = left.evaluate(x);
        double rightEval = right.evaluate(x);
        switch(op){
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

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append(op).append("\n");
        stringBuilder.append(left.convertToString(indentLevel));
        stringBuilder.append(right.convertToString(indentLevel));
    }
}
