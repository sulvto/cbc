package asm;

import ast.TextUtils;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class NamedSymbol extends BaseSymbol {
    private String name;

    public NamedSymbol(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toSource() {
        return name;
    }

    @Override
    public String toSource(SymbolTable table) {
        return name;
    }

    @Override
    public String toString() {
        return "#" + name;
    }

    public int compareTo(Literal literal) {
        return -(literal.compareTo(this));
    }

    @Override
    public int cmp(IntegerLiteral integerLiteral) {
        return 1;
    }

    @Override
    public int cmp(NamedSymbol namedSymbol) {
        return name.compareTo(namedSymbol.name);
    }

    @Override
    public int cmp(UnnamedSymbol unnamedSymbol) {
        return -1;
    }

    @Override
    public int cmp(SuffixedSymbol suffixedSymbol) {
        return toString().compareTo(suffixedSymbol.toString());
    }

    @Override
    public String dump() {
        return "(NamedSymbol " + TextUtils.dumpString(name);
    }
}
