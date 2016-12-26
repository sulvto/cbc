package sysdep;

import asm.Type;
import sysdep.x86.CodeGenerator;
import type.TypeTable;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-12-4.
 */
public class X86Linux implements Platform {
    @Override
    public TypeTable typeTable() {
        return TypeTable.ilp32();
    }

    @Override
    public CodeGenerator codeGenerator(CodeGeneratorOptions options, ErrorHandler errorHandler) {
        return new CodeGenerator(options, naturalType(), errorHandler);
    }

    private Type naturalType() {
        return Type.INT32;
    }

    @Override
    public Assembler assembler(ErrorHandler errorHandler) {
        return new GNUAssembler(errorHandler);
    }

    @Override
    public Linker linker(ErrorHandler errorHandler) {
        return new GNULinker(errorHandler);
    }
}
