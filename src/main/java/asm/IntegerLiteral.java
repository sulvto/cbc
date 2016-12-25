package asm;

/**
 * Created by sulvto on 16-11-26.
 */
public class IntegerLiteral implements Literal {
    protected long value;

    public IntegerLiteral(long n) {
        this.value = n;
    }

    @Override
    public String toSource() {
        return Long.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IntegerLiteral) && this.value == ((IntegerLiteral) obj).value;
    }

    @Override
    public String toSource(SymbolTable table) {
        return toSource();
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public String dump() {
        return "(IntegerLiteral " + Long.toString(value) + ")";
    }

    @Override
    public void collectStatistics(Statistics stats) {

    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public Literal plus(long diff) {
        return new IntegerLiteral(value + diff);
    }

    @Override
    public int cmp(IntegerLiteral integerLiteral) {
        return new Long(value).compareTo(integerLiteral.value);
    }

    @Override
    public int cmp(NamedSymbol namedSymbol) {
        return -1;
    }

    @Override
    public int cmp(UnnamedSymbol unnamedSymbol) {
        return -1;
    }

    @Override
    public int cmp(SuffixedSymbol suffixedSymbol) {
        return -1;
    }

    @Override
    public int compareTo(Literal o) {
        return -(o.cmp(this));
    }
}
