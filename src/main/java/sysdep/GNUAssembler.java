package sysdep;

import exception.IPCException;
import utils.CommandUtils;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-12-26.
 */
public class GNUAssembler implements Assembler {
    private ErrorHandler errorHandler;

    public GNUAssembler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void assemble(String srcPath, String destPath, AssemblerOptions options) throws IPCException {
        List<String> cmd = new ArrayList<>();
        cmd.add("as");
        cmd.addAll(options.args);
        cmd.add("-o");
        cmd.add(destPath);
        cmd.add(srcPath);
        CommandUtils.invoke(cmd, errorHandler, options.verbose);
    }
}
