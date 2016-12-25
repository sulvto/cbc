package sysdep.x86;

import asm.*;
import utils.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sulvto on 16-12-24.
 */
public class PeepholeOptimizer {
    private Map<String, List<Filter>> filterSet;

    public PeepholeOptimizer() {
        this.filterSet = new HashMap<>();
    }

    public void add(Filter filter) {
        String[] heads = filter.patternHeads();
        for (String head : heads) {
            List<Filter> list = filterSet.get(head);
            if (list == null) {
                list = new ArrayList<>();
                list.add(filter);
                filterSet.put(head, list);
            } else {
                list.add(filter);
            }
        }
    }

    public List<Assembly> optimize(List<Assembly> assemblies) {
        List<Assembly> result = new ArrayList<>();
        Cursor<Assembly> cursor = new Cursor<>(assemblies);
        while (cursor.hasNext()) {
            Assembly asm = cursor.next();
            if (asm.isInstruction()) {
                Filter matched = matchFilter(cursor);
                if (matched != null) {
                    matched.optimize(cursor, result);
                    continue;
                }
            }
            result.add(asm);
        }
        return result;
    }

    private Filter matchFilter(Cursor<Assembly> asms) {
        Instruction insn = (Instruction) asms.current();
        List<Filter> filters = filterSet.get(insn.mnemonic());
        if (filters == null || filters.isEmpty()) return null;

        for (Filter filter : filters) {
            if (filter.match(asms)) {
                return filter;
            }
        }
        return null;
    }

    public static PeepholeOptimizer defaultSet() {
        PeepholeOptimizer set = new PeepholeOptimizer();
        set.loadDefaultFilters();
        return set;
    }

    private void loadDefaultFilters() {
        PeepholeOptimizer set = this;
        // mov
        set.add(new SingleInsnFilter(new InsnPattern("mov", imm(0), reg()), insn -> insn.build("xor", insn.operand2(), insn.operand2())));

        // add
        set.add(new SingleInsnFilter(new InsnPattern("add", imm(-1), reg()), insn -> insn.build("dec", insn.operand2())));

        // add
        set.add(new SingleInsnFilter(new InsnPattern("add", imm(0), reg()), null));

        // add
        set.add(new SingleInsnFilter(new InsnPattern("add", imm(1), reg()), insn -> insn.build("inc", insn.operand2())));

        // sub
        set.add(new SingleInsnFilter(new InsnPattern("sub", imm(-1), reg()), insn -> insn.build("inc", insn.operand2())));

        set.add(new SingleInsnFilter(new InsnPattern("sub", imm(0), reg()), null));

        set.add(new SingleInsnFilter(new InsnPattern("sub", imm(1), reg()), insn -> insn.build("dec", insn.operand2())));

        // imul
        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(0), reg()), insn -> insn.build("xor", insn.operand2())));

        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(1), reg()), null));

        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(2), reg()), insn -> insn.build("sal", imm(1), insn.operand2())));

        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(4), reg()), insn -> insn.build("sal", imm(2), insn.operand2())));

        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(8), reg()), insn -> insn.build("sal", imm(3), insn.operand2())));

        set.add(new SingleInsnFilter(new InsnPattern("imul", imm(16), reg()), insn -> insn.build("sal", imm(4), insn.operand2())));

        set.add(new JumpEliminationFilter());
    }

    private ImmediateValue imm(long n) {
        return new ImmediateValue(n);
    }

    private OperandPattern reg() {
        return new AnyRegisterPattern();
    }


    abstract class Filter {
        abstract public String[] patternHeads();

        abstract public boolean match(Cursor<Assembly> asms);

        abstract public void optimize(Cursor<Assembly> asms, List<Assembly> dest);
    }


    class SingleInsnFilter extends Filter {
        private InsnPattern pattern;
        private InsnTransform transform;

        public SingleInsnFilter(InsnPattern pattern, InsnTransform transform) {
            this.pattern = pattern;
            this.transform = transform;
        }

        @Override
        public String[] patternHeads() {
            return new String[]{pattern.name};
        }

        @Override
        public boolean match(Cursor<Assembly> asms) {
            return pattern.match((Instruction) asms.current());
        }

        @Override
        public void optimize(Cursor<Assembly> src, List<Assembly> dest) {
            if (transform == null) {
                // remove instruction
            } else {
                dest.add(transform.apply((Instruction) src.current()));
            }
        }
    }

    class InsnPattern {
        private String name;
        private OperandPattern pattern1;
        private OperandPattern pattern2;

        InsnPattern(String name, OperandPattern pattern1, OperandPattern pattern2) {
            this.name = name;
            this.pattern1 = pattern1;
            this.pattern2 = pattern2;
        }

        public boolean match(Instruction insn) {
            return name.equals(insn.mnemonic())
                    && (pattern1 == null || pattern1.match(insn.operand1()))
                    && (pattern2 == null || pattern2.match(insn.operand2()));
        }
    }

    class AnyRegisterPattern implements OperandPattern {
        @Override
        public boolean match(Operand operand) {
            return operand.isRegister();
        }
    }


    interface InsnTransform {
        Instruction apply(Instruction insn);
    }


    class JumpEliminationFilter extends Filter {
        private String[] jmpInsns() {
            return new String[]{"jmp", "jz", "jne", "je", "jne"};
        }

        @Override
        public String[] patternHeads() {
            return jmpInsns();
        }

        @Override
        public boolean match(Cursor<Assembly> asms) {
            Instruction insn = (Instruction) asms.current();
            return doesLabelFollows(asms.clone(), insn.jmpDestination());
        }

        private boolean doesLabelFollows(Cursor<Assembly> asms, Symbol jmpDest) {
            while (asms.hasNext()) {
                Assembly asm = asms.next();
                if (asm.isLabel()) {
                    Label label = (Label) asm;
                    if (label.getSymbol().equals(jmpDest)) {
                        return true;
                    }
                } else if (asm.isComment()) {
                } else {
                    // instructions or directives
                    return false;
                }
            }
            return false;
        }

        @Override
        public void optimize(Cursor<Assembly> asms, List<Assembly> dest) {

        }
    }
}


