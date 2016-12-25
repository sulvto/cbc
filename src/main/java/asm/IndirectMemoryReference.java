package asm;

/**
 * 间接内存引用
 * Created by sulvto on 16-11-26.
 */
public class IndirectMemoryReference extends MemoryReference {
    Literal offset;
    Register base;
    boolean fixed;

    public IndirectMemoryReference(long offset, Register base) {
        this(new IntegerLiteral(offset), base, true);
    }

    public IndirectMemoryReference(Symbol offset, Register base) {
        this(offset, base, true);
    }

    public IndirectMemoryReference(Literal offset, Register base, boolean fixed) {
        this.offset = offset;
        this.base = base;
        this.fixed = fixed;
    }

    public static IndirectMemoryReference relocatable(long offset, Register base) {
        return new IndirectMemoryReference(new IntegerLiteral(offset), base, false);
    }

    public void fixOffset(long diff) {
        if (fixed) {
            throw new Error("must not happen: fixed = true");
        }
        long curr = ((IntegerLiteral) offset).value;
        this.offset = new IntegerLiteral(curr + diff);
        this.fixed = true;
    }

    @Override
    protected int cmp(DirectMemoryReference mem) {
        return -1;
    }

    @Override
    protected int cmp(IndirectMemoryReference mem) {
        return -(mem.cmp(this));
    }

    @Override
    public String toSource(SymbolTable table) {

        if (!fixed) {
            throw new Error("must not happen: writing unfixed");
        }
        return (offset.isZero() ? "" : offset.toSource(table)) + "(" + base.toSource(table) + ")";

    }

    @Override
    public String dump() {
        return "(IndirectMemoryReference " + (fixed ? "" : "*") + offset.dump() + " " + base.dump() + ")";
    }

    @Override
    public void collectStatistics(Statistics stats) {
        base.collectStatistics(stats);
    }

    @Override
    public int compareTo(MemoryReference mem) {
        return -(mem.cmp(this));
    }
}
