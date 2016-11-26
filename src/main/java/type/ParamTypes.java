package type;

import ast.Location;
import entity.ParamSlots;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public class ParamTypes extends ParamSlots<Type> {

    protected ParamTypes(Location location, List<Type> paramDescs, boolean vararg) {
        super(location, paramDescs, vararg);
    }

    public List<Type> getTypes() {
        return paramDescriptors;
    }

    public boolean isSameType(ParamTypes other) {
        if(vararg!=other.vararg) return false;
        if(minArgc()!=other.minArgc()) return false;

        Iterator<Type> typeIterator = other.getTypes().iterator();
        for (Type t : paramDescriptors) {
            if (!t.isSameType(typeIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ParamTypes && equals((ParamTypes) obj);
    }

    public boolean equals(ParamTypes other) {
        return vararg = other.vararg && paramDescriptors.equals(other.paramDescriptors);
    }

}
