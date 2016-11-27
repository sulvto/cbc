package ir;

import ast.Location;

/**
 * Created by sulvto on 16-11-20.
 */
public abstract class Stmt implements Dumpable {
    protected Location location;

    public Stmt(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void dump(Dumper d) {
        d.printClass(this, location);
        doDump(d);
    }

    protected abstract void doDump(Dumper d);

    public abstract <S, E> S accept(IRVisitor<S, E> visitor);
}
