package ast;

/**
 * DONE
 * Created by sulvto on 16-11-17.
 */
public class BreakNode extends StmtNode {
    public BreakNode(Location location) {
        super(location);
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {

    }
}
