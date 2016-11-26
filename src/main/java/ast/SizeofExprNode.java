package ast;

import type.Type;
import type.TypeRef;

/**
 * 计算表达式的sizeof的表达式
 * Created by sulvto on 16-11-15.
 */
public class SizeofExprNode extends ExprNode {
    private ExprNode expr;
    private TypeNode type;

    public SizeofExprNode(ExprNode expr, TypeRef type) {
        this.expr = expr;
        this.type = new TypeNode(type);
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type getType() {
        return type.getType();
    }

    public TypeNode getTypeNode() {
        return type;
    }

    @Override
    public Location location() {
        return expr.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
