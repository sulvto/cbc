package sysdep.x86;

import asm.*;
import type.Type;

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
    final boolean varbose;
    final VirtualStack virtualStack = new VirtualStack();
    private List<Assembly> assemblies = new ArrayList<>();
    private int commentIndentLevel = 0;
    private Statisties statisties;

    AssemblyCode(Type naturalType, long stackWordSize, SymbolTable labelSymbols, boolean varbose) {
        this.naturalType = naturalType;
        this.stackWordSize = stackWordSize;
        this.labelSymbols = labelSymbols;
        this.varbose = varbose;
    }

    public List<Assembly> assemblies() {
        return assemblies;
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



}
