package ast;

/**
 * Created by sulvto on 16-11-14.
 */
public class ExprStmtNode extends StmtNode {
    ExprNode exprNode;

    public ExprNode getExpr() {
        return exprNode;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
