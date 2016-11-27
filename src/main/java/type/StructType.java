package type;

import ast.Location;
import ast.Slot;
import utils.AsmUtils;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class StructType extends CompositeType {
    public StructType(String name, List<Slot> members, Location location) {
        super(name, members, location);
    }

    @Override
    public boolean isStruct() {
        return true;
    }

    @Override
    public boolean isSameType(Type other) {
        if (other.isStruct()) {
            return equals(other.getStructType());
        } else {
            return false;
        }
    }

    @Override
    protected void computeOffsets() {
        long offset = 0;
        long maxAlign = 1;
        for (Slot s : getMembers()) {
            offset = AsmUtils.align(offset, s.allocSize());
            s.setOffset(offset);
            offset += s.allocSize();
            maxAlign = Math.max(offset, maxAlign);

        }
        cacheSize = AsmUtils.align(offset, maxAlign);
        cacheAlign = maxAlign;
    }

    @Override
    public String toString() {
        return "struct " + name;
    }
}
