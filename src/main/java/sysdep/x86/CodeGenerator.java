package sysdep.x86;

import asm.*;
import entity.*;
import ir.*;
import sysdep.CodeGeneratorOptions;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sulvto on 16-11-26.
 */
public class CodeGenerator implements sysdep.CodeGenerator, IRVisitor<Void, Void>, ELFConstants {
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
        return options.isPositionIndependent() && !optimizeGvarAccess(ent);
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
            generateReadOnlyDataSection(file, ir.constanTable());
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

    private void generateImmediate(AssemblyCode file, long size, Expr node) {
        if (node instanceof Int) {
            Int exp = (Int) node;
            switch ((int) size) {
                case 1:
                    file._byte(exp.getValue());
                    break;
                case 2:
                    file._value(exp.getValue());
                    break;
                case 4:
                    file._long(exp.getValue());
                    break;
                case 8:
                    file._quad(exp.getValue());
                    break;
                default:
                    throw new Error("entry size must be 1,2,4,8");
            }
        } else if (node instanceof Str) {
            Str expr = (Str) node;
            switch ((int) size) {
                case 4:
                    file._long(expr.getSymbol());
                    break;
                case 8:
                    file._quad(expr.getSymbol());
                    break;
                default:
                    throw new Error("pointer size must be  4,8");
            }
        } else {
            throw new Error("unknown literal node type" + node.getClass());
        }
    }

    private void generateReadOnlyDataSection(AssemblyCode file, ConstantTable constants) {
        file._section(".rodata");
        for (ConstantEntry ent : constants) {
            file.label(ent.getSymbol());
            file._string(ent.getValue());
        }
    }

    private void generateTextSection(AssemblyCode file, List<DefinedFunction> functions) {
        file._text();
        for (DefinedFunction func : functions) {
            Symbol sym = globalSymbol(func.getName());
            if (!func.isPrivate()) {
                file._globl(sym);
            }
            file._type(sym, "@function");
            file.label(sym);
            compileFunctionBody(file, func);
            file._size(sym, ".-" + sym.toSource());
        }
    }

    private void generateCommonSymbols(AssemblyCode file, List<DefinedVariable> variables) {
        for (DefinedVariable var : variables) {
            Symbol sym = globalSymbol(var.symbolString());
            if (var.isPrivate()) {
                file._local(sym);
            }
            file._comm(sym, var.allocSize(), var.alignment());
        }
    }


    private Register GOTBaseReg() {
        return bx();
    }

    private Symbol globalGOTSymbol(Symbol base) {
        return new SuffixedSymbol(base, "@GOT");
    }

    private Symbol localGOTSymbol(Symbol base) {
        return new SuffixedSymbol(base, "@GOTOFF");
    }

    private Symbol PICThunkSymbol(Register reg) {
        return new NamedSymbol("__i886.get_pc_thunk." + reg.baseName());
    }

    private static final String PICThunkSectionFlags = SectionFlag_allocatable
            + SectionFlag_executable
            + SectionFlag_sectiongroup;

    private void PICThunk(AssemblyCode file, Register reg) {
        Symbol sym = PICThunkSymbol(reg);
        file._section(".text" + "." + sym.toSource(), "\"" + PICThunkSectionFlags + "\"",
                SectionType_bits,
                sym.toSource(), Linkage_linkonce);
        file._globl(sym);
        file._hidden(sym);
        file._type(sym, SymbolType_function);
        file.label(sym);
        file.mov(mem(sp()), reg);
        file.ret();
    }

    private static final long STACK_WORD_SIZE = 4;

    class StackFrameInfo {
        List<Register> saveRegs;
        long lvarSize;
        long tempSize;

        long saveRegsSize() {
            return saveRegs.size() * STACK_WORD_SIZE;
        }

        long lvarOffset() {
            return saveRegsSize();
        }

        long tempOffset() {
            return saveRegsSize() + lvarSize;
        }

        long frameSize() {
            return saveRegsSize() + lvarSize + tempSize;
        }
    }

    private void compileFunctionBody(AssemblyCode file, DefinedFunction func) {
        StackFrameInfo frame = new StackFrameInfo();
        locateParameters(func.parameters());
        frame.lvarSize = locateLocalVariables(func.lvarScope());

        AssemblyCode body = optimize(compileStmts(func));
        frame.saveRegs = usedCalleeSaveRegisters(body);
        frame.tempSize = body.virtualStack.maxSize();

        fixLocalVariableOffsets(func.lvarScope(), frame.lvarOffset());
        fixTempVariableOffsets(body, frame.tempOffset());

        if (options.isVerboseAsm()) {
            printStackFrameLayout(file, frame, func.localVariables());
        }

        generateFunctionBody(file, body, frame);
    }

    private AssemblyCode optimize(AssemblyCode body) {
        if (options.optimizeLevel() < 1) {
            return body;
        }
        body.apply(PeepholeOptimizer.defaultSet());
        body.reduceLabels();
        return body;
    }

    class MemInfo {
        MemoryReference mem;
        String name;

        MemInfo(MemoryReference mem, String name) {
            this.mem = mem;
            this.name = name;
        }
    }

    private void printStackFrameLayout(AssemblyCode file, StackFrameInfo frame, List<DefinedVariable> lvars) {

        List<MemInfo> vars = new ArrayList<>();
        for (DefinedVariable var : lvars) {
            vars.add(new MemInfo(var.getMemref(), var.getName()));
        }

        vars.add(new MemInfo(mem(0, bp()), "return address"));
        vars.add(new MemInfo(mem(4, bp()), "save %ebp"));
        if (frame.saveRegsSize() > 0) {
            vars.add(new MemInfo(mem(-frame.saveRegsSize(), bp()), "saved callee-saved registers (" + frame.saveRegsSize() + " bytes)"));
        }
        if (frame.tempSize > 0) {
            vars.add(new MemInfo(mem(-frame.frameSize(), bp()), "tmp variables (" + frame.saveRegsSize() + frame.tempSize + " bytes)"));
        }

        Collections.sort(vars, (o1, o2) -> o1.mem.compareTo(o2.mem));


        file.comment("---- Stack Frame Layout ------");
        for (MemInfo info : vars) {
            file.comment(info.mem.toString() + ": " + info.name);
        }
        file.comment("------------------------------");
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


    private Register ax() {
        return ax(naturalType);
    }

    private Register al() {
        return ax(Type.INT8);
    }

    private Register bx() {
        return bx(naturalType);
    }

    private Register cx() {
        return cx(Type.INT8);
    }

    private Register c1() {
        return cx(Type.INT8);
    }

    private Register dx() {
        return dx(naturalType);
    }

    private Register ax(Type type) {
        return new Register(RegisterClass.BX, type);
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

    private Register si() {
        return new Register(RegisterClass.SI, naturalType);
    }

    private Register di() {
        return new Register(RegisterClass.DI, naturalType);
    }

    private Register bp() {
        return new Register(RegisterClass.BP, naturalType);
    }

    private Register sp() {
        return new Register(RegisterClass.SP, naturalType);
    }
}
