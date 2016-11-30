package ast;

import entity.DefinedFunction;
import entity.DefinedVariable;
import entity.UndefinedFunction;
import entity.UndefinedVariable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DONE
 * Created by sulvto on 16-11-14.
 */
public class Declarations {
    Set<DefinedVariable> defvars = new LinkedHashSet<>();
    Set<UndefinedVariable> vardecls = new LinkedHashSet<>();
    Set<DefinedFunction> defuns = new LinkedHashSet<>();
    Set<UndefinedFunction> funcdecls = new LinkedHashSet<>();
    Set<Constant> constants = new LinkedHashSet<>();
    Set<StructNode> defstructs = new LinkedHashSet<>();
    Set<UnionNode> defunions = new LinkedHashSet<>();
    Set<TypedefNode> typedefs = new LinkedHashSet<>();

    public void add(Declarations decls) {
        defvars.addAll(decls.defvars);
        vardecls.addAll(decls.vardecls);
        funcdecls.addAll(decls.funcdecls);
        constants.addAll(decls.constants);
        defstructs.addAll(decls.defstructs);
        defunions.addAll(decls.defunions);
        typedefs.addAll(decls.typedefs);
    }

    public void addDefvar(DefinedVariable variable) {
        defvars.add(variable);
    }

    public void addDefvars(List<DefinedVariable> variables) {
        defvars.addAll(variables);
    }

    public List<DefinedVariable> defvars() {
        return new ArrayList<>(defvars);
    }

    public void addVardecl(UndefinedVariable variable) {
        vardecls.add(variable);
    }

    public List<UndefinedVariable> vardecls() {
        return new ArrayList<>(vardecls);
    }

    public void addConstant(Constant constant) {
        constants.add(constant);
    }

    public List<Constant> constants() {
        return new ArrayList<>(constants);
    }

    public void addDefun(DefinedFunction function) {
        defuns.add(function);
    }

    public List<DefinedFunction> defuns() {
        return new ArrayList<>(defuns);
    }

    public void addFuncdecl(UndefinedFunction function) {
        funcdecls.add(function);
    }

    public List<UndefinedFunction> funcdecls() {
        return new ArrayList<>(funcdecls);
    }


    public void addDefstruct(StructNode structNode) {
        defstructs.add(structNode);
    }

    public List<StructNode> defstructs() {
        return new ArrayList<>(defstructs);
    }

    public void addDefunion(UnionNode unionNode) {
        defunions.add(unionNode);
    }

    public List<UnionNode> defunions() {
        return new ArrayList<>(defunions);
    }

    public void addTypedef(TypedefNode typedefNode) {
        typedefs.add(typedefNode);
    }

    public List<TypedefNode> typedefs() {
        return new ArrayList<>(typedefs);
    }
}
