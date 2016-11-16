package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class WhileNode extends StmtNode {
    ExprNode cond;
    StmtNode body;

    public StmtNode getBody() {
        return body;
    }

    public ExprNode getCond() {
        return cond;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
