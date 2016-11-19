package type;

import ast.Location;
import entity.ParamSlots;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public class ParamTypeRefs extends ParamSlots<TypeRef> {

    public ParamTypeRefs(List<TypeRef> paramDescs) {
        super(paramDescs);
    }

    public ParamTypeRefs(Location location, List<TypeRef> paramDescs, boolean vararg) {
        super(location,paramDescs,vararg);
    }

    public List<TypeRef> typerefs() {
        return paramDescriptors;
    }

    public ParamTypes internTypes(TypeTable table) {
        List<Type> types = new ArrayList<>();
        for (TypeRef ref : paramDescriptors) {
            types.add(table.getParamType(ref));
        }
        return new ParamTypes(location, types, vararg);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ParamTypeRefs&&equals((ParamTypeRefs)obj);
    }

    public boolean equals(ParamTypeRefs other) {
        return vararg == other.vararg && paramDescriptors.equals(other.paramDescriptors);
    }
}
