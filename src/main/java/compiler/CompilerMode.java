package compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulvto on 16-11-22.
 */
enum CompilerMode {

    CheckSyntax("--check-syntax"),
    DumpTokens("--dump-tokens"),
    DumpAst("--dump-ast"),
    DumpStmt("--dump-stmt"),
    DumpExpr("--dump-expr"),
    DumpSemantic("--dump-semantic"),
    DumpReference("--dump-reference"),
    DumpIr("--dump-ir"),
    DumpAsm("--dump-asm"),
    PrintAsm("--print-asm"),
    Compile("-S"),
    Assemble("-c"),
    Link("--link");

    private static Map<String, CompilerMode> modes;

    static {
        modes = new HashMap<>();
        modes.put("--check-syntax", CheckSyntax);
        modes.put("--dump-tokens", DumpTokens);
        modes.put("--dump-ast", DumpAst);
        modes.put("--dump-stmt", DumpStmt);
        modes.put("--dump-expr", DumpExpr);
        modes.put("--dump-semantic", DumpSemantic);
        modes.put("--dump-reference", DumpReference);
        modes.put("--dump-ir", DumpIr);
        modes.put("--dump-asm", DumpAsm);
        modes.put("--print-asm", PrintAsm);
        modes.put("-S", Compile);
        modes.put("-c", Assemble);
    }

    public static boolean isModeOption(String opt) {
        return modes.containsKey(opt);
    }

    public static CompilerMode fromOption(String opt) {
        CompilerMode mode = modes.get(opt);
        if (mode == null) {
            throw new Error("mist not happen: unknown mode option: " + opt);
        }
        return mode;
    }

    private final String option;

    CompilerMode(String option) {
        this.option = option;
    }

    public String toOption() {
        return option;
    }

    public boolean requires(CompilerMode mode) {
        return ordinal() >= mode.ordinal();
    }
}
