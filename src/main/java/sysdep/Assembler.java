package sysdep;

import exception.IPCException;

/**
 * Created by sulvto on 16-12-4.
 */
public interface Assembler {
    void assemble(String srcPath, String destPath, AssemblerOptions options) throws IPCException;
}
