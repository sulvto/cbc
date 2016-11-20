package ast;

import type.Type;

/**
 * 计算类型的sizeof的表达式
 * Created by sulvto on 16-11-15.
 */
public class SizeofTypeNode extends ExprNode{
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {

    }
}
