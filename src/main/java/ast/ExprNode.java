package ast;

import exception.SemanticError;

/**
 * Created by sulvto on 16-11-14.
 */
public abstract class ExprNode extends Node{

    @Override
    public Location location() {
        return null;
    }

    abstract public  Type getType();

    Type origType() {
        return getType();
    }

    public long allocSize(){
        return getType().allocSize();
    }

    public boolean isConstant() {
        return false;
    }

    public boolean isParameter() {
        return false;
    }

    public boolean isLvalue() {
        return false;
    }

    public boolean isAssignable() {
        return false;
    }

    public boolean isLoadable() {
        return false;
    }

    public boolean isCallable() {
        try {
            return getType().isCallable();
        } catch (SemanticError error) {
            return false;
        }
    }

    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);

}
