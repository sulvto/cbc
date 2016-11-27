package asm;

import utils.TextUtils;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class SuffixedSymbol implements Symbol {
    private Symbol base;
    private String suffix;

    public SuffixedSymbol(Symbol base, String suffix) {
        this.base = base;
        this.suffix = suffix;
    }

    @Override
    public String getName() {
        return base.getName();
    }

    @Override
    public String toSource() {
        return base.toSource() + suffix;
    }

    @Override
    public String toSource(SymbolTable table) {
        return base.toSource(table) + suffix;
    }

    @Override
    public String dump() {
        return "(SuffixedSymbol " + base.dump() + " " + TextUtils.dumpString(suffix) + ")";
    }

    @Override
    public void collectStatistics(Statistics stats) {
        base.collectStatistics(stats);
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public Literal plus(long diff) {
        throw new Error("must not happen: SuffixedSymbol.plus called");
    }

    @Override
    public int cmp(IntegerLiteral integerLiteral) {
        return 1;
    }

    @Override
    public int cmp(NamedSymbol namedSymbol) {
        return toString().compareTo(suffix.toString());
    }

    @Override
    public int cmp(UnnamedSymbol unnamedSymbol) {
        return -1;
    }

    @Override
    public int cmp(SuffixedSymbol suffixedSymbol) {
        return toString().compareTo(toString().toString());
    }

    @Override
    public int compareTo(Literal o) {
        return -(o.compareTo(this));
    }

    @Override
    public String toString() {
        return base.toString() + suffix;
    }
}
