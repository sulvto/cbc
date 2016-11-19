package type;

/**
 * Created by sulvto on 16-11-15.
 */
public class FunctionTypeRef extends TypeRef {
    private TypeRef returnType;
    private ParamTypeRefs params;

    public FunctionTypeRef(TypeRef returnType,ParamTypeRefs params) {
        super(returnType.getLocation());
        this.returnType = returnType;
        this.params = params;
    }

    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FunctionTypeRef && equals((FunctionTypeRef) obj);
    }

    public boolean equals(FunctionTypeRef other) {
        return returnType.equals(other.returnType) && params.equals(other.params);
    }

    public TypeRef getReturnType() {
        return returnType;
    }

    public ParamTypeRefs getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(returnType.toString());
        buf.append(" (");
        String sep = "";

        for (TypeRef ref : this.params.typerefs()) {
            buf.append(sep);
            buf.append(ref.toString());
            sep = ",";
        }
        buf.append(")");
        return buf.toString();
    }

}
