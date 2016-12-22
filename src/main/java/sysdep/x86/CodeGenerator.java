package sysdep.x86;

import asm.*;
import entity.*;
import ir.*;
import sysdep.CodeGeneratorOptions;
import utils.ErrorHandler;

import java.util.List;

/**
 * Created by sulvto on 16-11-26.
 */
public class CodeGenerator implements sysdep.CodeGenerator, IRVisitor<Void, Void> {
    private final CodeGeneratorOptions options;
    private final Type naturalType;
    private final ErrorHandler errorHandler;

    public CodeGenerator(CodeGeneratorOptions options, Type naturalType, ErrorHandler errorHandler) {
        this.options = options;
        this.naturalType = naturalType;
        this.errorHandler = errorHandler;

    }

    @Override
    public AssemblyCode generate(IR ir) {
        locateSymbols(ir);
        return generateAssemblyCode(ir);
    }

    static final String CONST_SYMBOL_BASE = ".LC";

    private void locateSymbols(IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry ent : ir.constanTable().entries()) {
            locateStringLiteral(ent, constSymbols);
        }
        for (Variable var : ir.allGlobalVariables()) {
            locateGlobalVariable(var);
        }

        for (Function func : ir.allFunctions()) {
            locateFuncation(func);
        }
    }

    private void locateGlobalVariable(Entity ent) {
        Symbol sym = symbol(ent.symbolString(), ent.isPrivate());
        if (options.isPositionIndependent()) {
            if (ent.isPrivate() || optimizeGvarAccess(ent)) {
                ent.setMemref(mem(localGOTSymbol(sym), GOTBaseReg()));
            } else {
                ent.setAddress(mem(globalGOTSymbol(sym), GOTBaseReg()));
            }
        } else {
            ent.setMemref(mem(sym));
            ent.setAddress(imm(sym));
        }
    }

    private Symbol symbol(String sym, boolean isPrivate) {
        return isPrivate ? privateSymbol(sym) : globalSymbol(sym);
    }

    private Symbol globalSymbol(String sym) {
        return new NamedSymbol(sym);
    }

    private Symbol privateSymbol(String sym) {
        return new NamedSymbol(sym);
    }

    private void locateFuncation(Function func) {
        func.setCallingSymbol(callingSymbol(func));
        locateGlobalVariable(func);
    }

    private Symbol callingSymbol(Function func) {
        if (func.isPrivate()) {
            return privateSymbol(func.symbolString());
        } else {
            Symbol sym = globalSymbol(func.symbolString());
            return shouldUsePLT(func) ? PLTSymbol(sym) : sym;
        }
    }

    private boolean shouldUsePLT(Entity ent) {
        return options.isPositionIndependent()&&!optimizeGvarAccess(ent);
    }

    private boolean optimizeGvarAccess(Entity ent) {
        return options.isPIERequired() && ent.isDefined();
    }

    private void locateStringLiteral(ConstantEntry ent, SymbolTable syms) {
        ent.setSymbol(syms.newSymbol());
        if (options.isPositionIndependent()) {
            Symbol offset = localGOTSymbol(ent.getSymbol());
            ent.setMemref(mem(offset, GOTBaseReg()));
        } else {
            ent.setMemref(mem(ent.getSymbol()));
            ent.setAddress(imm(ent.getSymbol()));
        }
    }



    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode file = newAssemblyCode();
        file._file(ir.fileName());

        if (ir.isGlobalVariableDefined()) {
            generateDataSection(file, ir.definedGlobalVariables());
        }
        if (ir.isStringLiteralDefined()) {
            generateReadOnlyDataSction(file,ir.constanTable())
        }
        if (ir.isFunctionDefined()) {
            generateTextSection(file, ir.definedFunctions());
        }
        if (ir.isCommonSymbolDefined()) {
            generateCommonSymbols(file, ir.definedCommonSymbols());
        }
        if (options.isPositionIndependent()) {
            PICThunk(file, GOTBaseReg());
        }
        return file;
    }

    /***
     *
     * @param file
     * @param gvars global variable
     */
    private void generateDataSection(AssemblyCode file, List<DefinedVariable> gvars) {
        file._data();
        for (DefinedVariable var : gvars) {
            Symbol sym = globalSymbol(var.symbolString());
            if (var.isPrivate()) {
                file._globl(sym);
            }
            file._align(var.alignment());
            file._type(sym, "@object");
            file._size(sym, var.allocSize());
            file.label(sym);
            generateImmediate(file, var.getType().allocSize(), var.getIr());
        }
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
        return new AssemblyCode(naturalType, STACK_WORD_SIZE, new SymbolTable(), options.isVerboseAsm());
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
