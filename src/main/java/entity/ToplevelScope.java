package entity;

import exception.SemanticException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sulvto on 16-11-18.
 */
public class ToplevelScope extends Scope {
    private Map<String, Entity> entityMap;
    private List<DefinedVariable> staticLocalVariables;

    public ToplevelScope() {
        super();
        entityMap = new LinkedHashMap<>();
        staticLocalVariables = null;
    }

    @Override
    public boolean isToplevel() {
        return true;
    }

    @Override
    public ToplevelScope toplevel() {
        return this;
    }

    @Override
    public Scope getParent() {
        return null;
    }

    @Override
    public Entity getEntity(String name) throws SemanticException {
        Entity entity = entityMap.get(name);
        if (entity == null) {
            throw new SemanticException("unresolved reference: " + name);
        }
        return entity;
    }

    public void declareEntity(Entity entity) {
        Entity e = entityMap.get(entity.getName());
        if (e != null) {
            throw new SemanticException("duplicated declaration: " + entity.getName() + ": " + e.location() + " and " + entity.location());
        }
        entityMap.put(entity.getName(), entity);
    }

    public void defineEntity(Entity entity) {
        Entity e = entityMap.get(entity.getName());
        if (e != null && e.isDefined()) {
            throw new SemanticException("duplicated definition: " + entity.getName() + ": " + e.location() + " and " + entity.location());
        }
        entityMap.put(entity.getName(), entity);
    }
}
