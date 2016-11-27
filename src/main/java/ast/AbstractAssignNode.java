package ast;

import type.Type;

/**
 * DONE
 * 赋值
 * Created by sulvto on 16-11-15.
 */
public abstract class AbstractAssignNode extends ExprNode {
    ExprNode lhs,rhs;


    public AbstractAssignNode(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Type getType() {
        return lhs.getType();
    }

    public ExprNode getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }

    public void setRhs(ExprNode rhs) {
        this.rhs = rhs;
    }

    @Override
    public Location location() {
        return lhs.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("lhs", lhs);
        d.printMember("rhs", rhs);
    }

}
