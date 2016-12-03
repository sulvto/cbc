package asm;

/**
 * Created by sulvto on 16-11-26.
 */
public class IntegerLiteral implements Literal {
    private long value;

    public IntegerLiteral(long n) {
        this.value = n;
    }
    @Override
    public String toSource() {
        return null;
    }

    @Override
    public String toSource(SymbolTable table) {
        return null;
    }

    @Override
    public String dump() {
        return null;
    }

    @Override
    public void collectStatistics(Statistics stats) {

    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public Literal plus(long diff) {
        return null;
    }

    @Override
    public int cmp(IntegerLiteral integerLiteral) {
        return 0;
    }

    @Override
    public int cmp(NamedSymbol namedSymbol) {
        return 0;
    }

    @Override
    public int cmp(UnnamedSymbol unnamedSymbol) {
        return 0;
    }

    @Override
    public int cmp(SuffixedSymbol suffixedSymbol) {
        return 0;
    }

    @Override
    public int compareTo(Literal o) {
        return 0;
    }
}
