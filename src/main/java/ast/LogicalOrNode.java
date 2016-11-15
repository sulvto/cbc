package ast;

/**
 * ||
 * Created by sulvto on 16-11-15.
 */
public class LogicalOrNode extends BinaryOpNode {
    public LogicalOrNode(ExprNode left, String op, ExprNode right) {
        super(left, op, right);
    }
}
