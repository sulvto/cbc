package type;

import ast.Location;
import ast.Slot;
import utils.AsmUtils;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class UnionType extends CompositeType {
    public UnionType(String name, List<Slot> members, Location location) {
        super(name, members, location);
    }

    @Override
    public boolean isUnion() {
        return true;
    }

    @Override
    public boolean isSameType(Type other) {
        if (other.isUnion()) {
            return equals(other.getUnionType());
        } else {
            return false;
        }
    }


    @Override
    protected void computeOffsets() {
        long maxSize = 0;
        long maxAlign = 1;
        for (Slot s : members) {
            s.setOffset(0);
            maxSize = Math.max(maxSize, s.allocSize());
            maxAlign = Math.max(maxAlign, s.alignment());
        }
        cacheSize = AsmUtils.align(maxSize, maxAlign);
        cacheAlign = maxAlign;
    }

    @Override
    public String toString() {
        return "union " + name;
    }
}
