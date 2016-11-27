package ast;

import entity.Entity;
import entity.EntityVisitor;

/**
 *
 * DONE
 * Created by sulvto on 16-11-14.
 */
public class Constant extends Entity {
    private TypeNode type;
    private String name;
    private ExprNode value;

    public Constant(TypeNode type, String name, ExprNode value) {
        super(true, type, name);
        this.value = value;
    }

    public boolean isAssignable() {
        return false;
    }

    @Override
    public boolean isDefined() {
        return true;
    }


    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public ExprNode getValue() {
        return value;
    }

    @Override
    protected void doDump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("typeNode", typeNode);
        dumper.printMember("value", value);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
