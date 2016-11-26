package entity;

import ast.Location;

import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public abstract class ParamSlots<T> {
    protected Location location;
    protected List<T> paramDescriptors;
    protected boolean vararg;

    public ParamSlots(List<T> paramDescs) {
        this(null, paramDescs);
    }

    public ParamSlots(Location location, List<T> paramDescs) {
        this(location,paramDescs,false);
    }

    public ParamSlots(Location location, List<T> paramDescs, boolean vararg) {
        this.location = location;
        this.paramDescriptors = paramDescs;
        this.vararg = vararg;
    }

    public int argc() {
        if (vararg) {
            throw new Error("must not happen: Param#argc for vararg");
        }
        return paramDescriptors.size();
    }

    public int minArgc() {
        return paramDescriptors.size();
    }

    public void acceptVarargs() {
        this.vararg = true;
    }

    public Location location() {
        return location;
    }

    public boolean isVararg() {
        return vararg;
    }
}
