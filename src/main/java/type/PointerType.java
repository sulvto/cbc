package type;

/**
 * Created by sulvto on 16-11-15.
 */
public class PointerType extends Type {
    private long size;
    private Type baseType;

    public PointerType(long size, Type baseType) {
        this.size = size;
        this.baseType = baseType;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public boolean isPointer() {
        return true;
    }

    @Override
    public boolean isScalar() {
        return true;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public boolean isCallable() {
        return baseType.isFunction();
    }

    @Override
    public Type getBaseType() {
        return baseType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointerType) {
            return baseType.equals(((Type) obj).getPointerType().baseType);
        } else {
            return false;
        }
    }

    @Override
    public boolean isSameType(Type other) {
        if (other.isPointer()) {
            return baseType.isSameType(other.getBaseType());
        } else {
            return false;
        }
    }

    @Override
    public boolean isCompatible(Type other) {
        if (!other.isPointer()) {
            return false;
        }
        if (baseType.isVoid()) {
            return true;
        }
        if (other.getBaseType().isVoid()) {
            return true;
        }
        return baseType.isCompatible(other.getBaseType());
    }

    @Override
    public boolean isCastableTo(Type other) {
        return other.isPointer() || other.isInteger();
    }

    @Override
    public String toString() {
        return baseType.toString() + "*";
    }
}
