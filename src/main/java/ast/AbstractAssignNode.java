package ast;

import type.Type;

/**
 * 赋值
 * Created by sulvto on 16-11-15.
 */
public abstract class AbstractAssignNode extends ExprNode {
    @Override
    public Type getType() {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {

    }

}
