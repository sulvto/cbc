package asm;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public interface Literal extends Comparable<Literal> {
    public String toSource();

    public String toSource(SymbolTable table);

    public String dump();

    public void collectStatistics(Statistics stats);

    public boolean isZero();

    public Literal plus(long diff);

    public int cmp(IntegerLiteral integerLiteral);

    public int cmp(NamedSymbol namedSymbol);

    public int cmp(UnnamedSymbol unnamedSymbol);

    public int cmp(SuffixedSymbol suffixedSymbol);

}
