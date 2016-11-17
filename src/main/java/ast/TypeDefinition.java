package ast;

/**
 * 类型定义
 * Created by sulvto on 16-11-14.
 */
public abstract class TypeDefinition extends Node {
    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void _dump(Dumper d) {

    }
    abstract public <T> T accept(DeclarationVisitor<T> visitor);

}
