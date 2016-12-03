package asm;

/**
 * 立即数
 * Created by sulvto on 16-11-26.
 */
public class ImmediateValue extends Operand {
    private Literal expr;

    public ImmediateValue(long n) {
//        this(new IntegerLiteral(n));
    }
    public ImmediateValue(Literal expr) {
        this.expr = expr;
    }

    public Literal getExpr() {
        return expr;
    }

    @Override
    public String toSource(SymbolTable table) {
        return null;
    }

    @Override
    public String dump() {
        return null;
    }

    @Override
    public void collectStatistics(Statistics stats) {

    }
}
