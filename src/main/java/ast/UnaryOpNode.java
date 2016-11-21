package ast;

import type.Type;

/**
 * 一元运算表达式（-x,+x...）
 * Created by sulvto on 16-11-15.
 */
public class UnaryOpNode extends ExprNode {
    private String operator;
    private ExprNode expr;
    private Type opType;

    public UnaryOpNode(String op, ExprNode exprNode) {
        this.operator = op;
        this.expr = exprNode;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Type getType() {
        return expr.getType();
    }

    public Type getOpType() {
        return opType;
    }

    public void setOpType(Type opType) {
        this.opType = opType;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public Location location() {
        return expr.location();
    }


    @Override
    protected void doDump(Dumper d) {
        d.printMember("operator", operator);
        d.printMember("expr", expr);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }


}
