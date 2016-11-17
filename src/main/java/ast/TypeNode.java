package ast;

import type.Type;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypeNode extends Node {
    boolean resolved;

    public TypeNode(Type type) {
        super();
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
