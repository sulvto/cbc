package type;

import ast.Location;

/**
 * Created by sulvto on 16-11-15.
 */
public abstract class NamedType extends Type {
    protected String name;
    private Location location;

    public NamedType(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location location() {
        return this.location;
    }
}