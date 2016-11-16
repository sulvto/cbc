package ast;

/**
 * 复合赋值表达式 （+=,-=...）
 * Created by sulvto on 16-11-15.
 */
public class OpAssignNode extends AbstractAssignNode {
    ExprNode lhs,rhs;
    String op;

    public OpAssignNode(ExprNode lhs, String op, ExprNode rhs) {
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }

    public ExprNode getLhs() {
        return lhs;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
