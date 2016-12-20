package sysdep.x86;

import asm.Assembly;
import asm.Statisties;
import asm.SymbolTable;
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
}
