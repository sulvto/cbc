package ast;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class CaseNode extends StmtNode{
    List<ExprNode> values;
    StmtNode body;

    public List<ExprNode> getValues() {
        return values;
    }

    public StmtNode getBody() {
        return body;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
