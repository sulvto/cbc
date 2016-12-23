package entity;

import asm.MemoryReference;
import asm.Operand;
import ast.*;
import type.Type;

/**
 * Created by sulvto on 16-11-14.
 */
public abstract class Entity implements Dumpable {

    protected boolean isPrivate;
    protected String name;
    protected TypeNode typeNode;
    private long nRefered;
    protected MemoryReference memref;
    private Operand address;

    public Entity(boolean priv, TypeNode type, String name) {
        this.name = name;
        this.isPrivate = priv;
        this.typeNode = type;
        this.nRefered = 0;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Type getType() {
        return getTypeNode().getType();
    }

    public long allocSize() {
        return getType().allocSize();
    }

    public long alignment() {
        return getType().alignment();
    }


    public String getName() {
        return name;
    }

    public Location location() {
        return typeNode.location();
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public ExprNode getValue() {
        throw new Error("Entity#value");
    }

    public boolean isParameter() {
        return false;
    }

    public void refered() {
        nRefered++;
    }

    public boolean isRefered() {
        return nRefered > 0;
    }

    public String symbolString() {
        return getName();
    }

    public abstract boolean isDefined();

    public abstract boolean isInitialized();

    public boolean isConstant() {
        return false;
    }

    public void setMemref(MemoryReference memref) {
        this.memref = memref;
    }

    public MemoryReference getMemref() {
        checkAddress();
        return memref;
    }

    public void setAddress(Operand address) {
        this.address = address;
    }

    public Operand address() {
        checkAddress();
        return address;
    }

    void checkAddress() {
        if (memref == null && address == null) {
            throw new Error("address did not resolved； " + name);
        }
    }

    public void dump(Dumper dumper) {
        dumper.printClass(this, location());
        doDump(dumper);
    }

    abstract protected void doDump(Dumper dumper);

    abstract public <T> T accept(EntityVisitor<T> visitor);
}
