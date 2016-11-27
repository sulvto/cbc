
package ast;

import java.util.List;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class SwitchNode extends StmtNode {
    protected ExprNode cond;
    protected List<StmtNode> cases;

    public SwitchNode(Location location, ExprNode cond, List<StmtNode> cases) {
        super(location);
        this.cond = cond;
        this.cases = cases;
    }

    public List<StmtNode> getCases() {
        return cases;
    }

    public ExprNode getCond() {
        return cond;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printNodeList("cases", cases);
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
