package ir;

import ast.ConstanTable;
import ast.Location;
import ast.ToplevelScope;
import entity.DefinedFunction;
import entity.DefinedVariable;
import entity.UndefinedFunction;

import java.util.List;

/**
 * Created by sulvto on 16-11-18.
 */
public class IR {

    public IR(Location source, List<DefinedVariable> defvars, List<DefinedFunction> defuns, List<UndefinedFunction> funcdecls, ToplevelScope scope, ConstanTable table) {

    }

    public void dump() {
        // TODO
    }
}
