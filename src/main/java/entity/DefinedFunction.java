package entity;

import ast.BlockNode;
import ast.Dumper;
import ast.TypeNode;
import compiler.Parameter;
import ir.Stmt;

import java.util.List;

/**
 * Created by sulvto on 16-11-14.
 */
public class DefinedFunction extends Function {
    private TypeNode typeNode;
    private boolean isPrivate;
    private Params params;
    private BlockNode body;
    private LocalScope scope;
    private List<Stmt> ir;

    public DefinedFunction(boolean priv, TypeNode typeNode,String name,Params params,BlockNode body){
        super(priv, typeNode, name);
        this.params = params;
        this.body = body;
    }

    public BlockNode getBody() {
        return body;
    }

    public List<Stmt> getIr() {
        return ir;
    }

    public void setIr(List<Stmt> ir) {
        this.ir = ir;
    }

    public LocalScope lvarScope() {
        return getBody().getScope();
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    public List<DefinedVariable> localVariables() {
        return scope.allLocalVariables();
    }

    @Override
    protected void doDump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("isPrivate", isPrivate);
        dumper.printMember("params", params);
        dumper.printMember("body", body);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<Parameter> parameters() {
        return params.parameters();
    }
}
