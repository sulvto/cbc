package ast;

import type.Type;

/**
 * 指针表达式 （*ptr）
 * Created by sulvto on 16-11-15.
 */
public class DereferenceNode  extends LHSNode{
    ExprNode expr;

    public DereferenceNode(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type origType() {
        return null;
    }


    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {

    }

}
