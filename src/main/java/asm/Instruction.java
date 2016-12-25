package asm;

import utils.TextUtils;

/**
 * Created by sulvto on 16-11-26.
 */
public class Instruction extends Assembly {
    protected String mnemonic;
    protected String suffix;
    protected Operand[] operands;
    protected boolean needRelocation;

    public Instruction(String mnemonic) {
        this(mnemonic, "", new Operand[0], false);
    }

    public Instruction(String mnemonic, String suffix, Operand a1) {
        this(mnemonic, suffix, new Operand[]{a1}, false);
    }

    public Instruction(String mnemonic, String suffix, Operand a1, Operand a2) {
        this(mnemonic, suffix, new Operand[]{a1, a2}, false);
    }

    public Instruction(String mnemonic, String suffix, Operand a1, Operand a2, boolean reloc) {
        this(mnemonic, suffix, new Operand[]{a1, a2}, reloc);
    }

    public Instruction(String mnemonic, String suffix, Operand[] operands, boolean reloc) {
        this.mnemonic = mnemonic;
        this.suffix = suffix;
        this.operands = operands;
        this.needRelocation = reloc;
    }

    public Instruction build(String mnemonic, Operand o1) {
        return new Instruction(mnemonic, this.suffix, new Operand[]{o1}, needRelocation);
    }

    public Instruction build(String mnemonic, Operand o1, Operand o2) {
        return new Instruction(mnemonic, this.suffix, new Operand[]{o1, o1}, needRelocation);
    }

    @Override
    public boolean isInstruction() {
        return true;
    }

    @Override
    public String toSource(SymbolTable table) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t");
        stringBuilder.append(mnemonic + suffix);
        String seq = "\t";
        for (Operand oper : operands) {
            stringBuilder.append(seq);
            seq = ", ";
            stringBuilder.append(oper.toSource(table));
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "#<Insn " + mnemonic + ">";
    }

    @Override
    public String dump() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(Instruction ");
        stringBuilder.append(TextUtils.dumpString(mnemonic));
        stringBuilder.append(" ");
        stringBuilder.append(TextUtils.dumpString(suffix));
        for (Operand oper : operands) {
            stringBuilder.append(" ").append(oper.dump());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String mnemonic() {
        return this.mnemonic;
    }

    public Operand operand1() {
        return operands[0];
    }

    public Operand operand2() {
        return operands[1];
    }

    public Symbol jmpDestination() {
        DirectMemoryReference ref = (DirectMemoryReference) operands[0];
        return (Symbol) ref.value;
    }
}
