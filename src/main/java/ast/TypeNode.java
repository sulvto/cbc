package ast;

import type.Type;
import type.TypeRef;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypeNode extends Node {
    private TypeRef typeRef;
    private Type type;

    public TypeNode(Type type) {
        this.type = type;
    }

    public TypeNode(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (this.type != null) {
            throw new Error("TypeNode#setType called twice");
        }
        this.type = type;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    public boolean isResolved() {
        return type != null;
    }

    @Override
    public Location location() {
        return typeRef == null ? null : typeRef.getLocation();
    }

    @Override
    protected void _dump(Dumper d) {
        d.printMember("typeref", typeRef);
        d.printMember("type", type);
    }

    public TypeNode accept(ASTVisitor visitor) {
        throw new Error("do not call TypeNode#accept");
    }
}
