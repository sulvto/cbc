package sysdep;

import java.util.List;

/**
 * Created by sulvto on 16-12-4.
 */
public interface Linker {
    void generateSharedLibrary(List<String> args, String destPath, LinkerOptions opts);

    void generateExecutable(List<String> args, String destPath, LinkerOptions opts);
}
