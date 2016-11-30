package ast;

/**
 * &&
 * Created by sulvto on 16-11-15.
 */
public class LogicalAndNode extends BinaryOpNode {
    ExprNode left, right;

    @Override
    public ExprNode getRight() {
        return right;
    }

    @Override
    public ExprNode getLeft() {
        return left;
    }

    public LogicalAndNode(ExprNode left, ExprNode right) {
        super(left, "&&", right);
    }
}
