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
        return value.toSource(table);
    }

    @Override
    public String toString() {
        return toSource(SymbolTable.getDummy());
    }

    @Override
    public String dump() {
        return "(DirectMemoryReference " + value.dump() + ")";
    }

    @Override
    public void collectStatistics(Statistics stats) {
        value.collectStatistics(stats);
    }

    @Override
    public int compareTo(MemoryReference mem) {
        return -mem.cmp(this);
    }

    @Override
    public void fixOffset(long diff) {
        throw new Error("DirectMemoryReference#fixOffset");
    }

    @Override
    protected int cmp(DirectMemoryReference mem) {
        return value.compareTo(mem.value);
    }

    @Override
    protected int cmp(IndirectMemoryReference mem) {
        return 1;
    }

}
