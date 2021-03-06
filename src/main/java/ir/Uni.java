package ir;

import asm.Type;

/**
 * Created by sulvto on 16-11-21.
 */
public class Uni extends Expr {
    private Op op;
    private Expr expr;

    public Uni(Type type, Op op, Expr expr) {
        super(type);
        this.op = op;
        this.expr = expr;
    }

    public Op getOp() {
        return op;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("op", op.toString());
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
