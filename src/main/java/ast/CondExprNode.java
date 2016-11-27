package ast;

import type.Type;

/**
 * （a?b:c）
 * Created by sulvto on 16-11-15.
 */
public class CondExprNode extends ExprNode {
    ExprNode cond, thenExpr, elseExpr;

    public CondExprNode(ExprNode c, ExprNode t, ExprNode e) {
        this.cond = c;
        this.thenExpr = t;
        this.elseExpr = e;
    }

    public ExprNode getCond() {
        return cond;
    }

    public ExprNode getElseExpr() {
        return elseExpr;
    }

    public ExprNode getThenExpr() {
        return thenExpr;
    }

    public void setElseExpr(ExprNode elseExpr) {
        this.elseExpr = elseExpr;
    }

    @Override
    public Type getType() {
        return thenExpr.getType();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Location location() {
        return cond.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printMember("thenExpr", thenExpr);
        d.printMember("elseExpr", elseExpr);
    }

}
