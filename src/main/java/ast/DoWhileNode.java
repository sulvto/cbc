package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class DoWhileNode extends StmtNode {
    StmtNode body;
    ExprNode cond;

    public ExprNode getCond() {
        return cond;
    }

    public StmtNode getBody() {
        return body;
    }
    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
