package ast;

/**
 * DONE
 * 复合赋值表达式 （+=,-=...）
 * Created by sulvto on 16-11-15.
 */
public class OpAssignNode extends AbstractAssignNode {
    protected String operator;

    public OpAssignNode(ExprNode lhs, String op, ExprNode rhs) {
        super(lhs,rhs);
        this.operator = op;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
