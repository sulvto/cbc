package ir;

import ast.Dumper;
import ast.Location;

import java.awt.*;

/**
 * Created by sulvto on 16-11-20.
 */
public class LabelStmt extends Stmt {
    private Label label;

    public LabelStmt(Location location, Label label) {
        super(location);
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("label", label);
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
