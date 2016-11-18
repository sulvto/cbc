package ast;

import type.Type;

/**
 * 能够成为赋值的左值的节点
 * Created by sulvto on 16-11-15.
 */
public abstract class LHSNode extends ExprNode {
    Type type;

    @Override
    public Type getType() {
        return type!=null?type:origType();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public abstract Type origType();

}
