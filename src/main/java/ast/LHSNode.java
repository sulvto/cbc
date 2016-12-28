package ast;

import type.Type;

/**
 * 能够成为赋值的左值的节点
 * Created by sulvto on 16-11-15.
 */
public abstract class LHSNode extends ExprNode {
    protected Type type;

    @Override
    public Type getType() {
        return type != null ? type : origType();
    }

    public void setType(Type type) {
        this.type = type;
    }

    protected abstract Type origType();

    @Override
    public long allocSize() {
        return origType().allocSize();
    }

    @Override
    public boolean isLvalue() {
        return true;
    }

    @Override
    public boolean isAssignable() {
        return isLoadable();
    }

    @Override
    public boolean isLoadable() {
        Type type = origType();
        return !type.isArray() && !type.isFunction();
    }
}
