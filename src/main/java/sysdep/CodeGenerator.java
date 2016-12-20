package sysdep;

import ir.IR;

/**
 * Created by sulvto on 16-12-20.
 */
public interface CodeGenerator {
    AssemblyCode generate(IR ir);
}
