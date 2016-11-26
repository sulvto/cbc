package asm;

/**
 * Created by sulvto on 16-11-18.
 */
public abstract class BaseSymbol implements Symbol {
    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public void collectStatistics(Statistics stats) {
        stats.symbolUsed(this);
    }

    @Override
    public Literal plus(long diff) {
        throw new Error("must not happen: BaseSymbol.plus called");
    }
}
