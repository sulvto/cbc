package entity;

import ast.Dumper;
import ast.TypeNode;
import compiler.Parameter;

import java.util.List;

/**
 * Created by sulvto on 16-11-26.
 */
public class UndefinedFunction extends Function {
    private Params params;

    public UndefinedFunction(TypeNode typeNode, String name, Params params) {
        super(false, typeNode, name);
        this.params = params;
    }


    public List<Parameter> parameters() {
        return params.parameters();
    }

    @Override
    public boolean isDefined() {
        return false;
    }

    @Override
    protected void doDump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("isPrivate", isPrivate);
        dumper.printMember("typeNode", typeNode);
        dumper.printMember("params", params);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
