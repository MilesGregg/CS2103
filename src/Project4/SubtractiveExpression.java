public class SubtractiveExpression implements Expression {
    Expression expr1;
    Expression expr2;

    public SubtractiveExpression(Expression expr1, Expression expr2){
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public double evaluate(double x) {
        return expr1.evaluate(x) - expr2.evaluate(x);
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append("-\n");
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append(expr1.convertToString(indentLevel)).append("\n");
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append(expr2.convertToString(indentLevel));
    }

}
