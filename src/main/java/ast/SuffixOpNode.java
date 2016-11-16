package ast;

/**
 * 后置的++和--
 * Created by sulvto on 16-11-15.
 */
public class SuffixOpNode extends UnaryArithmeticOpNode {
    String op;
    ExprNode expr;

    public SuffixOpNode(String op, ExprNode exprNode) {
        this.op = op;
        this.expr = exprNode;
    }

    @Override
    public ExprNode getExpr() {
        return expr;
    }
}
