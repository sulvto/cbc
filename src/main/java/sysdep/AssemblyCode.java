package sysdep;

import java.io.PrintStream;

/**
 * Created by sulvto on 16-11-26.
 */
public interface AssemblyCode {
    String toSource();

    void dump();

    void dump(PrintStream printStream);
}
