package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class ReturnNode extends StmtNode {
    protected ExprNode expr;

    public ReturnNode(Location location, ExprNode expr) {
        super(location);
        this.expr = expr;
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
