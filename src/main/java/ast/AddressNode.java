package ast;

/**
 * 地址表达式 （&x）
 * Created by sulvto on 16-11-15.
 */
public class AddressNode extends ExprNode{
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
