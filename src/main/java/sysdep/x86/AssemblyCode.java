package sysdep.x86;

import asm.*;
import utils.TextUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulvto on 16-12-20.
 */
public class AssemblyCode implements sysdep.AssemblyCode {
    final Type naturalType;
    final long stackWordSize;
    final SymbolTable labelSymbols;
    final boolean verbose;
    final VirtualStack virtualStack = new VirtualStack();
    private List<Assembly> assemblies = new ArrayList<>();
    private int commentIndentLevel = 0;
    private Statistics statistics;

    AssemblyCode(Type naturalType, long stackWordSize, SymbolTable labelSymbols, boolean verbose) {
        this.naturalType = naturalType;
        this.stackWordSize = stackWordSize;
        this.labelSymbols = labelSymbols;
        this.verbose = verbose;
    }

    public List<Assembly> assemblies() {
        return assemblies;
    }

    void addAll(List<Assembly> assemblies) {
        this.assemblies.addAll(assemblies);
    }

    @Override
    public String toSource() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Assembly asm : assemblies) {
            stringBuilder.append(asm.toSource(labelSymbols));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void dump() {
        dump(System.out);
    }

    @Override
    public void dump(PrintStream printStream) {
        for (Assembly asm : assemblies) {
            printStream.println(asm.dump());
        }
    }

    void apply(PeepholeOptimizer opt) {
        assemblies = opt.optimize(assemblies);
    }

    private Statistics statistics() {
        if (statistics == null) {
            statistics = Statistics.collect(assemblies);
        }
        return statistics;
    }

    boolean doesUser(Register reg) {
        return statistics().doesRegisterUsed(reg);
    }

    public void comment(String str) {
        assemblies.add(new Comment(str, commentIndentLevel));
    }

    void indentComment() {
        commentIndentLevel++;
    }

    void unindentComment() {
        commentIndentLevel--;
    }


    public void label(Symbol sym) {
        assemblies.add(new Label(sym));
    }

    public void label(Label label) {
        assemblies.add(label);
    }

    public void reduceLabels() {
        Statistics stats = statistics();
        List<Assembly> result = new ArrayList<>();
        for (Assembly asm : assemblies) {
            if (asm.isLabel() && !stats.doesSymbolUsed((Label) asm)) {

            } else {
                result.add(asm);
            }
        }
        assemblies = result;
    }

    private void directive(String direc) {
        assemblies.add(new Directive(direc));
    }

    private void insn(String op) {
        assemblies.add(new Instruction(op));
    }

    private void insn(String op, Operand a) {
        assemblies.add(new Instruction(op, "", a));
    }

    private void insn(String op, String suffix, Operand a) {
        assemblies.add(new Instruction(op, suffix, a));
    }

    private void insn(Type t, String op, Operand a) {
        assemblies.add(new Instruction(op, typeSuffix(t), a));
    }

    private void insn(String op, String suffix, Operand a, Operand b) {
        assemblies.add(new Instruction(op, suffix, a, b));
    }

    private void insn(Type t, String op, Operand a, Operand b) {
        assemblies.add(new Instruction(op, typeSuffix(t), a, b));
    }

    private String typeSuffix(Type t1, Type t2) {
        return typeSuffix(t1) + typeSuffix(t2);
    }

    private String typeSuffix(Type t) {
        switch (t) {
            case INT8:
                return "b";
            case INT16:
                return "w";
            case INT32:
                return "l";
            case INT64:
                return "q";
            default:
                throw new Error("unknown register type: " + t.size());
        }
    }


    //
    // directives
    //

    void _file(String name) {
        directive(".file\t" + TextUtils.dumpString(name));
    }

    void _text() {
        directive("\t.text");
    }

    void _data() {
        directive("\t.data");
    }

    void _section(String name) {
        directive("\t.section\t" + name);
    }

    void _section(String name, String flags, String type, String group, String linkage) {
        directive("\t.section\t" + name + "," + flags + "," + type + "," + group + "," + linkage);
    }

    void _globl(Symbol sym) {
        directive(".globl" + sym.getName());
    }

    void _local(Symbol sym) {
        directive(".local" + sym.getName());
    }

    void _hidden(Symbol sym) {
        directive("\t.hidden\t" + sym.getName());
    }

    void _comm(Symbol sym, long size, long alignment) {
        directive("\t._comm\t" + sym.getName() + "," + size + "," + alignment);
    }

    void _align(long n) {
        directive("\t.align\t" + n);
    }

    void _type(Symbol sym, String type) {
        directive("\t.type\t" + sym.getName() + "," + type);
    }

    void _size(Symbol sym, long size) {
        _size(sym, Long.toString(size));
    }

    void _size(Symbol sym, String size) {
        directive("\t.size\t" + sym.getName() + "," + size);
    }

    void _byte(long val) {
        directive(".byte\t" + new IntegerLiteral((byte) val).toSource());
    }

    void _value(long val) {
        directive(".value\t" + new IntegerLiteral((short) val).toSource());
    }

    void _long(long val) {
        directive(".long\t" + new IntegerLiteral((int) val).toSource());
    }

    void _quad(long val) {
        directive(".quad\t" + new IntegerLiteral(val).toSource());
    }

    void _byte(Literal val) {
        directive(".byte\t" + val.toSource());
    }

    void _value(Literal val) {
        directive(".value\t" + val.toSource());
    }

    void _long(Literal val) {
        directive(".long\t" + val.toSource());
    }

    void _quad(Literal val) {
        directive(".quad\t" + val.toSource());
    }

    void _string(String str) {
        directive("\t.string\t" + TextUtils.dumpString(str));
    }


    //
    // Instructions
    //

    void jmp(Label label) {
        insn("jmp", new DirectMemoryReference(label.getSymbol()));
    }

    void jnz(Label label) {
        insn("jnz", new DirectMemoryReference(label.getSymbol()));
    }

    void je(Label label) {
        insn("je", new DirectMemoryReference(label.getSymbol()));
    }

    void cmp(Operand a, Register b) {
        insn(b.type, "cmp", a, b);
    }

    void sete(Register reg) {
        insn("sete", reg);
    }

    void setne(Register reg) {
        insn("setne", reg);
    }

    void seta(Register reg) {
        insn("seta", reg);
    }

    void setae(Register reg) {
        insn("setae", reg);
    }

    void setb(Register reg) {
        insn("setb", reg);
    }

    void setbe(Register reg) {
        insn("setbe", reg);
    }

    void setg(Register reg) {
        insn("setg", reg);
    }

    void setge(Register reg) {
        insn("setge", reg);
    }

    void setl(Register reg) {
        insn("setl", reg);
    }

    void setle(Register reg) {
        insn("setle", reg);
    }

    void test(Register a, Register b) {
        insn(b.type, "test", a, b);
    }

    void push(Register reg) {
        insn("push", typeSuffix(naturalType), reg);
    }

    void pop(Register reg) {
        insn("pop", typeSuffix(naturalType), reg);
    }

    // call function by relative address
    void call(Symbol sym) {
        insn("call", new DirectMemoryReference(sym));
    }

    // call function by absolute address
    void callAbsolute(Register reg) {
        insn("call", new AbsoluteAddress(reg));
    }

    void ret() {
        insn("ret");
    }

    void mov(Register src, Register dest) {
        insn(dest.type, "mov", src, dest);
    }

    // load
    void mov(Operand src, Register dest) {
        insn(dest.type, "mov", src, dest);
    }

    // save
    void mov(Register src, Operand dest) {
        insn(src.type, "mov", src, dest);
    }

    void relocatableMov(Operand src, Operand dest) {
        assemblies.add(new Instruction("mov", typeSuffix(naturalType), src, dest, true));
    }

    void movsx(Register src, Register dest) {
        insn("movs", typeSuffix(src.type, dest.type), src, dest);
    }

    void movzx(Register src, Register dest) {
        insn("movz", typeSuffix(src.type, dest.type), src, dest);
    }

    void movab(Register src, Register dest) {
        insn("movz", "b" + typeSuffix(dest.type), src, dest);
    }

    void lea(Operand src, Register dest) {
        insn(naturalType, "lea", src, dest);
    }

    void neg(Register reg) {
        insn(reg.type, "neg", reg);
    }

    void add(Operand diff, Register base) {
        insn(base.type, "and", diff, base);
    }

    void sub(Operand diff, Register base) {
        insn(base.type, "sub", base);
    }

    void imul(Operand m, Register base) {
        insn(base.type, "imul", base);
    }

    void cltd() {
        insn("cltd");
    }

    void div(Register base) {
        insn(base.type, "div", base);
    }

    void idiv(Register base) {
        insn(base.type, "idiv", base);
    }

    void not(Register reg) {
        insn(reg.type, "not", reg);
    }

    void and(Operand bits, Register base) {
        insn(base.type, "and", bits, base);
    }

    void or(Operand bits, Register base) {
        insn(base.type, "or", bits, base);
    }

    void xor(Operand bits, Register base) {
        insn(base.type, "xor", bits, base);
    }

    void sar(Register bits, Register base) {
        insn(base.type, "sar", bits, base);
    }


    void sal(Register bits, Register base) {
        insn(base.type, "sal", bits, base);
    }

    void shr(Register bits, Register base) {
        insn(base.type, "shr", bits, base);
    }


    class VirtualStack {
        private long offset;
        private long max;
        private List<IndirectMemoryReference> memrefs = new ArrayList<>();

        VirtualStack() {
            reset();
        }

        void reset() {
            offset = 0;
            max = 0;
            memrefs.clear();
        }

        long maxSize() {
            return max;
        }

        void extend(long len) {
            offset += len;
            max = Math.max(offset, max);
        }

        void rewind(long len) {
            offset -= len;
        }

        IndirectMemoryReference top() {
            IndirectMemoryReference mem = relocatableMen(-offset, bp());
            memrefs.add(mem);
            return mem;
        }

        private IndirectMemoryReference relocatableMen(long offset, Register base) {
            return IndirectMemoryReference.relocatable(offset, base);
        }

        private Register bp() {
            return new Register(RegisterClass.BP, naturalType);
        }

        void fixOffset(long diff) {
            for (IndirectMemoryReference mem : memrefs) {
                mem.fixOffset(diff);
            }
        }
    }

    void virtualPop(Register reg) {
        if (verbose) {
            comment("push " + reg.baseName() + " -> " + virtualStack.top());
        }
        virtualStack.extend(stackWordSize);
        mov(reg, virtualStack.top());
    }

    void virtualPush(Register reg) {
        if (verbose) {
            comment("pop " + reg.baseName() + " <- " + virtualStack.top());
        }
        mov(virtualStack.top(), reg);
        virtualStack.rewind(stackWordSize);
    }

}
