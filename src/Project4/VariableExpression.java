public class VariableExpression implements Expression {
    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        stringBuilder.append("x");
    }

}
