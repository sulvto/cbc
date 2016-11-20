package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class DoWhileNode extends StmtNode {
    private StmtNode body;
    private ExprNode cond;

    public DoWhileNode(Location location,StmtNode body,ExprNode cond) {
        super(location);
        this.body = body;
        this.cond = cond;
    }

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

    @Override
    protected void doDump(Dumper d) {
        d.printMember("body", body);
        d.printMember("cond", cond);
    }
}
