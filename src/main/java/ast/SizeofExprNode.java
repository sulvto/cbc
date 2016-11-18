package ast;

import type.Type;

/**
 * 计算表达式的sizeof的表达式
 * Created by sulvto on 16-11-15.
 */
public class SizeofExprNode extends ExprNode {
    ExprNode expr;

    public ExprNode getExpr() {
        return expr;
    }

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
