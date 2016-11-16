package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class ReturnNode extends StmtNode {
    ExprNode expr;

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
