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

    // isMultiDimension a[x][y][z] = true
    // isMultiDimension a[x][y] = true
    // isMultiDimension a[x] = false
    public boolean isMultiDimension() {
        return (expr instanceof ArefNode) && !expr.origType().isPointer();
    }

    public ExprNode baseExpr() {
        return isMultiDimension() ? ((ArefNode) expr).baseExpr() : expr;
    }

    public long elementSize() {
        return origType().allocSize();
    }

    public Type length() {
        return ((ArefNode) expr).origType().length();
    }

    @Override
    protected Type origType() {
        return expr.origType().getBaseType();
    }

    @Override
    public Location location() {
        return expr.location();
    }

    @Override
    protected void doDump(Dumper d) {
        if (type != null) {
            d.printMember("type", type);
        }
        d.printMember("expr", expr);
        d.printMember("index", index);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
