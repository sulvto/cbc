package asm;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public abstract class Operand implements OperandPattern {
    public abstract String toSource(SymbolTable table);

    public abstract String dump();

    public boolean isRegister() {
        return false;
    }

    public boolean isMemoryReference() {
        return false;
    }

    public IntegerLiteral getIntegerLiteral() {
        return null;
    }

    public abstract void collectStatistics(Statistics stats);

    @Override
    public boolean match(Operand operand) {
        return equals(operand);
    }

}
