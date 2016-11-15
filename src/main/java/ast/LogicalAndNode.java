package ast;

/**
 * &&
 * Created by sulvto on 16-11-15.
 */
public class LogicalAndNode  extends BinaryOpNode{
    public LogicalAndNode(ExprNode left, String op, ExprNode right) {
        super(left, op, right);
    }
}
