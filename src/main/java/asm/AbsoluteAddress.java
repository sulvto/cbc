package asm;

/**
 * Created by sulvto on 16-11-26.
 */
public class AbsoluteAddress extends Operand {
    protected Register register;

    public AbsoluteAddress(Register reg) {
        this.register = reg;
    }

    @Override
    public String toSource(SymbolTable table) {
        return "*"+register.toSource(table);
    }

    @Override
    public String dump() {
        return "(AbsoluteAddress " + register.dump() + ")";
    }

    @Override
    public void collectStatistics(Statistics stats) {
        register.collectStatistics(stats);
    }
}
