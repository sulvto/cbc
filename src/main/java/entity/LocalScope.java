package entity;

import exception.SemanticException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sulvto on 16-11-18.
 */
public class LocalScope extends Scope {
    Scope parent;
    Map<String , DefinedVariable> variables;

    public LocalScope(Scope parent) {
        super();
        this.parent = parent;
        parent.addChildren(this);
        variables = new LinkedHashMap<>();
    }

    @Override
    public boolean isToplevel() {
        return false;
    }

    @Override
    public ToplevelScope toplevel() {
        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    @Override
    public Entity getEntity(String name) throws SemanticException {
        DefinedVariable definedVariable = variables.get(name);
        if (definedVariable != null) {
            return definedVariable;
        }
        return parent.getEntity(name);
    }
}
