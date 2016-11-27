package ast;

import asm.Label;

import java.util.List;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class CaseNode extends StmtNode {
    protected Label label;
    protected List<ExprNode> values;
    protected BlockNode body;

    public CaseNode(Location location, List<ExprNode> values, BlockNode blockNode) {
        super(location);
        this.values = values;
        this.body = blockNode;
        this.label = new Label();
    }

    public boolean isDefault() {
        return values.isEmpty();
    }

    public List<ExprNode> getValues() {
        return values;
    }

    public BlockNode getBody() {
        return body;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printNodeList("values", values);
        d.printMember("body", body);
    }
}
