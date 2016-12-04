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
}
