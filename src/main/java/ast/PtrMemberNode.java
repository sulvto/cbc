package ast;

import type.Type;

/**
 * 成员表达式 （ptr->memb）
 * Created by sulvto on 16-11-15.
 */
public class PtrMemberNode  extends LHSNode{
    ExprNode expr ;
    String memb;

    public PtrMemberNode(ExprNode exprNode, String memb) {
        this.expr = exprNode;
        this.memb = memb;
    }


    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type origType() {
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
