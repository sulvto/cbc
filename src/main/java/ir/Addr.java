package ir;

import asm.MemoryReference;
import asm.Operand;
import asm.Type;
import entity.Entity;

/**
 * Created by sulvto on 16-12-3.
 */
public class Addr extends Expr {
    private Entity entity;

    public Addr(Type type, Entity entity) {
        super(type);
        this.entity = entity;
    }

    @Override
    public boolean isAddr() {
        return true;
    }

    @Override
    public Operand address() {
        return entity.address();
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public MemoryReference memref() {
        return entity.getMemref();
    }

    @Override
    public Entity getEntityForce() {
        return entity;
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
