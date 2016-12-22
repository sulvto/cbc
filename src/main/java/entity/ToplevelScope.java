package entity;

import exception.SemanticException;
import utils.ErrorHandler;

import java.util.*;

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

    public void declareEntity(Entity entity) throws SemanticException {
        Entity e = entityMap.get(entity.getName());
        if (e != null) {
            throw new SemanticException("duplicated declaration: " + entity.getName() + ": " + e.location() + " and " + entity.location());
        }
        entityMap.put(entity.getName(), entity);
    }

    public void defineEntity(Entity entity) throws SemanticException {
        Entity e = entityMap.get(entity.getName());
        if (e != null && e.isDefined()) {
            throw new SemanticException("duplicated definition: " + entity.getName() + ": " + e.location() + " and " + entity.location());
        }
        entityMap.put(entity.getName(), entity);
    }

    public void checkReferences(ErrorHandler errorHandler) {
        for (Entity ent : entityMap.values()) {
            if (ent.isDefined() && ent.isPrivate() && !ent.isConstant() && !ent.isRefered()) {
                errorHandler.warn(ent.location(), "unused variable: " + ent.getName());
            }
        }

        for (LocalScope funScope : children) {
            for (LocalScope s : funScope.children) {
                s.checkReferences(errorHandler);
            }
        }
    }

    public List<Variable> allGlobalVariables() {
        List<Variable> result = new ArrayList<>();
        for (Entity ent : entityMap.values()) {
            if (ent instanceof Variable) {
                result.add((Variable) ent);
            }
        }
        result.addAll(staticLocalVariables());
        return result;
    }

    public List<DefinedVariable> definedGlobalScopeVariables() {
        List<DefinedVariable> result = new ArrayList<>();

        for (Entity ent : entityMap.values()) {
            if (ent instanceof DefinedVariable) {
                result.add((DefinedVariable) ent);
            }
        }
        result.addAll(staticLocalVariables());
        return result;
    }

    private List<DefinedVariable> staticLocalVariables() {
        if (staticLocalVariables == null) {
            staticLocalVariables = new ArrayList<>();
            for (LocalScope s : children) {
                staticLocalVariables.addAll(s.staticLocalVariable());
            }
            Map<String, Integer> seqTable = new HashMap<>();
            for (DefinedVariable var : staticLocalVariables) {
                Integer seq = seqTable.get(var.getName());
                if (seq == null) {
                    var.setSequence(0);
                    seqTable.put(var.getName(), 1);
                } else {
                    var.setSequence(seq);
                    seqTable.put(var.getName(), seq + 1);
                }
            }
        }
        return staticLocalVariables;
    }
}
