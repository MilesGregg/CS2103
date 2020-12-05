public class ParentheticalExpression implements Expression {
    Expression expr;

    public ParentheticalExpression(Expression expr1){
        this.expr = expr1;
    }

    @Override
    public double evaluate(double x) {
        return expr.evaluate(x);
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append("()\n");
        stringBuilder.append(expr.convertToString(indentLevel+1));
    }

}
