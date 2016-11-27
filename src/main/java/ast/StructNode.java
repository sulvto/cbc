package ast;

import type.StructType;
import type.Type;
import type.TypeRef;

import java.util.List;

/**
 * DONE
 * 结构体的定义
 * Created by sulvto on 16-11-15.
 */
public class StructNode extends CompositeTypeDefinition {

    public StructNode(Location location, TypeRef typeRef, String name, List<Slot> members) {
        super(location, typeRef, name, members);
    }

    public boolean isStruct() {
        return true;
    }
    @Override
    public Type definingType() {
        return new StructType(name,getMembers(),location());
    }

    @Override
    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String kind() {
        return "struct";
    }
}
