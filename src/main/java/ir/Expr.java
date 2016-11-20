package ir;

import ast.Dumpable;
import ast.Dumper;
import entity.Entity;
import type.Type;

/**
 * Created by sulvto on 16-11-18.
 */
public abstract class Expr implements Dumpable {
    final Type type;

    Expr(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isVar() {
        return false;
    }

    public boolean isAddr() {
        return false;
    }

    public boolean isConstant() {
        return false;
    }

    public ImmediateValue asmValue() {
        throw new Error("Expr#asmValue called");
    }

    public Operand address() {
        throw new Error("Expr#address called");
    }

    public MemoryReference memref() {
        throw new Error("Expr#memref called");
    }

    public Expr addressNode(Type type) {
        throw new Error("unexpected node for us: " + getClass());
    }

    public Entity getEntityForce() {
        return null;
    }

    @Override
    public void dump(Dumper d) {
        d.printClass(this);
        d.printMember("type", type);
        doDump(d);
    }

    public abstract <S, E> E accept(IRVisitor<S, E> visitor);

    protected abstract void doDump(Dumper d);
}
