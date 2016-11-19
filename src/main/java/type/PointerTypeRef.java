package type;

/**
 * Created by sulvto on 16-11-15.
 */
public class PointerTypeRef extends TypeRef {
    private TypeRef baseType;

    public PointerTypeRef(TypeRef baseType) {
        super(baseType.getLocation());
        this.baseType = baseType;
    }

    public TypeRef getBaseType() {
        return baseType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointerTypeRef) {
            return baseType.equals(((PointerTypeRef) obj).baseType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return baseType.toString() + "*";
    }
}
