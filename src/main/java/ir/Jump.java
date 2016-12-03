package ir;

import asm.Label;
import ast.Location;

/**
 * Created by sulvto on 16-11-20.
 */
public class Jump extends Stmt {
    private Label label;

    public Jump(Location location, Label label) {
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
