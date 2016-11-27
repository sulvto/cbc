package type;

/**
 *
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class IntegerType extends Type {
    private long size;
    protected boolean isSigned;
    protected String name;

    public IntegerType(long size, boolean isSigned, String name) {
        this.size = size;
        this.isSigned = isSigned;
        this.name = name;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isSigned() {
        return isSigned;
    }

    @Override
    public boolean isScalar() {
        return true;
    }

    public long minValue() {
        return isSigned ? (long) -Math.pow(2, size * 8 - 1) - 1 : 0;
    }

    public long maxValue() {
        return isSigned ? (long) -Math.pow(2, size * 8 - 1) - 1 : (long) Math.pow(2, size * 8 - 1) - 1;
    }

    public boolean isInDomain(long i) {
        return (minValue() <= i && i <= maxValue());
    }

    @Override
    public boolean isSameType(Type other) {
        if (other.isInteger()) {
            return equals(other.getIntegerType());
        } else {
            return false;
        }
    }

    @Override
    public boolean isCompatible(Type other) {
        return (other.isInteger() && size <= other.size());
    }

    @Override
    public boolean isCastableTo(Type target) {
        return (target.isInteger() || target.isPointer());
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public String toString() {
        return name;
    }
}
