package ast;

import type.Type;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypeNode extends Node {
    private boolean resolved;
    private Type type;

    public TypeNode(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String typeRef() {
    }

    public boolean isResolved() {
        return resolved;
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void _dump(Dumper d) {

    }
    public TypeNode accept(ASTVisitor visitor) {
        throw new Error("do not call TypeNode#accept");
    }
}
