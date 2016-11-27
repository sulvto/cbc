package type;

import ast.Location;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class VoidTypeRef extends TypeRef {

    public VoidTypeRef() {
        super(null);
    }

    public VoidTypeRef(Location location) {
        super(location);
    }

    public boolean isVoid() {
        return true;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VoidTypeRef);
    }

    @Override
    public String toString() {
        return "void";
    }
}
