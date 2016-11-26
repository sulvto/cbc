package ast;

import type.Type;
import type.TypeRef;

/**
 * 表示结构体成员的节点
 * Created by sulvto on 16-11-15.
 */
public class Slot extends Node {
    private TypeNode typeNode;
    private String name;
    private long offset;

    public Slot(TypeNode typeNode, String name) {
        this.typeNode = typeNode;
        this.name = name;
        offset = Type.sizeUnknown;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public TypeRef getTypeRef() {
        return typeNode.getTypeRef();
    }

    public Type getType() {
        return typeNode.getType();
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }

    public long size() {
        return getType().size();
    }

    public long alignment() {
        return getType().alignment();
    }

    public long allocSize() {
        return getType().allocSize();
    }

    public String getName() {
        return name;
    }

    @Override
    public Location location() {
        return typeNode.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("name", name);
        d.printMember("typeNode", typeNode);
    }
}
