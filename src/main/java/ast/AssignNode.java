package ast;

/**
 * DONE
 * 赋值表达式
 * Created by sulvto on 16-11-15.
 */
public class AssignNode extends AbstractAssignNode {

    public AssignNode(ExprNode lhs, ExprNode rhs) {
        super(lhs,rhs);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }


}
