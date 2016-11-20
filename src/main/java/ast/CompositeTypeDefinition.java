package ast;

import type.TypeRef;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public abstract class CompositeTypeDefinition extends TypeDefinition {
    private List<Slot> members;

    public CompositeTypeDefinition(Location location, TypeRef typeRef, String name,List<Slot> members) {
        super(location, typeRef, name);
        this.members = members;
    }

    public boolean isCompositeType() {
        return true;
    }

    public abstract String kind();

    public List<Slot> getMembers() {
        return members;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("name",name);
        d.printNodeList("members",members);
    }
}
