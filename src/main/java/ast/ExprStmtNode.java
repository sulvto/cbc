package ast;

/**
 * DONE
 * Created by sulvto on 16-11-14.
 */
public class ExprStmtNode extends StmtNode {
    ExprNode expr;

    public ExprStmtNode(Location location, ExprNode expr) {
        super(location);
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);

    }
}
