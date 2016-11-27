package type;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class ArrayType extends Type {
    private Type baseType;
    private long length;
    private long pointerSize;
    static final long undefined = -1;

    public ArrayType(Type baseType, long pointerSize) {
        this(baseType, undefined, pointerSize);
    }

    public ArrayType(Type baseType, long length, long pointerSize) {
        this.baseType = baseType;
        this.length = length;
        this.pointerSize = pointerSize;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isAllocatedArray() {
        return length != undefined && (!baseType.isArray() || baseType.isAllocatedArray());
    }

    @Override
    public boolean isIncompleteArray() {
        if (baseType.isArray()) {
            return !baseType.isAllocatedArray();
        } else {
            return false;
        }
    }

    public Type getBaseType() {
        return baseType;
    }

    public long length() {
        return length;
    }


    public long size() {
        return pointerSize;
    }

    @Override
    public long allocSize() {
        if (length == undefined) {
            return size();
        } else {
            return baseType.allocSize() * length;
        }
    }

    @Override
    public long alignment() {
        return baseType.alignment();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) obj;
            return baseType.equals(arrayType.baseType) && length == arrayType.length;
        } else {
            return false;
        }
    }

    @Override
    public boolean isCompatible(Type other) {
        if(!other.isPointer()&&!other.isArray()) return false;
        if (other.getBaseType().isVoid()) {
            return true;
        } else {
            return baseType.isCompatible(other.getBaseType()) && baseType.size() == other.getBaseType().size();
        }
    }

    @Override
    public boolean isCastableTo(Type target) {
        return target.isPointer() || target.isArray();
    }

    @Override
    public boolean isSameType(Type other) {
        if(!other.isPointer()&&!other.isArray()) return false;
        return baseType.isSameType(other.getBaseType());
    }

    @Override
    public String toString() {
        if (length < 0) {
            return baseType.toString() + "[]";
        } else {
            return baseType.toString() + "[" + length + "]";
        }
    }
}
