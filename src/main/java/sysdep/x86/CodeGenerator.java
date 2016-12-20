package sysdep.x86;

import asm.Label;
import asm.SymbolTable;
import asm.Type;
import entity.DefinedFunction;
import ir.*;
import sysdep.CodeGeneratorOptions;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-11-26.
 */
public class CodeGenerator implements sysdep.CodeGenerator , IRVisitor<Void,Void>{
    private final CodeGeneratorOptions options;
    private final Type naturalType;
    private final ErrorHandler errorHandler;

    public CodeGenerator(CodeGeneratorOptions options, Type naturalType, ErrorHandler errorHandler) {
        this.options = options;
        this.naturalType = naturalType;
        this.errorHandler = errorHandler;

    }

    private AssemblyCode as;
    private Label epilogue;

    private AssemblyCode compileStmts(DefinedFunction func) {
        as = newAssemblyCode();
        epilogue = new Label();
        for (Stmt s : func.getIr()) {
            compileStme(s);
        }
        as.lalel(epilogue);
        return as;
    }

    private AssemblyCode newAssemblyCode() {
        return new AssemblyCode(naturalType,STACK_WORD_SIZE,new SymbolTable(),options.isVerboseAsm());
    }

    private void compileStme(Stmt stmt) {
        if (options.isVerboseAsm()) {
            if (stmt.getLocation() != null) {
                as.comment(stmt.getLocation().numberedLine());
            }
        }
        stmt.accept(this);
    }

    @Override
    public Void visit(Assign assign) {
        return null;
    }

    @Override
    public Void visit(CJump cJump) {
        return null;
    }

    @Override
    public Void visit(ExprStmt exprStmt) {
        return null;
    }

    @Override
    public Void visit(LabelStmt labelStmt) {
        return null;
    }

    @Override
    public Void visit(Jump jump) {
        return null;
    }

    @Override
    public Void visit(Switch aSwitch) {
        return null;
    }

    @Override
    public Void visit(Return aReturn) {
        return null;
    }

    @Override
    public Void visit(Bin bin) {
        return null;
    }

    @Override
    public Void visit(Int anInt) {
        return null;
    }

    @Override
    public Void visit(Addr addr) {
        return null;
    }

    @Override
    public Void visit(Call call) {
        return null;
    }

    @Override
    public Void visit(Mem mem) {
        return null;
    }

    @Override
    public Void visit(Str str) {
        return null;
    }

    @Override
    public Void visit(Uni uni) {
        return null;
    }

    @Override
    public Void visit(Var var) {
        return null;
    }

    @Override
    public AssemblyCode generate(IR ir) {
        return null;
    }
//
//
//    private Register ax(Type type) {
//        return new Register(RegisterClass.AX, type);
//    }
//
//    private Register bx(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register cx(Type type) {
//        return new Register(RegisterClass.CX, type);
//    }
//
//    private Register dx(Type type) {
//        return new Register(RegisterClass.DX, type);
//    }
//
//    private Register si(Type type) {
//        return new Register(RegisterClass.SI, type);
//    }
//
//    private Register di(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register sp(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register bp(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register ax(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register cx(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register al(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
//
//    private Register cl(Type type) {
//        return new Register(RegisterClass.BX, type);
//    }
}
