package type;

import ast.Location;

/**
 * Created by sulvto on 16-11-15.
 */
public class UserTypeRef extends TypeRef {
    private String name;

    public UserTypeRef(String name) {
        super(null);
        this.name = name;
    }

    public UserTypeRef(Location location, String name) {
        super(location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserTypeRef) {
            return name.equals(((UserTypeRef) obj).getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
