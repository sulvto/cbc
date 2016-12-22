package asm;

/**
 * 寄存器
 * Created by sulvto on 16-11-26.
 */
public abstract class Register extends Operand {
    @Override
    public boolean isRegister() {
        return true;
    }

    @Override
    public void collectStatistics(Statistics stats) {
        stats.registerUsed(this);
    }
}
