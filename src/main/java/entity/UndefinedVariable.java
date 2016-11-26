package entity;

import ast.Dumper;
import ast.TypeNode;

/**
 * Created by sulvto on 16-11-26.
 */
public class UndefinedVariable extends Variable {
    public UndefinedVariable(boolean priv, TypeNode type, String name) {
        super(priv, type, name);
    }

    @Override
    public boolean isDefined() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    protected void doDump(Dumper dumper) {

    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return null;
    }
}
