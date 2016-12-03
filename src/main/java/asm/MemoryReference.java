package asm;

/**
 * Created by sulvto on 16-11-26.
 */
public abstract class MemoryReference extends Operand implements Comparable<MemoryReference> {
    public boolean isMemoryReference() {
        return false;
    }
}
