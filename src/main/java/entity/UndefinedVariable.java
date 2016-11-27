package entity;

import ast.Dumper;
import ast.TypeNode;

/**
 * Created by sulvto on 16-11-26.
 */
public class UndefinedVariable extends Variable {
    public UndefinedVariable(TypeNode type, String name) {
        super(false, type, name);
    }

    @Override
    public boolean isDefined() {
        return false;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    protected void doDump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("isPrivate", isPrivate());
        dumper.printMember("typeNode",typeNode);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
