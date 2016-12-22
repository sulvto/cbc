package sysdep.x86;

import asm.SymbolTable;
import asm.Type;

/**
 * Created by sulvto on 16-12-22.
 */
public class Register extends asm.Register {

    RegisterClass _class;
    Type type;

    Register(RegisterClass _class, Type type) {
        this._class = _class;
        this.type = type;
    }

    Register forType(Type type) {
        return new Register(_class, type);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Register) && equals((Register) obj);
    }

    public boolean equals(Register register) {
        return _class.equals(register._class);
    }

    @Override
    public int hashCode() {
        return _class.hashCode();
    }

    String baseName() {
        return _class.toString().toLowerCase();
    }

    @Override
    public String toSource(SymbolTable table) {
        return "%" + typeName();
    }

    private String typeName() {
        switch (type) {
            case INT8:
                return lowerByteRegister();
            case INT16:
                return baseName();
            case INT32:
                return "e" + baseName();
            case INT64:
                return "r" + baseName();
            default:
                throw new Error("unknown register type: " + type);
        }
    }

    private String lowerByteRegister() {
        switch (_class) {
            case AX:
            case BX:
            case CX:
            case DX:
                return baseName().substring(0, 1) + "1";
            default:
                throw new Error("doesnot have lower-byte register: " + _class);
        }
    }

    @Override
    public String dump() {
        return "(Register " + _class.toString() + " " + type.toString() + ")";
    }
}
