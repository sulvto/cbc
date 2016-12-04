package ir;

import asm.Type;

/**
 * Created by sulvto on 16-12-3.
 */
public class Mem extends Expr {
    private Expr expr;

    public Mem(Type type,Expr expr) {
        super(type);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public Expr addressNode(Type type) {
        return expr;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
