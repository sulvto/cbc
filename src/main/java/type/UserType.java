package type;

import ast.Location;
import ast.TypeNode;

/**
 * Created by sulvto on 16-11-15.
 */
public class UserType extends NamedType {
    private TypeNode real;

    public UserType(String name, TypeNode real, Location location) {
        super(name, location);
        this.real = real;
    }

    public Type realType() {
        return real.getType();
    }

    @Override
    public long allocSize() {
        return realType().allocSize();
    }

    @Override
    public long alignment() {
        return realType().alignment();
    }

    @Override
    public boolean isVoid() {
        return realType().isVoid();
    }

    @Override
    public boolean isArray() {
        return realType().isArray();
    }

    @Override
    public boolean isCallable() {
        return realType().isCallable();
    }

    @Override
    public boolean isCompositeType() {
        return realType().isCompositeType();
    }

    @Override
    public boolean isFunction() {
        return realType().isFunction();
    }

    @Override
    public boolean isIncompleteArray() {
        return realType().isIncompleteArray();
    }

    @Override
    public boolean isInt() {
        return realType().isInt();
    }

    @Override
    public boolean isInteger() {
        return realType().isInteger();
    }

    @Override
    public boolean isPointer() {
        return realType().isPointer();
    }

    @Override
    public boolean isScalar() {
        return realType().isScalar();
    }

    @Override
    public boolean isSigned() {
        return realType().isSigned();
    }

    @Override
    public boolean isStruct() {
        return realType().isStruct();
    }

    @Override
    public boolean isUnion() {
        return realType().isUnion();
    }

    @Override
    public boolean isUserType() {
        return true;
    }

    @Override
    public boolean isAllocatedArray() {
        return realType().isAllocatedArray();
    }

    @Override
    public Type getBaseType() {
        return realType().getBaseType();
    }

    @Override
    public long size() {
        return realType().size();
    }

    @Override
    public boolean isSameType(Type other) {
        return realType().isSameType(other);
    }

    @Override
    public boolean isCompatible(Type other) {
        return realType().isCompatible(other);
    }

    @Override
    public boolean isCastableTo(Type target) {
        return realType().isCastableTo(target);
    }

    @Override
    public UnionType getUnionType() {
        return realType().getUnionType();
    }

    @Override
    public StructType getStructType() {
        return realType().getStructType();
    }

    @Override
    public PointerType getPointerType() {
        return realType().getPointerType();
    }

    @Override
    public IntegerType getIntegerType() {
        return realType().getIntegerType();
    }

    @Override
    public FunctionType getFunctionType() {
        return realType().getFunctionType();
    }

    @Override
    public ArrayType getArrayType() {
        return realType().getArrayType();
    }

    @Override
    public CompositeType getCompositeType() {
        return realType().getCompositeType();
    }
}
