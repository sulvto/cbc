package ir;

import asm.ImmediateValue;
import asm.IntegerLiteral;
import asm.MemoryReference;
import asm.Type;

/**
 * Created by sulvto on 16-11-22.
 */
public class Int extends Expr {
    private long value;

    public Int(Type type, long value) {
        super(type);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public ImmediateValue asmValue() {
        return new ImmediateValue(new IntegerLiteral(value));
    }

    @Override
    public MemoryReference memref() {
        throw new Error("must not happen: IntValue#memref");
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("value", value);
    }
}
