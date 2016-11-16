package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class GotoNode extends StmtNode {
    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
