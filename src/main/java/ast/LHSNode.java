package ast;

/**
 * 能够成为赋值的左值的节点
 * Created by sulvto on 16-11-15.
 */
public class LHSNode extends ExprNode {
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
