package ast;

import type.Type;
import type.TypeRef;
import type.UnionType;

import java.util.List;

/**
 * 联合体的定义
 * Created by sulvto on 16-11-15.
 */
public class UnionNode extends CompositeTypeDefinition {
    public UnionNode(Location location, TypeRef typeRef, String name, List<Slot> members) {
        super(location, typeRef, name, members);
    }

    public  boolean isUnion() {
        return true;
    }

    @Override
    public Type definingType() {
        return new UnionType(getName(),getMembers(),location());
    }

    @Override
    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String kind() {
        return "union";
    }
}
