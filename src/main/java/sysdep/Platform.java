package sysdep;

import sysdep.x86.CodeGenerator;
import type.TypeTable;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-12-4.
 */
public interface Platform {
    TypeTable typeTable();

    CodeGenerator codeGenerator(CodeGeneratorOptions options, ErrorHandler errorHandler);

    Assembler assembler(ErrorHandler errorHandler);

    Linker linker(ErrorHandler errorHandler);
}
