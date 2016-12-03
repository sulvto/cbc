package asm;

/**
 * 间接内存引用
 * Created by sulvto on 16-11-26.
 */
public class IndirectMemoryReference extends MemoryReference {
    @Override
    public String toSource(SymbolTable table) {
        return null;
    }

    @Override
    public String dump() {
        return null;
    }

    @Override
    public void collectStatistics(Statistics stats) {

    }

    @Override
    public int compareTo(MemoryReference o) {
        return 0;
    }
}
