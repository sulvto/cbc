package ast;

/**
 * 字面量
 * Created by sulvto on 16-11-15.
 */
public class LiteralNode extends ExprNode {
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    protected void _dump(Dumper d) {

    }
}
