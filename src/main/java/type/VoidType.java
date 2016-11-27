package type;

/**
 *
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class VoidType extends Type {

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public long size() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VoidType);
    }

    @Override
    public boolean isSameType(Type other) {
        return other.isVoid();
    }

    @Override
    public boolean isCompatible(Type other) {
        return other.isVoid();
    }

    @Override
    public boolean isCastableTo(Type target) {
        return target.isVoid();
    }

    @Override
    public String toString() {
        return "void";
    }
}
