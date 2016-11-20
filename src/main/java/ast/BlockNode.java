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
    private List<StmtNode> stmts;

    public BlockNode(Location location, List<DefinedVariable> vars, List<StmtNode> stmts) {
        super(location);
        this.variables = vars;
        this.stmts= stmts;
    }

    public List<StmtNode> getStmts() {
        return stmts;
    }

    public List<DefinedVariable> getVariables() {
        return variables;
    }

    public LocalScope getScope() {
        return scope;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }


    @Override
    protected void doDump(Dumper d) {
        d.printNodeList("variables", variables);
        d.printNodeList("stmts", stmts);
    }
}
