package type;

import ast.Location;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class StructTypeRef extends TypeRef {
    private String name;

    public StructTypeRef(Location location, String name) {
        super(location);
        this.name = name;
    }

    public StructTypeRef(String name) {
        this(null, name);
    }

    public boolean isStruct() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StructTypeRef) {
            return name.equals(((StructTypeRef) obj).name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "struct " + name;
    }
}
