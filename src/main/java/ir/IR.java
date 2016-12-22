package ir;

import ast.Location;
import entity.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-11-18.
 */
public class IR {
    Location source;
    List<DefinedVariable> defvars;
    List<DefinedFunction> defuns;
    List<UndefinedFunction> funcdecls;
    ToplevelScope scope;
    ConstantTable constantTable;
    List<DefinedVariable> gvars;
    List<DefinedVariable> comms;

    public IR(Location source, List<DefinedVariable> defvars, List<DefinedFunction> defuns, List<UndefinedFunction> funcdecls, ToplevelScope scope, ConstantTable table) {
        this.source = source;
        this.defvars = defvars;
        this.defuns = defuns;
        this.funcdecls = funcdecls;
        this.scope = scope;
        this.constantTable = table;
    }

    public String fileName() {
        return source.getSourceName();
    }

    public Location location() {
        return source;
    }

    public boolean isFunctionDefined() {
        return !defuns.isEmpty();
    }

    public List<DefinedFunction> definedFunctions() {
        return defuns;
    }

    public List<Function> allFunctions() {
        List<Function> result = new ArrayList<>();
        result.addAll(defuns);
        result.addAll(funcdecls);
        return result;
    }

    public void dump() {
        dump(System.out);
    }

    public void dump(PrintStream s) {
        Dumper d = new Dumper(s);
        d.printClass(this, source);
        d.printVars("variables", defvars);
        d.printFuncs("functions", defuns);
    }

    public ConstantTable constanTable() {
        return constantTable;
    }

    public List<Variable> allGlobalVariables() {
        return scope.allGlobalVariables();
    }

    /**
     * global scope and is initialized
     * @return list
     */
    public List<DefinedVariable> definedGlobalVariables() {
        if (gvars == null) {
            initVariables();
        }
        return gvars;
    }

    private void initVariables() {
        gvars = new ArrayList<>();
        comms = new ArrayList<>();
        for (DefinedVariable var : scope.definedGlobalScopeVariables()) {
            (var.hasInitializer() ? gvars : comms).add(var);
        }
    }

    /**
     * global scope and is not initialized
     * @return
     */
    public List<DefinedVariable> definedCommonSymbols() {
        if (comms == null) {
            initVariables();
        }
        return comms;
    }

    public boolean isGlobalVariableDefined() {
        return !definedGlobalVariables().isEmpty();
    }


    public boolean isStringLiteralDefined() {
        return !constantTable.isEmpty();
    }

    public boolean isCommonSymbolDefined() {
        return !definedCommonSymbols().isEmpty();
    }
}
