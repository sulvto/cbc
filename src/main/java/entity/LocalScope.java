package entity;

import exception.SemanticException;
import type.Type;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sulvto on 16-11-18.
 */
public class LocalScope extends Scope {
    protected Scope parent;
    protected Map<String, DefinedVariable> variables;

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
        return parent.toplevel();
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public List<LocalScope> children() {
        return children;
    }

    public boolean isDefinedLocally(String name) {
        return variables.containsKey(name);
    }


    public void defineVariable(DefinedVariable variable) {
        String variableName = variable.getName();
        if (variables.containsKey(variableName)) {
            throw new Error("duplicated variable :" + variableName);
        } else {
            variables.put(variableName, variable);
        }
    }

    public DefinedVariable allocateTmp(Type type) {
        DefinedVariable vat = DefinedVariable.tem(type);
        defineVariable(vat);
        return vat;
    }

    @Override
    public Entity getEntity(String name) throws SemanticException {
        DefinedVariable definedVariable = variables.get(name);
        if (definedVariable != null) {
            return definedVariable;
        }
        return parent.getEntity(name);
    }

    /**
     * all local variable in this scope.
     *
     * @return
     */
    public List<DefinedVariable> allLocalVariables() {
        return allLocalScopes().stream().flatMap(localScope -> localScope.localVariable().stream()).collect(Collectors.toList());
    }

    public List<DefinedVariable> localVariable() {
        return variables.values()
                .stream()
                .filter(variable -> !variable.isPrivate())
                .collect(Collectors.toList());
    }

    public List<DefinedVariable> staticLocalVariable() {
        return allLocalScopes()
                .stream()
                .flatMap(localScope -> localScope.variables.values().stream())
                .filter(Entity::isPrivate)
                .collect(Collectors.toList());
    }

    private List<LocalScope> allLocalScopes() {
        List<LocalScope> result = new ArrayList<>();
        collectScope(result);
        return result;
    }

    private void collectScope(List<LocalScope> list) {
        list.add(this);
        for (LocalScope s : children) {
            s.collectScope(list);
        }
    }

    public void checkReferences(ErrorHandler errorHandler) {
        for (DefinedVariable var : variables.values()) {
            if (!var.isRefered()) {
                errorHandler.warn(var.location(), "unused variable: " + var.getName());
            }
        }
        for (LocalScope c : children) {
            c.checkReferences(errorHandler);
        }
    }
}
