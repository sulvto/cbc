package ast;

import type.Type;

/**
 * 数组表达式（a[i]）
 * Created by sulvto on 16-11-15.
 */
public class ArefNode extends LHSNode {
    ExprNode expr, index;

    public ArefNode(ExprNode expr, ExprNode idx) {
        this.expr = expr;
        this.index = idx;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public ExprNode getIndex() {
        return index;
    }


    @Override
    public Type origType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {

    }
}
