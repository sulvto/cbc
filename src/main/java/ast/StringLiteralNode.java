package ast;

import entity.ConstantEntry;
import type.TypeRef;

/**
 * 字符串字面量
 * Created by sulvto on 16-11-15.
 */
public class StringLiteralNode extends LiteralNode {
    private String value;
    private ConstantEntry entry;

    public StringLiteralNode(Location location, TypeRef ref, String value) {
        super(location, ref);
        this.value = value;
    }

    public ConstantEntry getEntry() {
        return entry;
    }

    public void setEntry(ConstantEntry entry) {
        this.entry = entry;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("value", value);
    }
}
