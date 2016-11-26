package sysdep.x86;

import asm.Register;
import type.Type;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-11-26.
 */
public class CodeGenerator {
    private final CodeGeneratorOptions options;
    private final Type naturalType;
    private final ErrorHandler errorHandler;

    public CodeGenerator(CodeGeneratorOptions options, Type naturalType, ErrorHandler errorHandler) {
        this.options = options;
        this.naturalType = naturalType;
        this.errorHandler = errorHandler;

    }


    private Register ax(Type type) {
        return new Register(RegisterClass.AX, type);
    }

    private Register bx(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register cx(Type type) {
        return new Register(RegisterClass.CX, type);
    }

    private Register dx(Type type) {
        return new Register(RegisterClass.DX, type);
    }

    private Register si(Type type) {
        return new Register(RegisterClass.SI, type);
    }

    private Register di(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register sp(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register bp(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register ax(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register cx(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register al(Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register cl(Type type) {
        return new Register(RegisterClass.BX, type);
    }
}
