package ast;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class ForNode extends StmtNode {
    protected StmtNode init, incr, body;
    protected ExprNode cond;

    public ForNode(Location location, ExprNode init, ExprNode cond, ExprNode incr, StmtNode body) {
        super(location);
        this.init = new ExprStmtNode(init.location(), init);
        this.cond = cond;
        this.incr = new ExprStmtNode(incr.location(), incr);
    }

    public ExprNode getCond() {
        return cond;
    }

    public StmtNode getBody() {
        return body;
    }

    public StmtNode getIncr() {
        return incr;
    }

    public StmtNode getInit() {
        return init;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("init", init);
        d.printMember("cond", cond);
        d.printMember("incr", incr);
        d.printMember("body", body);
    }
}
