package ast;

import type.Type;
import type.TypeRef;

/**
 * 计算类型的sizeof的表达式
 * Created by sulvto on 16-11-15.
 */
public class SizeofTypeNode extends ExprNode {
    private TypeNode operand;
    private TypeNode type;

    public SizeofTypeNode(TypeNode operand, TypeRef type) {
        this.operand = operand;
        this.type = new TypeNode(type);
    }

    public Type operand() {
        return operand.getType();
    }

    @Override
    public Type getType() {
        return type.getType();
    }

    public TypeNode getOperandTypeNode() {
        return operand;
    }

    public TypeNode getTypeNode() {
        return type;
    }

    @Override
    public Location location() {
        return operand.location();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("operand", operand);
    }
}
