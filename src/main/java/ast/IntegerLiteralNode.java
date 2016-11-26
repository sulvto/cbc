package ast;

import type.TypeRef;

/**
 * 整数字面量
 * Created by sulvto on 16-11-15.
 */
public class IntegerLiteralNode extends LiteralNode {
    private long value;

    public IntegerLiteralNode(Location location, TypeRef ref, long value) {
        super(location, ref);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("typeNode", typeNode);
        d.printMember("value", value);
    }
}
