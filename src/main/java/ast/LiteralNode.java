package ast;

import type.Type;
import type.TypeRef;

/**
 * 字面量
 * Created by sulvto on 16-11-15.
 */
public abstract class LiteralNode extends ExprNode {
    protected Location location;
    protected TypeNode typeNode;

    public LiteralNode(Location location, TypeRef ref) {
        this.location = location;
        this.typeNode = new TypeNode(ref);
    }

    @Override
    public Type getType() {
        return typeNode.getType();
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Location location() {
        return location;
    }
}
