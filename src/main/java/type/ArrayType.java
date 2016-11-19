package type;

/**
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

    public Type getBaseType() {
        return baseType;
    }
    public long size() {
        return pointerSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) obj;
            return baseType.equals(arrayType.baseType) && length == arrayType.length;
        }else{
            return false;
        }
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
