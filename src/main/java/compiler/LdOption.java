package compiler;

/**
 * Created by sulvto on 16-12-27.
 */
public class LdOption implements LdArg {
    private final  String arg;
    public LdOption(String arg) {
        this.arg = arg;
    }

    @Override
    public boolean isSourceFile() {
        return false;
    }

    @Override
    public String toString() {
        return arg;
    }
}
