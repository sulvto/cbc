package asm;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class UnnamedSymbol extends BaseSymbol {
    @Override
    public String getName() {
        throw new Error("unnamed symbol");
    }

    @Override
    public String toSource() {
        throw new Error("UnnamedSymbol#toSource() called");
    }

    @Override
    public String toSource(SymbolTable table) {
        return table.symbolString(this);
    }

    @Override
    public String dump() {
        return "(UnnamedSymbol @" + Integer.toHexString(hashCode()) + ")";
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
