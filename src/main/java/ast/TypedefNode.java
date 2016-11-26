package ast;

import type.Type;
import type.TypeRef;
import type.UserType;
import type.UserTypeRef;

/**
 * Created by sulvto on 16-11-15.
 */
public class TypedefNode extends TypeDefinition {

    private TypeNode real;

    public TypedefNode(Location location, TypeRef real, String name) {
        super(location, new UserTypeRef(name), name);
        this.real = new TypeNode(real);
    }

    public boolean isUserType() {
        return true;
    }

    public TypeNode realTypeNode() {
        return real;
    }

    public Type realType() {
        return real.getType();
    }

    public TypeRef realTypeRef() {
        return real.getTypeRef();
    }


    @Override
    public Type definingType() {
        return new UserType(name, realTypeNode(), location());
    }

    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("name", name);
        d.printMember("typeNode", real);
    }
}
