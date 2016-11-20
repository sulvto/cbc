package ast;

import type.Type;

/**
 * 一元运算表达式（-x,+x...）
 * Created by sulvto on 16-11-15.
 */
public class UnaryOpNode extends ExprNode {
    String op;
    ExprNode expr;

    public UnaryOpNode(String op, ExprNode exprNode) {
        this.op = op;
        this.expr = exprNode;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }


    @Override
    protected void doDump(Dumper d) {

    }


}
