package ast;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class LabelNode extends StmtNode {
    protected String name;
    protected StmtNode stmt;

    public LabelNode(Location location,String name,StmtNode stmt) {
        super(location);
        this.name = name;
        this.stmt = stmt;
    }

    public String getName() {
        return name;
    }

    public StmtNode getStmt() {
        return stmt;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("name", name);
        d.printMember("stmt", stmt);
    }
}
