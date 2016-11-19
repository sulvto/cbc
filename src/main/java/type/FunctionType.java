package type;

/**
 * Created by sulvto on 16-11-15.
 */
public class FunctionType extends Type {
    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean isSameType(Type other) {
        return false;
    }

    @Override
    public boolean isCompatible(Type other) {
        return false;
    }

    @Override
    public boolean isCastableTo(Type target) {
        return false;
    }
}
