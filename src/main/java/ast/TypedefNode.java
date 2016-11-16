package ast;

import compiler.DeclarationVisitor;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypedefNode extends TypeDefinition {
    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
