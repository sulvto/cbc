package type;

import ast.Location;

/**
 * Created by sulvto on 16-11-15.
 */
public class IntegerTypeRef extends TypeRef {
    public static IntegerTypeRef charRef(Location location) {
        return new IntegerTypeRef("char", location);
    }

    public static IntegerTypeRef charRef() {
        return new IntegerTypeRef("char");
    }

    public static IntegerTypeRef ucharRef(Location location) {
        return new IntegerTypeRef("unsigned char", location);
    }

    public static IntegerTypeRef ucharRef() {
        return new IntegerTypeRef("unsigned char");
    }

    public static IntegerTypeRef shortRef(Location location) {
        return new IntegerTypeRef("short", location);
    }

    public static IntegerTypeRef shortRef() {
        return new IntegerTypeRef("short");
    }

    public static IntegerTypeRef ushortRef(Location location) {
        return new IntegerTypeRef("unsigned short", location);
    }

    public static IntegerTypeRef ushortRef() {
        return new IntegerTypeRef("unsigned short");
    }

    public static IntegerTypeRef intRef(Location location) {
        return new IntegerTypeRef("int", location);
    }

    public static IntegerTypeRef intRef() {
        return new IntegerTypeRef("int");
    }

    public static IntegerTypeRef uintRef(Location location) {
        return new IntegerTypeRef("unsigned int", location);
    }

    public static IntegerTypeRef uintRef() {
        return new IntegerTypeRef("unsigned int");
    }

    public static IntegerTypeRef longRef(Location location) {
        return new IntegerTypeRef("long", location);
    }

    public static IntegerTypeRef longRef() {
        return new IntegerTypeRef("long");
    }

    public static IntegerTypeRef ulongRef(Location location) {
        return new IntegerTypeRef("unsigned long", location);
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
