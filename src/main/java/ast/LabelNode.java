package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class LabelNode extends StmtNode {
    StmtNode stmt;

    public StmtNode getStmt() {
        return stmt;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
