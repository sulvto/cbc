package type;

import ast.Location;
import ast.Slot;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class CompositeType extends NamedType {
    private List<Slot> members;
    private long cacheSize;
    private long cacheAlign;
    private boolean isRecursiveCheck;

    public CompositeType(String name, List<Slot> members, Location location) {
        super(name, location);
        this.members = members;
        this.cacheSize = Type.sizeUnknown;
        this.cacheAlign = Type.sizeUnknown;
        this.isRecursiveCheck = false;
    }

    public List<Slot> getMembers() {
        return members;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean isSameType(Type other) {
        return false;
    }

    @Override
    public boolean isCompatible(Type other) {
        return true;
    }

    @Override
    public boolean isCastableTo(Type target) {
        return false;
    }

    public Slot getMember(String name) {
        for (Slot s : members) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }
}
