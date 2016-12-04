package ir;

import ast.Location;

/**
 * Created by sulvto on 16-12-3.
 */
public class Return extends Stmt {
    private Expr expr;

    public Return(Location location,Expr expr) {
        super(location);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
