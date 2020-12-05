public class LiteralExpression implements Expression {
    double value;
    public LiteralExpression(String str){
        value = Double.parseDouble(str);
    }

    @Override
    public double evaluate(double x) {
        return value;
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        Expression.indent(stringBuilder, indentLevel);
        indentLevel++;
        stringBuilder.append(value);
    }

}
