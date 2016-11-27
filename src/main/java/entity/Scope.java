package entity;

import exception.SemanticException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-11-18.
 */
public abstract class Scope {
    protected List<LocalScope> children;

    public Scope() {
        children = new ArrayList<>();
    }


    public void addChildren(LocalScope scope) {
        children.add(scope);
    }

    abstract public boolean isToplevel();

    abstract public ToplevelScope toplevel();

    abstract public Scope getParent();

    abstract public Entity getEntity(String name) throws SemanticException;
}
