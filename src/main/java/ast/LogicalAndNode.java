package ast;

/**
 * &&
 * Created by sulvto on 16-11-15.
 */
public class LogicalAndNode extends BinaryOpNode {

    public LogicalAndNode(ExprNode left, ExprNode right) {
        super(left, "&&", right);
    }
}
