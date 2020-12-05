public class ParentheticalExpression implements Expression {
    Expression expr;

    @Override
    public double evaluate(double x) {
        return expr.evaluate(x);
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append("()\n");
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append(expr.convertToString(indentLevel));
    }

}
