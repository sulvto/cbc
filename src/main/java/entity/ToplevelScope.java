package entity;

import exception.SemanticException;

import java.util.List;
import java.util.Map;

/**
 * Created by sulvto on 16-11-18.
 */
public class ToplevelScope extends Scope {
    Map<String, Entity> entity;
    List<DefinedVariable> staticLocalVariables;

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
        return null;
    }

    @Override
    public Entity getEntity(String name) throws SemanticException {
        return null;
    }
}
