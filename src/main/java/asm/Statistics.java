package asm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DONE
 * Created by sulvto on 16-11-26.
 */
public class Statistics {
    private Map<Register, Integer> registerUsage;
    private Map<String, Integer> insnUsage;
    private Map<Symbol, Integer> symbolUsage;

    public static Statistics collect(List<Assembly> assemblies) {
        Statistics stats = new Statistics();
        for (Assembly asm : assemblies) {
            asm.collectStatistics(stats);
        }
        return stats;
    }

    public Statistics() {
        registerUsage = new HashMap<>();
        insnUsage = new HashMap<>();
        symbolUsage = new HashMap<>();
    }

    public boolean doesRegisterUsed(Register register) {
        return numRegisterUsage(register) > 0;
    }

    public int numRegisterUsage(Register register) {
        return fetchCount(registerUsage, register);
    }

    public void registerUsed(Register register) {
        incrementCount(registerUsage, register);
    }

    public int numInstructionUsed(String insn) {
        return fetchCount(insnUsage, insn);
    }

    public void instructionUsed(String insn) {
        incrementCount(insnUsage, insn);
    }

    public boolean doesSymbolUsed(Label label) {
        return doesSymbolUsed(label.getSymbol());
    }

    public boolean doesSymbolUsed(Symbol symbol) {
        return numSymbolUsed(symbol) > 0;
    }

    private int numSymbolUsed(Symbol symbol) {
        return fetchCount(symbolUsage, symbol);
    }

    public void symbolUsed(Symbol symbol) {
        incrementCount(symbolUsage, symbol);
    }

    private <K> void incrementCount(Map<K, Integer> m, K key) {
        m.put(key, fetchCount(m, key) + 1);
    }

    private <K> int fetchCount(Map<K, Integer> m, K key) {
        Integer integer = m.get(key);
        if (integer == null) {
            return 0;
        } else {
            return integer;
        }
    }
}
