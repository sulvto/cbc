package ast;

import type.Type;

/**
 * 二元运算表达式 （x+y,x-y）
 * Created by sulvto on 16-11-15.
 */
public class BinaryOpNode extends ExprNode {
    String operator;
    ExprNode left, right;
    Type type;

    public BinaryOpNode(ExprNode left, String op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    public BinaryOpNode(Type type, ExprNode left, String op, ExprNode right) {
        super();
        this.type = type;
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public Type getType() {
        return (type != null) ? type : left.getType();
    }

    public void setType(Type type) {
        if (this.type != null) {
            throw new Error("BinaryOp#setType called twice");
        }
        this.type = type;
    }


    public ExprNode getLeft() {
        return left;
    }

    public void setLeft(ExprNode left) {
        this.left = left;
    }

    public ExprNode getRight() {
        return right;
    }

    public void setRight(ExprNode right) {
        this.right = right;
    }

    public Location getLocation() {
        return left.location();
    }

    @Override
    public Location location() {
        return left.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("operator", operator);
        d.printMember("left", left);
        d.printMember("right", right);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
