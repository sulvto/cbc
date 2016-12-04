package sysdep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-12-4.
 */
public class AssemblerOptions {
    public boolean verbose = false;

    List<String> args = new ArrayList<>();

    public void addArg(String arg) {
        args.add(arg);
    }
}
