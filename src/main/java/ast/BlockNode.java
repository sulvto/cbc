package ast;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class BlockNode extends StmtNode {

    List<DefinedVariable> variables;
    List<StmtNode> stmtNodes;

    public List<StmtNode> getStmtNodes() {
        return stmtNodes;
    }

    public List<DefinedVariable> getVariables() {
        return variables;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
