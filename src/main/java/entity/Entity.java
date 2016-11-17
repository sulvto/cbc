package entity;

import ast.Dumpable;
import ast.Dumper;
import ast.Location;
import ast.TypeNode;

/**
 * Created by sulvto on 16-11-14.
 */
public abstract class Entity implements Dumpable {

    boolean isPrivate;
    TypeNode typeNode;
    String name;

    public Entity(boolean priv, TypeNode type, String name) {
        this.name = name;
    }

    public void dump(Dumper dumper) {
        dumper.printClass(this, location());
        _dump(dumper);
    }

    Location location(){
        return typeNode.location();
    };

    abstract protected void _dump(Dumper dumper);

    abstract public <T> T accept(EntityVisitor<T> visitor);

}
