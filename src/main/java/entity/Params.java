package entity;

import ast.Dumpable;
import ast.Dumper;
import ast.Location;
import compiler.Parameter;
import type.ParamTypeRefs;
import type.TypeRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public class Params extends ParamSlots<Parameter> implements Dumpable {
    public Params(Location location, List<Parameter> paramDescs) {
        super(location, paramDescs, false);
    }

    public List<Parameter> parameters() {
        return paramDescriptors;
    }

    public ParamTypeRefs parametersTypeRef() {
        List<TypeRef> typeRefs = new ArrayList<>();
        for (Parameter parameter : paramDescriptors) {
            typeRefs.add(parameter.getTypeNode().getTypeRef());
        }
        return new ParamTypeRefs(location, typeRefs, vararg);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Params&&equals((Params)obj);
    }


    public boolean equals(Params other) {
        return other.vararg == vararg && other.paramDescriptors.equals(paramDescriptors);
    }

    @Override
    public void dump(Dumper d) {
        d.printNodeList("parameters", parameters());
    }
}
