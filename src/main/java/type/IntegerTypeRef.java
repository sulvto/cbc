package type;

import ast.Location;

/**
 * Created by sulvto on 16-11-15.
 */
public class IntegerTypeRef extends TypeRef {
    public static IntegerTypeRef intRef() {
        return new IntegerTypeRef("int");
    }

    public static IntegerTypeRef uintRef() {
        return new IntegerTypeRef("unsigned int");
    }

    public static IntegerTypeRef longRef() {
        return new IntegerTypeRef("long");
    }

    public static IntegerTypeRef ulongRef() {
        return new IntegerTypeRef("unsigned long");
    }

    private String name;

    public IntegerTypeRef(String name) {
        this(name, null);
    }

    public IntegerTypeRef(String name, Location location) {
        super(location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerTypeRef) {
            return name.equals(((IntegerTypeRef) obj).getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }


}
