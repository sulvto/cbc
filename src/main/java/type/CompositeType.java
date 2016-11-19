package type;

import ast.Location;
import ast.Slot;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class CompositeType extends NamedType {
    List<Slot> members;

    public CompositeType(String name, List<Slot> members, Location location) {
        this.members = members;
    }

    public List<Slot> getMembers() {
        return members;
    }
}
