package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypeNode extends Node {
    boolean resolved;

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
}
