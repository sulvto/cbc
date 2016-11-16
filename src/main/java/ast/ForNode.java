package ast;

/**
 * Created by sulvto on 16-11-15.
 */
public class ForNode extends StmtNode {
    StmtNode init,incr,body;
    ExprNode cond;

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
}
