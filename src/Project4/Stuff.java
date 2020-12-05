import java.util.function.Function;

public class Stuff {

    private Expression parseHelper(String string,
                                   char op,
                                   Function<String, Expression> m1,
                                   Function<String, Expression> m2) {
        Expression output = null;
        for (int i = 0; i < string.length(); i++) {
            Expression left = m1.apply(string.substring(0, i));
            Expression right = m2.apply(string.substring(i + 1));
            if (string.charAt(i) == op && left != null && right != null) {
                if (op == '+') {
                    output = new AdditiveExpression(left, right);
                } else if (op == '-') {
                    output = new SubtractiveExpression(left, right);
                } else if (op == '*') {
                    output = new MultiplicativeExpression(left, right);
                } else if (op == '/') {
                    output = new DivisionExpression(left, right);
                }
            }
        }
        return output;
    }
}
