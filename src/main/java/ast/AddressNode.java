package ast;

import type.Type;

/**
 * 地址表达式 （&x）
 * Created by sulvto on 16-11-15.
 */
public class AddressNode extends ExprNode {
    final ExprNode expr;
    Type type;

    public AddressNode(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public void setType(Type type) {
        if (type != null) {
            throw new Error("type set twice");
        }
        this.type = type;
    }

    @Override
    public Type getType() {
        if (type == null) {
            throw new Error("type is null");
        }
        return type;
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
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
