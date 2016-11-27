package ast;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class GotoNode extends StmtNode {
    protected String target;

    public GotoNode(Location location, String target) {
        super(location);
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("target", target);
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
