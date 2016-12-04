package ir;

import asm.Label;
import ast.Location;

import java.util.List;

/**
 * Created by sulvto on 16-12-3.
 */
public class Switch extends Stmt {
    private Expr cond;
    private List<Case> cases;
    private Label defaultLabel, endLabel;

    public Switch(Location location, Expr cond, List<Case> cases, Label defaultLabel, Label endLabel) {
        super(location);
        this.cond = cond;
        this.cases = cases;
        this.defaultLabel = defaultLabel;
        this.endLabel = endLabel;
    }

    public Expr getCond() {
        return cond;
    }

    public Label getDefaultLabel() {
        return defaultLabel;
    }

    public List<Case> getCases() {
        return cases;
    }

    public Label getEndLabel() {
        return endLabel;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printMembers("cases", cases);
        d.printMember("defaultLabel", defaultLabel);
        d.printMember("endLabel", endLabel);
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
