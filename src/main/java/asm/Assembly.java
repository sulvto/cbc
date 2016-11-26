package asm;

/**
 * Created by sulvto on 16-11-26.
 */
public abstract class Assembly {
    public abstract String toSource(SymbolTable table);

    public abstract String dump();

    public boolean isInstruction() {
        return false;
    }

    public boolean isLabel() {
        return false;
    }

    public boolean isDirective() {
        return false;
    }

    public boolean isComment() {
        return false;
    }

    public void collectStatistics(Statistics stats) {
    }
}
