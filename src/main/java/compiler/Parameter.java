package compiler;

import ast.Dumper;
import ast.TypeNode;
import entity.DefinedVariable;

/**
 * Created by sulvto on 16-11-19.
 */
public class Parameter extends DefinedVariable {
    public Parameter(TypeNode type, String name) {
        super(false, type, name, null);
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    protected void _dump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("typeNode", typeNode);
    }
}
