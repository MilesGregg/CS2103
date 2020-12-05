public class ExponentExpression implements Expression {
    Expression expr1;
    Expression expr2;

    public ExponentExpression(Expression expr1, Expression expr2){
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public double evaluate(double x) {
        return Math.pow(expr1.evaluate(x), expr2.evaluate(x));
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append("^\n");
        stringBuilder.append(expr1.convertToString(indentLevel));
        stringBuilder.append(expr2.convertToString(indentLevel));
    }

}
