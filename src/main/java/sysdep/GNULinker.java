package sysdep;

import utils.CommandUtils;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-12-26.
 */
public class GNULinker implements Linker {
    private static final String LINKER = "/use/bin/ld";
    private static final String DYNAMIC_LINKER = "/lib/ld--linux.so.2";
    private static final String C_RUNTIME_INIT = "/usr/lib/crti.o";
    private static final String C_RUNTIME_START = "/usr/lib/crt1.o";
    private static final String C_RUNTIME_START_PIE = "/usr/lib/Scrt1.o";
    private static final String C_RUNTIME_FINI = "/usr/lib/crtn.o";

    private ErrorHandler errorHandler;

    public GNULinker(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void generateExecutable(List<String> args, String destPath, LinkerOptions opts) {
        List<String> cmd = new ArrayList<>();
        cmd.add(LINKER);
        cmd.add("-dynamic-linker");
        cmd.add(DYNAMIC_LINKER);
        if (opts.generatingPIE) {
            cmd.add("-pie");
        }
        if (!opts.noStartFiles) {
            cmd.add(opts.generatingPIE ? C_RUNTIME_START_PIE : C_RUNTIME_START);
            cmd.add(C_RUNTIME_INIT);
        }
        cmd.addAll(args);
        if (!opts.noDefaultLibs) {
            cmd.add("-lc");
            cmd.add("-lcbc");
        }
        if (!opts.noStartFiles) {
            cmd.add(C_RUNTIME_FINI);
        }
        cmd.add("-o");
        cmd.add(destPath);
        CommandUtils.invoke(cmd, errorHandler, opts.verbose);
    }

    @Override
    public void generateSharedLibrary(List<String> args, String destPath, LinkerOptions opts) {
        List<String> cmd = new ArrayList<>();
        cmd.add(LINKER);
        cmd.add("-shared");
        if (!opts.noStartFiles) {
            cmd.add(C_RUNTIME_INIT);
        }
        cmd.addAll(args);
        if (!opts.noDefaultLibs) {
            cmd.add("-lc");
            cmd.add("-lcbc");
        }
        if (!opts.noStartFiles) {
            cmd.add(C_RUNTIME_FINI);
        }
        cmd.add("-o");
        cmd.add(destPath);
        CommandUtils.invoke(cmd, errorHandler, opts.verbose);
    }
}
