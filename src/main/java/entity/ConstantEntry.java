package entity;

import asm.ImmediateValue;
import asm.MemoryReference;
import asm.Symbol;

/**
 * Created by sulvto on 16-11-26.
 */
public class ConstantEntry {
    private String value;
    private Symbol symbol;
    private MemoryReference memref;
    private ImmediateValue address;

    public ConstantEntry(String value) {
        this.value = value;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        if (this.symbol == null) {
            throw new Error("must not happen: symbol == null");
        }
        return symbol;
    }

    public void setMemref(MemoryReference memref) {
        this.memref = memref;
    }

    public MemoryReference getMemref() {
        if (memref == null) {
            throw new Error("must not happen: memref == null");
        }
        return memref;
    }

    public void setAddress(ImmediateValue address) {
        this.address = address;
    }

    public ImmediateValue getAddress() {
        return address;
    }

    public String getValue() {
        return value;
    }
}
