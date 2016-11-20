package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class WhileNode extends StmtNode {
    private ExprNode cond;
    private StmtNode body;

    public WhileNode(Location location,ExprNode cond,StmtNode body) {
        super(location);
        this.cond = cond;
        this.body = body;
    }

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

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printMember("body", body);
    }
}
