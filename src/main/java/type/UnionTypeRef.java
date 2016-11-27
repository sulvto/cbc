package type;

import ast.Location;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class UnionTypeRef extends TypeRef {
    private String name;

    public UnionTypeRef(String name) {
        this(null, name);
    }

    public UnionTypeRef(Location location, String name) {
        super(location);
        this.name = name;
    }

    public  boolean isUnion() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnionTypeRef) {
            return name.equals(((UnionTypeRef) obj).name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "union " + name;
    }
}
