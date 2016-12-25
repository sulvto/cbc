package sysdep.x86;

import asm.*;
import compiler.Parameter;
import entity.*;
import ir.*;
import sysdep.CodeGeneratorOptions;
import utils.AsmUtils;
import utils.ErrorHandler;
import utils.ListUtils;

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

    static final String LABEL_SYMBOL_BASE = ".L";
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

    private static final Symbol GOT = new NamedSymbol("_GLOBAL_OFFSET_TABLE_");

    private void loadGOTBaseAddress(AssemblyCode file, Register register) {
        file.call(PICThunkSymbol(register));
        file.add(imm(GOT), register);
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

    private Symbol PLTSymbol(Symbol base) {
        return new SuffixedSymbol(base, "@PLT");
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

    private long alienStack(long size) {
        return AsmUtils.align(size, STACK_WORD_SIZE);
    }

    private long stackSizeFromWordNum(long numWords) {
        return numWords * STACK_WORD_SIZE;
    }

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
            compileStmt(s);
        }
        as.lalel(epilogue);
        return as;
    }

    private List<Register> usedCalleeSaveRegisters(AssemblyCode body) {
        List<Register> result = new ArrayList<>();
        for (Register reg : calleeSaveRegisters()) {
            if (body.doesUser(reg)) {
                result.add(reg);
            }
        }
        result.remove(bp());
        return result;
    }

    final static RegisterClass[] CALLEE_SAVE_REGISTERS = {
            RegisterClass.BX, RegisterClass.BP, RegisterClass.SI, RegisterClass.DI
    };

    private List<Register> calleeSaveRegistersCache = null;

    private List<Register> calleeSaveRegisters() {
        if (calleeSaveRegistersCache == null) {
            List<Register> regs = new ArrayList<>();
            for (RegisterClass c : CALLEE_SAVE_REGISTERS) {
                regs.add(new Register(c, naturalType));
            }
            calleeSaveRegistersCache = regs;
        }
        return calleeSaveRegistersCache;
    }

    private void generateFunctionBody(AssemblyCode file, AssemblyCode body, StackFrameInfo frame) {
        file.virtualStack.reset();
        prologue(file, frame.saveRegs, frame.frameSize());
        if (options.isPositionIndependent() && body.doesUser(GOTBaseReg())) {
            loadGOTBaseAddress(file, GOTBaseReg());
        }
        file.addAll(body.assemblies());
        epilogue(file, frame.saveRegs);
        file.virtualStack.fixOffset(0);
    }

    private void prologue(AssemblyCode file, List<Register> saveRegs, long frameSize) {

        file.push(bp());
        file.mov(sp(), bp());
        for (Register reg : saveRegs) {
            file.virtualPush(reg);
        }
        extendStack(file, frameSize);
    }

    private void epilogue(AssemblyCode file, List<Register> saveRegs) {
        for (Register reg : ListUtils.reverse(saveRegs)) {
            file.virtualPop(reg);
        }
        file.mov(bp(), sp());
        file.pop(bp());
        file.ret();
    }

    private final static long PARAM_START_WORD = 2;

    private void locateParameters(List<Parameter> parameters) {
        long numWords = PARAM_START_WORD;
        for (Parameter var : parameters) {
            var.setMemref(mem(stackSizeFromWordNum(numWords), bp()));
            numWords++;
        }
    }

    private long locateLocalVariables(LocalScope scope) {
        return locateLocalVariables(scope, 0);
    }

    private long locateLocalVariables(LocalScope scope, long parentStackLen) {
        long len = parentStackLen;
        for (DefinedVariable var : scope.localVariable()) {
            len = alienStack(len + var.allocSize());
            var.setMemref(relocatableMem(-len, bp()));
        }

        long maxLen = len;
        for (LocalScope s : scope.children()) {
            long childLen = locateLocalVariables(s, len);
            maxLen = Math.max(childLen, maxLen);
        }

        return maxLen;
    }

    private IndirectMemoryReference relocatableMem(long offset, Register base) {
        return IndirectMemoryReference.relocatable(offset, base);
    }

    private void fixLocalVariableOffsets(LocalScope localScope, long len) {
        for (DefinedVariable var : localScope.allLocalVariables()) {
            var.getMemref().fixOffset(-len);
        }
    }

    private void fixTempVariableOffsets(AssemblyCode asm, long len) {
        asm.virtualStack.fixOffset(-len);
    }

    private void extendStack(AssemblyCode file, long len) {
        if (len > 0) {
            file.sub(imm(len), sp());
        }
    }

    private AssemblyCode newAssemblyCode() {
        return new AssemblyCode(naturalType, STACK_WORD_SIZE, new SymbolTable(LABEL_SYMBOL_BASE), options.isVerboseAsm());
    }

    private void compileStmt(Stmt stmt) {
        if (options.isVerboseAsm()) {
            if (stmt.getLocation() != null) {
                as.comment(stmt.getLocation().numberedLine());
            }
        }
        stmt.accept(this);
    }


    @Override
    public Void visit(Call call)
    {

        return null;
    }

    @Override
    public Void visit(Return aReturn) {
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
    public Void visit(CJump cJump) {
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

    //
    // Expressions
    //

    private void compile(Expr n) {
        if (options.isVerboseAsm()) {
            as.comment(n.getClass().getSimpleName() + " {");
            as.indentComment();
        }
        n.accept(this);
        if (options.isVerboseAsm()) {
            as.unindentComment();
            as.comment("}");
        }
    }

    @Override
    public Void visit(Bin node) {
        Op op = node.getOp();
        Type t = node.getType();
        if (node.getRight().isConstant() && !doesRequireRegisterOperand(op)) {
            compile(node.getLeft());
            compileBinaryOp(op, ax(t), node.getRight().asmValue());
        } else if (node.getRight().isConstant()) {
            compile(node.getLeft());
            loadConstant(node.getRight(), cx());
            compileBinaryOp(op, ax(t), cx(t));
        } else if (node.getRight().isVar()) {
            compile(node.getRight());
            loadVariable((Var) node.getRight().getEntityForce(), cx(t));
            compileBinaryOp(op, ax(t), cx(t));
        } else if (node.getLeft().isConstant() || node.getLeft().isVar() || node.getLeft().isAddr()) {
            compile(node.getRight());
            as.mov(ax(), cx());
            compile(node.getLeft());
            compileBinaryOp(op, ax(t), cx(t));
        } else {
            compile(node.getRight());
            as.virtualPush(ax());
            compile(node.getLeft());
            as.virtualPop(cx());
            compileBinaryOp(op, ax(t), cx(t));
        }
        return null;
    }

    private boolean doesRequireRegisterOperand(Op op) {
        switch (op) {
            case S_DIV:
            case U_DIV:
            case S_MOD:
            case U_MOD:
            case BIT_LSHIFT:
            case BIT_RSHIFT:
            case ARITH_RSHIFT:
                return true;
            default:
                return false;
        }
    }

    private void compileBinaryOp(Op op, Register left, Register right) {
        switch (op) {
            case ADD:
                as.add(right, left);
                break;
            case SUB:
                as.sub(right, left);
                break;
            case MUL:
                as.imul(right, left);
                break;
            case S_DIV:
            case S_MOD:
                as.cltd();
                as.idiv(cx(right.type));
                if (op == Op.S_MOD) {
                    as.mov(dx(), left);
                }
                break;
            case U_DIV:
            case U_MOD:
                as.mov(imm(0), dx());
                as.div(cx(left.type));
                if (op == Op.U_MOD) {
                    as.mov(dx(), left);
                }
                break;
            case BIT_AND:
                as.add(right, left);
                break;
            case BIT_OR:
                as.or(right, left);
                break;
            case BIT_XOR:
                as.xor(right, left);
                break;
            case BIT_LSHIFT:
                as.sal(c1(), left);
                break;
            case BIT_RSHIFT:
                as.shr(cl(), left);
                break;
            case ARITH_RSHIFT:
                as.sar(cl(), left);
                break;
            default:
                as.cmp(right, ax(left.type));
                switch (op) {
                    case EQ:     as.sete (al()); break;
                    case NEQ:    as.setne(al()); break;
                    case S_GT:   as.setg (al()); break;
                    case S_GTEQ: as.setge(al()); break;
                    case S_LT:   as.setl (al()); break;
                    case S_LTEQ: as.setle(al()); break;
                    case U_GT:   as.seta (al()); break;
                    case U_GTEQ: as.setae(al()); break;
                    case U_LT:   as.setb (al()); break;
                    case U_LTEQ: as.setbe(al()); break;
                    default:
                        throw new Error("unknown binary operator: " + op);
                }
                as.movzx(al(),left);
        }
    }

    @Override
    public Void visit(Uni node) {
        Type src = node.getExpr().getType();
        Type dest = node.getType();
        compile(node.getExpr());
        switch (node.getOp()) {
            case UMINUS:
                as.neg(ax(src));
                break;
            case BIT_NOT:
                as.not(ax(src));
                break;
            case NOT:
                as.text(ax(src),ax(src));
                as.sete(al());
                as.movzx(al(),ax(dest));
                break;
            case S_CAST:
                as.movsx(ax(src), ax(dest));
                break;
            case U_CAST:
                as.movzx(ax(src),ax(dest));
                break;
            default:
                throw new Error("unknown unary operator: " + node.getOp());
        }
        return null;
    }

    @Override
    public Void visit(Var node) {
        loadVariable(node, ax());
        return null;
    }

    @Override
    public Void visit(Int node) {
        as.mov(imm(node.getValue()), ax());
        return null;
    }

    @Override
    public Void visit(Str node) {
        loadConstant(node, ax());
        return null;
    }

    @Override
    public Void visit(Assign node) {
        if (node.getLhs().isAddr() && node.getLhs().memref() != null) {
            compile(node.getLhs());
            store(ax(node.getLhs().getType()), node.getLhs().memref());
        } else if (node.getRhs().isConstant()) {
            compile(node.getLhs());
            as.mov(ax(), cx());
            loadConstant(node.getRhs(), ax());
            store(ax(node.getLhs().getType()), mem(cx()));
        } else {
            compile(node.getRhs());
            as.virtualPush(ax());
            compile(node.getLhs());
            as.mov(ax(), cx());
            as.virtualPop(ax());
            store(ax(node.getLhs().getType()), mem(cx()));
        }
        return null;
    }

    @Override
    public Void visit(Mem node) {
        compile(node.getExpr());
        load(mem(ax()), ax(node.getType()));
        return null;
    }

    @Override
    public Void visit(Addr node) {
        loadAddress(node.getEntity(), ax());
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

    private Register cl() {
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

    private DirectMemoryReference mem(Symbol sym) {
        return new DirectMemoryReference(sym);
    }

    private IndirectMemoryReference mem(Register reg) {
        return new IndirectMemoryReference(0, reg);
    }

    private IndirectMemoryReference mem(long offset, Register reg) {
        return new IndirectMemoryReference(offset, reg);
    }

    private IndirectMemoryReference mem(Symbol offset, Register reg) {
        return new IndirectMemoryReference(offset, reg);
    }

    private ImmediateValue imm(long n) {
        return new ImmediateValue(n);
    }

    private ImmediateValue imm(Symbol sym) {
        return new ImmediateValue(sym);
    }

    private ImmediateValue imm(Literal literal) {
        return new ImmediateValue(literal);
    }


}
