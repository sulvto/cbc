package ir;

import asm.Label;
import ast.Location;

/**
 * Created by sulvto on 16-11-20.
 */
public class CJump extends Stmt {
    private Expr cond;
    private Label thenLabel;
    private Label elseLabel;

    public CJump(Location location,Expr cond,Label thenLabel,Label elseLabel) {
        super(location);
        this.cond = cond;
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
    }

    public Expr getCond() {
        return cond;
    }

    public Label getThenLabel() {
        return thenLabel;
    }

    public Label getElseLabel() {
        return elseLabel;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("cond", cond);
        d.printMember("thenLabel", thenLabel);
        d.printMember("elseLabel", elseLabel);
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
