package type;

/**
 * Created by sulvto on 16-11-15.
 */
public class ArrayTypeRef extends TypeRef {
    private TypeRef baseType;
    private long length;
    static final long undefined = -1;

    public ArrayTypeRef(TypeRef baseType) {
        super(baseType.getLocation());
        this.baseType = baseType;
        this.length = undefined;
    }

    public ArrayTypeRef(TypeRef baseType, long length) {
        super(baseType.getLocation());
        if (length < 0) {
            throw new Error("negative array length");
        }
        this.baseType = baseType;
        this.length = length;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ArrayTypeRef) && (this.length == ((ArrayTypeRef) obj).getLength());
    }

    public TypeRef getBaseType() {
        return baseType;
    }

    public long getLength() {
        return length;
    }

    public boolean isLengthUndefined() {
        return this.length == undefined;
    }

    @Override
    public String toString() {
        return baseType.toString() + "[" + (length == undefined ? "" : length) + "]";
    }
}
