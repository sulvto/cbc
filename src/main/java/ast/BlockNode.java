package ast;

import entity.DefinedVariable;
import entity.LocalScope;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class BlockNode extends StmtNode {

    private LocalScope scope;
    private List<DefinedVariable> variables;
    private List<StmtNode> stmtNodes;

    public BlockNode(Location location, List<DefinedVariable> vars, List<StmtNode> stmts) {
        super(location);
        this.variables = vars;
        this.stmtNodes = stmts;
    }

    public List<StmtNode> getStmtNodes() {
        return stmtNodes;
    }

    public List<DefinedVariable> getVariables() {
        return variables;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }


}
