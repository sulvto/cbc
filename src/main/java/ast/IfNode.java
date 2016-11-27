package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class IfNode extends StmtNode {
    protected ExprNode cond;
    protected StmtNode thenBody;
    protected StmtNode elseBody;

    public IfNode(Location location, ExprNode cond, StmtNode thenBody, StmtNode elseBody) {
        super(location);
        this.cond = cond;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public StmtNode getElseBody() {
        return elseBody;
    }

    public StmtNode getThenBody() {
        return thenBody;
    }

    public ExprNode getCond() {
        return cond;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printMember("thenBody", thenBody);
        d.printMember("elseBody", elseBody);
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
