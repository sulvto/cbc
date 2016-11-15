package ast;

import java.util.Collection;
import java.util.List;

/**
 * Created by sulvto on 16-11-14.
 */
public class Declarations {
    public int funcdecls;
    public int vardecls;
    public int defvars;
    public int defuns;
    public int constants;

    public Collection<? extends TypeDefinition> defstructs() {
        return null;
    }

    public Collection<? extends TypeDefinition> defunions() {
        return null;
    }

    public Collection<? extends TypeDefinition> typedefs() {
        return null;
    }

    public List<Constant> constants() {
        return null;
    }

    public List<DefinedVariable> defvars() {
        return null;
    }

    public List<DefinedFunction> defuns() {
        return null;
    }

    public int getDefvars() {
        return defvars;
    }

    public int getDefuns() {
        return defuns;
    }

    public int getFuncdecls() {
        return funcdecls;
    }
}
