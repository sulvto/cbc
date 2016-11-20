package ast;

import type.Type;

/**
 * 成员表达式 （s.memb）
 * Created by sulvto on 16-11-15.
 */
public class MemberNode extends LHSNode {
    ExprNode expr ;
    String memb;

    public MemberNode(ExprNode exprNode, String memb) {
        this.expr = exprNode;
        this.memb = memb;
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
