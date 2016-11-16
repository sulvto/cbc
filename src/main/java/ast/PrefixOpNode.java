package ast;

/**
 * 前置的++和--
 * Created by sulvto on 16-11-15.
 */
public class PrefixOpNode extends UnaryArithmeticOpNode {
    String op;
    ExprNode expr;

    public PrefixOpNode(String op, ExprNode exprNode) {
        super(op,exprNode);
        this.op = op;
        this.expr = exprNode;
    }

    @Override
    public ExprNode getExpr() {
        return expr;
    }
}
