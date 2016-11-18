package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class IfNode extends StmtNode {
    ExprNode cond;
    StmtNode thenBody;
    StmtNode elseBody;

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
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
