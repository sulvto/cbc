package ast;

/**
 * 类型转换
 * Created by sulvto on 16-11-15.
 */
public class CastNode extends ExprNode {
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
