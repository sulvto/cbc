package ir;

import ast.Location;

/**
 * Created by sulvto on 16-12-3.
 */
public class Assign extends Stmt {
    private Expr lhs, rhs;

    public Assign(Location location, Expr lhs, Expr rhs) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expr getLhs() {
        return lhs;
    }

    public Expr getRhs() {
        return rhs;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("lhs", lhs);
        d.printMember("rhs", rhs);
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
