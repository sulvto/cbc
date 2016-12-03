package ir;

/**
 * Created by sulvto on 16-11-21.
 */
public class Uni extends Expr {
    public Uni(Object asmType, Object internUnary, Expr expr) {
        // NUll
        super(null);
    }

    @Override
    protected void doDump(Dumper d) {

    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return null;
    }
}
