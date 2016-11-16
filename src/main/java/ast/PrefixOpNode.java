package ast;

/**
 * 前置的++和--
 * Created by sulvto on 16-11-15.
 */
public class PrefixOpNode extends UnaryArithmeticOpNode {
    String op;
    ExprNode exprNode;

    public PrefixOpNode(String op, ExprNode exprNode) {
        this.op = op;
        this.exprNode = exprNode;
    }
}
