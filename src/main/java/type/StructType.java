package type;

import ast.Location;
import ast.Slot;

import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public class StructType extends CompositeType {
    public StructType(String name, List<Slot> members, Location location) {
        super(name, members, location);
    }
}
