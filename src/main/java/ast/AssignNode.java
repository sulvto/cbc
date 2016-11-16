package ast;

/**
 * 赋值表达式
 * Created by sulvto on 16-11-15.
 */
public class AssignNode extends AbstractAssignNode {
    ExprNode lhs, rhs;

    public AssignNode(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public ExprNode getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
