package ast;

/**
 * 数组表达式（a[i]）
 * Created by sulvto on 16-11-15.
 */
public class ArefNode extends LHSNode {
    ExprNode expr, idx;

    public ArefNode(ExprNode expr, ExprNode idx) {
        this.expr = expr;
        this.idx = idx;
    }
}
