package ast;

/**
 * 赋值
 * Created by sulvto on 16-11-15.
 */
public class AbstractAssignNode extends ExprNode {
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
