package type;

import ast.Location;

/**
 * Created by sulvto on 16-11-15.
 */
public abstract class TypeRef {
    private Location location;

    public TypeRef(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
