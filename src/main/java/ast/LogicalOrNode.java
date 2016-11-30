package ast;

/**
 * ||
 * Created by sulvto on 16-11-15.
 */
public class LogicalOrNode extends BinaryOpNode {
    ExprNode left, right;

    @Override
    public ExprNode getLeft() {
        return left;
    }

    @Override
    public ExprNode getRight() {
        return right;
    }

    public LogicalOrNode(ExprNode left, ExprNode right) {
        super(left, "||", right);
    }
}
