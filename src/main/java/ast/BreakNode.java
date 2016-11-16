package ast;

/**
 * Created by sulvto on 16-11-17.
 */
public class BreakNode extends StmtNode {
    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
