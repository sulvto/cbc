package type;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class FunctionType extends Type {
    private Type returnType;
    private ParamTypes paramTypes;

    public FunctionType(Type returnType, ParamTypes paramTypes) {
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean isVararg() {
        return paramTypes.isVararg();
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public boolean isSameType(Type other) {
        if (other.isFunction()) {
            FunctionType functionType = other.getFunctionType();
            return functionType.returnType.isSameType(returnType) && functionType.paramTypes.isSameType(paramTypes);
        } else {
            return false;
        }
    }

    @Override
    public boolean isCompatible(Type other) {

        if (other.isFunction()) {
            FunctionType functionType = other.getFunctionType();
            return functionType.returnType.isCompatible(returnType) && functionType.paramTypes.isSameType(paramTypes);
        } else {
            return false;
        }
    }

    @Override
    public boolean isCastableTo(Type target) {
        return target.isFunction();
    }

    public List<Type> getParamTypes() {
        return paramTypes.getTypes();
    }

    @Override
    public long alignment() {
        throw new Error("FunctionType#alignment called");
    }

    @Override
    public long size() {
        throw new Error("FunctionType#size called");
    }

    @Override
    public String toString() {
        String sep = "";

        StringBuffer buffer = new StringBuffer();
        buffer.append(returnType.toString());
        buffer.append("(");
        for (Type t : paramTypes.getTypes()) {
            buffer.append(sep);
            buffer.append(t.toString());
            sep = ", ";
        }
        buffer.append(")");
        return buffer.toString();
    }
}
