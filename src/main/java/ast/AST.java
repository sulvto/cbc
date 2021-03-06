package ast;

import entity.*;
import ir.IR;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-11-14.
 */
public class AST extends Node {

    Location source;
    Declarations declarations;
    ToplevelScope scope;
    ConstantTable constantTable;


    public AST(Location source, Declarations declarations) {
        super();
        this.source = source;
        this.declarations = declarations;
    }

    @Override
    public Location location() {
        return source;
    }

    public List<TypeDefinition> types(){
        List<TypeDefinition> result = new ArrayList<>();
        result.addAll(declarations.defstructs());
        result.addAll(declarations.defunions());
        result.addAll(declarations.typedefs());
        return result;
    }

    public List<Entity> entities(){
        List<Entity> result = new ArrayList<>();
        result.addAll(declarations.funcdecls);
        result.addAll(declarations.vardecls);
        result.addAll(declarations.defvars);
        result.addAll(declarations.defuns);
        result.addAll(declarations.constants);
        return result;
    }

    public List<Entity> declarations(){
        List<Entity> result = new ArrayList<>();
        result.addAll(declarations.funcdecls);
        result.addAll(declarations.vardecls);
        return result;
    }

    public List<Entity> definitions() {
        List<Entity> result = new ArrayList<>();
        result.addAll(declarations.defvars);
        result.addAll(declarations.defuns);
        result.addAll(declarations.constants);
        return result;
    }

    public List<Constant> getConstants() {
        return declarations.constants();
    }

    public List<DefinedVariable> definedVariables(){
        return declarations.defvars();
    }

    public List<DefinedFunction> definedFunctions() {
        return declarations.defuns();
    }

    public void setScope(ToplevelScope scope) {
        if (this.scope != null) {
            throw new Error("must not happen: ToplevelScope set twice");
        }
        this.scope = scope;
    }

    public ToplevelScope getScope() {
        if (this.scope == null) {
            throw new Error("must not happen: AST.scope is null");
        }
        return scope;
    }

    public void setConstantTable(ConstantTable table) {
        if (this.constantTable != null) {
            throw new Error("must not happen: ConstantTable set twice");
        }
        this.constantTable = table;
    }

    public ConstantTable getConstantTable() {
        if (this.constantTable == null) {
            throw new Error("must not happen: AST.constantTable is null");
        }
        return  constantTable;
    }

    public IR ir(){
        return new IR(source, declarations.defvars(), declarations.defuns(), declarations.funcdecls(), scope, constantTable);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printNodeList("variables", definedVariables());
        d.printNodeList("functions", definedFunctions());
    }

    public void dumpTokens(PrintStream printStream) {
        for (CflatToken t : source.token()) {
            printPair(t.kindName(), t.dumpedImage(), printStream);
        }
    }

    private static final int NUM_LEFT_COLUMNS = 24;

    private void printPair(String key, String value, PrintStream printStream) {
        printStream.print(key);
        for(int n = NUM_LEFT_COLUMNS-key.length();n>0;n--) {
            printStream.print(" ");
        }
        printStream.print(value);
    }

    public StmtNode getSingleMainStmt() {
        for (DefinedFunction f : definedFunctions()) {
            if ("main".equals(f.getName())) {
                if (f.getBody().getStmts().isEmpty()) {
                    return null;
                }
                return f.getBody().getStmts().get(0);
            }
        }
        return null;
    }

    public ExprNode getSingleMainExpr() {
        StmtNode stmt = getSingleMainStmt();
        if (stmt == null) {
            return null;
        } else if (stmt instanceof ExprStmtNode) {
            return ((ExprStmtNode) stmt).getExpr();
        } else if (stmt instanceof ReturnNode) {
            return ((ReturnNode) stmt).getExpr();
        }else {
            return null;
        }
    }
}
