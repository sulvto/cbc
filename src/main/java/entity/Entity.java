package entity;

import ast.Dumpable;
import ast.Dumper;
import ast.Location;
import ast.TypeNode;
import type.Type;

/**
 * Created by sulvto on 16-11-14.
 */
public abstract class Entity implements Dumpable {

    protected boolean isPrivate;
    protected String name;
    protected TypeNode typeNode;
    private long nRefered;
    private MemoryReference memory;
    private Operand address;

    public Entity(boolean priv, TypeNode type, String name) {
        this.name = name;
        this.isPrivate = priv;
        this.typeNode = type;
        this.nRefered = 0;
    }

    public TypeNode typeNode() {
        return typeNode;
    }

    public Type type() {
        return typeNode().getType();
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

    public void setMemory(MemoryReference memory) {
        this.memory = memory;
    }

    public MemoryReference memory() {
        checkAddress();
        return memory;
    }

    public void setAddress(Operand address) {
        this.address = address;
    }

    public Operand address() {
        checkAddress();
        return address;
    }

    void checkAddress() {
        if (memory == null && address == null) {
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
