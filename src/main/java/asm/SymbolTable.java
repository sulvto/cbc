package asm;

import java.util.HashMap;
import java.util.Map;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class SymbolTable {
    protected String base;
    protected Map<UnnamedSymbol, String> map;
    protected long seq = 0;

    private static final String DUMMY_SYMBOL_BASE = "L";
    private static final SymbolTable dummy = new SymbolTable(DUMMY_SYMBOL_BASE);

    public static SymbolTable getDummy() {
        return dummy;
    }

    public SymbolTable(String base) {
        this.base = base;
        this.map = new HashMap<>();
    }

    public Symbol newSymbol() {
        return new NamedSymbol(newString());
    }

    public String symbolString(UnnamedSymbol symbol) {
        String str = map.get(symbol);
        if (str == null) {
            String newStr = newString();
            map.put(symbol, newStr);
            return newStr;
        } else {
            return str;
        }
    }

    private String newString() {
        return base + seq++;
    }

}
