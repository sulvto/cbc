package ir;

import asm.MemoryReference;
import asm.Operand;
import asm.Type;
import entity.Entity;

/**
 * Created by sulvto on 16-12-3.
 */
public class Var extends Expr {
    private Entity entity;

    public Var(Type type, Entity entity) {
        super(type);
        this.entity = entity;
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public Type getType() {
        if (super.getType() == null) {
            throw new Error("Var is too big to load by 1 insn");
        }
        return super.getType();
    }

    public String getName() {
        return entity.getName();
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public MemoryReference memref() {
        return entity.getMemref();
    }

    @Override
    public Addr addressNode(Type type) {
        return new Addr(type,entity);
    }

    @Override
    public Entity getEntityForce() {
        return entity;
    }

    @Override
    public Operand address() {
        return entity.address();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("entity", entity.getName());
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
