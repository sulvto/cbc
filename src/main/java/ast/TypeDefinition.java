package ast;

import type.Type;
import type.TypeRef;

/**
 * 类型定义
 * Created by sulvto on 16-11-14.
 */
public abstract class TypeDefinition extends Node {
    private Location location;
    protected String name;
    private TypeNode typeNode;

    public TypeDefinition(Location location, TypeRef typeRef, String name) {
        this.location = location;
        this.name = name;
        this.typeNode = new TypeNode(typeRef);
    }

    public String getName() {
        return name;
    }

    @Override
    public Location location() {
        return location;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public TypeRef getTypeRef() {
        return typeNode.typeRef();
    }

    public Type getType() {
        return typeNode.getType();
    }

    public abstract Type definingType();

    public abstract <T> T accept(DeclarationVisitor<T> visitor);

}
