package ast;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class SwitchNode extends StmtNode {
    ExprNode cond;
    List<StmtNode> cases;

    public List<StmtNode> getCases() {
        return cases;
    }

    public ExprNode getCond() {
        return cond;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
