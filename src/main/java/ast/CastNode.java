package ast;

import type.Type;

/**
 * 类型转换
 * Created by sulvto on 16-11-15.
 */
public class CastNode extends ExprNode {
    TypeNode typeNode;
    ExprNode expr;

    public CastNode(TypeNode t, ExprNode expr) {
        this.typeNode = t;
        this.expr = expr;
    }

    public CastNode(Type t, ExprNode expr) {
        this(new TypeNode(t), expr);
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type getType() {
        return typeNode.getType();
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    @Override
    public boolean isLvalue() {
        return expr.isLvalue();
    }

    @Override
    public boolean isAssignable() {
        return expr.isAssignable();
    }

    public boolean isEffectiveCast() {
        return getType().size() > expr.getType().size();
    }

    @Override
    public Location location() {
        return typeNode.location();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("typeNode", typeNode);
        d.printMember("expr", expr);
    }
}
