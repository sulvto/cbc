package asm;

/**
 * 直接内存引用
 * Created by sulvto on 16-11-26.
 */
public class DirectMemoryReference extends MemoryReference {

    protected Literal value;

    public DirectMemoryReference(Literal value) {
        this.value = value;
    }

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

    @Override
    public void fixOffset(long diff) {

    }

    @Override
    protected int cmp(DirectMemoryReference mem) {
        return 0;
    }

    @Override
    protected int cmp(IndirectMemoryReference mem) {
        return 0;
    }
}
