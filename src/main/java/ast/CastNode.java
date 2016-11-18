package ast;

import type.Type;

/**
 * 类型转换
 * Created by sulvto on 16-11-15.
 */
public class CastNode extends ExprNode {
    TypeNode typeNode;
    ExprNode exprNode;

    public CastNode(TypeNode t,ExprNode n) {
        this.typeNode = t;
        this.exprNode = n;
    }

    public ExprNode getExpr() {
        return exprNode;
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
