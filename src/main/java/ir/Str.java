package ir;

import asm.*;
import entity.ConstantEntry;

/**
 * Created by sulvto on 16-12-3.
 */
public class Str extends Expr {
    private ConstantEntry entry;

    public Str(Type type, ConstantEntry entry) {
        super(type);
        this.entry = entry;
    }

    public ConstantEntry getEntry() {
        return entry;
    }

    public Symbol getSymbol() {
        return entry.getSymbol();
    }

    @Override
    public MemoryReference memref() {
        return entry.getMemref();
    }

    @Override
    public Operand address() {
        return entry.getAddress();
    }

    @Override
    public ImmediateValue asmValue() {
        return entry.getAddress();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("entry", entry.toString());
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
