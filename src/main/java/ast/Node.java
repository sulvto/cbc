package ast;

import java.io.PrintStream;

/**
 * Created by sulvto on 16-11-14.
 */
 public abstract class Node implements Dumpable{

    abstract public Location location();

    public void dump() {
        dump(System.out);
    }

    public void dump(PrintStream printStream) {
        dump(new Dumper(printStream));
    }

    @Override
    public void dump(Dumper d) {
        d.printClass(this, location());
        _dump(d);
    }

    protected abstract void _dump(Dumper d);
}
