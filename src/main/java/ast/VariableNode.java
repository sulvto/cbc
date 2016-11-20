package ast;

import entity.DefinedVariable;
import entity.Entity;
import type.Type;

/**
 * 变量表达式
 * Created by sulvto on 16-11-15.
 */
public class VariableNode extends LHSNode {
    private Location location;
    private String name;
    private Entity entity;

    public VariableNode(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public VariableNode(DefinedVariable variable) {
        this.entity = variable;
        this.name = variable.getName();
    }

    public String getName() {
        return name;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        if (entity == null) {
            throw new Error("VariableNode.entity == null");
        }
        return entity;
    }

    public boolean isResolved() {
        return entity != null;
    }

    public TypeNode typeNode() {
        return getEntity().getTypeNode();
    }

    @Override
    public boolean isParameter() {
        return getEntity().isParameter() ;
    }


    @Override
    public Type origType() {
        return getEntity().getType();
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    protected void doDump(Dumper d) {
        if (type != null) {
            d.printMember("type", type);
        }
        d.printMember("name",name,isResolved());
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
