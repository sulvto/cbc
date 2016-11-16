package ast;

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

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    protected void _dump(Dumper d) {

    }
}
