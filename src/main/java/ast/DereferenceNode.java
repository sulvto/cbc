package ast;

/**
 * 指针表达式 （*ptr）
 * Created by sulvto on 16-11-15.
 */
public class DereferenceNode  extends LHSNode{
    ExprNode expr;

    public ExprNode getExpr() {
        return expr;
    }
}
