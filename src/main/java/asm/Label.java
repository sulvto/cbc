package asm;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class Label extends Assembly {
    protected Symbol symbol;


    public Label() {
        this(new UnnamedSymbol());
    }

    public Label(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public boolean isLabel() {
        return true;
    }

    @Override
    public String toSource(SymbolTable table) {
        return symbol.toSource(table) + ":";
    }

    @Override
    public String dump() {
        return "(Label " + symbol.dump() + ")";
    }
}
