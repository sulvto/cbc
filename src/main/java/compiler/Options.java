package compiler;

import exception.OptionParseError;
import parser.LibraryLoader;
import sysdep.*;
import sysdep.x86.CodeGenerator;
import type.TypeTable;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Created by sulvto on 16-11-22.
 */
public class Options {
    static Options parse(String[] args) {
        Options options = new Options();
        options.parseArgs(args);
        return options;
    }

    private CompilerMode mode;

    private String outputFileName;
    private boolean verbose = false;
    private Platform platform = new X86Linux();
    private CodeGeneratorOptions genOptions = new CodeGeneratorOptions();
    private AssemblerOptions asOptions = new AssemblerOptions();
    private LinkerOptions ldOptions = new LinkerOptions();
    private List<LdArg> ldArgs;
    private List<SourceFile> sourceFiles;
    private LibraryLoader loader = new LibraryLoader();
    private boolean debugParser = false;

    private void parseArgs(String[] args) {
        sourceFiles = new ArrayList<>();
        ldArgs = new ArrayList<>();
        ListIterator<String> argIterator = Arrays.asList(args).listIterator();
        while (argIterator.hasNext()) {
            String arg = argIterator.next();
            if ("--".equals(arg)) {
                break;
            } else if (arg.startsWith("-")) {
                if (CompilerMode.isModeOption(arg)) {
                    if (mode != null) {
                        parseError(mode.toOption() + " option and " + arg + " option is exclusive");
                    }
                    mode = CompilerMode.fromOption(arg);
                } else if (arg.startsWith("--debug-parser")) {
                    debugParser = true;
                } else if (arg.startsWith("-o")) {
                    outputFileName = getOptArg(arg, argIterator);
                } else if ("-fpic".equalsIgnoreCase(arg)) {
                    genOptions.generatePIC();
                } else if ("-fpie".equalsIgnoreCase(arg)) {
                    genOptions.generatePIE();
                } else if ("-0".equals(arg)) {
                    String type = arg.substring(2);
                    if (!type.matches("^([0123s]|)$")) {
                        parseError("unknown optimization switch: " + arg);
                    }
                    genOptions.setOptimizationLevel(type.equals("0") ? 0 : 1);
                } else if ("-fverbose-asm".equals(arg) || "verbose-asm".equals(arg)) {
                    genOptions.generateVerboseAsm();
                } else if ("-Wa,".equals(arg)) {
                    for (String s : parseCommaSeparatedOptions(arg)) {
                        asOptions.addArg(s);
                    }
                } else if ("-Xassembler".equals(arg)) {
                    asOptions.addArg(nextArg(arg, argIterator));
                } else if ("-static".equals(arg)) {
                    addLdArg(arg);
                } else if ("-shared".equals(arg)) {
                    ldOptions.generatingSharedLibrary = true;
                } else if ("-pie".equals(arg)) {
                    ldOptions.generatingPIE = true;
                } else if ("--readonly-got".equals(arg)) {
                    addLdArg("-z");
                    addLdArg("combreloc");
                    addLdArg("-z");
                    addLdArg("now");
                    addLdArg("-z");
                    addLdArg("relro");
                } else if ("-l".equalsIgnoreCase(arg)) {
                    addLdArg(arg + getOptArg(arg, argIterator));
                } else if ("-nostartfiles".equals(arg)) {
                    ldOptions.noStartFiles = true;
                } else if ("-nodefaultlibs".equals(arg)) {
                    ldOptions.noDefaultLibs = true;
                } else if ("-nostdlib".equals(arg)) {
                    ldOptions.noStartFiles = true;
                } else if ("-Wl,".equals(arg)) {
                    for (String opt : parseCommaSeparatedOptions(arg)) {
                        addLdArg(opt);
                    }
                } else if ("-Xlinker".equals(arg)) {
                    addLdArg(nextArg(arg, argIterator));
                } else if ("-v".equals(arg)) {
                    verbose = true;
                    asOptions.verbose = true;
                    ldOptions.verbose = true;
                } else {
                    parseError("unknown option: " + arg);
                }
            } else {
                ldArgs.add(new SourceFile(arg));
            }
        }

        while (argIterator.hasNext()) {
            ldArgs.add(new SourceFile(argIterator.next()));
        }

        if (mode == null) {
            mode = CompilerMode.Link;
        }

        sourceFiles = selectSourceFiles(ldArgs);
        if (sourceFiles.isEmpty()) {
            parseError("no input file");
        }

        for (SourceFile src : sourceFiles) {
            if (!src.isKnownFileType()) {
                parseError("unknown file type: " + src.path());
            }
        }

        if (outputFileName != null && sourceFiles.size() > 1 && !isLinkRequired()) {
            parseError("-o option only 1 input (except linking)");
        }
    }

    private List<String> parseCommaSeparatedOptions(String opt) {
        String[] opts = opt.split(",");
        if (opts.length <= 1) {
            parseError("missing argument for " + opt);
        }

        List<String> result = new ArrayList<>();
        for (int i = 1; i < opts.length; i++) {
            result.add(opts[i]);
        }
        return result;
    }

    private void addLdArg(String arg) {
        ldArgs.add(new LdOption(arg));
    }

    private String getOptArg(String opt, ListIterator<String> args) {
        String path = opt.substring(2);
        if (path.length() != 0) {
            return path;
        } else {
            return nextArg(opt, args);
        }
    }

    private String nextArg(String opt, ListIterator<String> args) {
        if (!args.hasNext()) {
            parseError("missing argument for " + opt);
        }
        return args.next();
    }

    private List<SourceFile> selectSourceFiles(List<LdArg> ldArgs) {
        List<SourceFile> result = new ArrayList<>();
        for (LdArg ldArg : ldArgs) {
            if (ldArg.isSourceFile()) {
                result.add((SourceFile) ldArg);
            }
        }
        return result;
    }

    private void parseError(String message) {
        throw new OptionParseError(message);
    }

    public CompilerMode mode() {
        return mode;
    }

    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    boolean doesDebugParser() {
        return this.debugParser;
    }

    public LibraryLoader getLoader() {
        return loader;
    }

    public boolean isAssembleRequired() {
        return mode.requires(CompilerMode.Assemble);
    }

    public boolean isLinkRequired() {
        return mode.requires(CompilerMode.Link);
    }

    public String asmFileNameOf(SourceFile src) {
        if (outputFileName != null && mode == CompilerMode.Compile) {
            return outputFileName;
        } else {
            return src.asmFileName();
        }
    }

    public String objFileNameOf(SourceFile src) {
        if (outputFileName != null && mode == CompilerMode.Assemble) {
            return outputFileName;
        } else {
            return src.objFileName();
        }

    }

    public TypeTable typeTable() {
        return platform.typeTable();
    }

    public CodeGenerator codeGenerator(ErrorHandler errorHandler) {
        return platform.codeGenerator(genOptions, errorHandler);
    }

    public Assembler assembler(ErrorHandler errorHandler) {
        return platform.assembler(errorHandler);
    }

    AssemblerOptions asOptions() {
        return asOptions;
    }

    public boolean isGeneratingSharedLibrary() {
        return ldOptions.generatingSharedLibrary;
    }

    public Linker linker(ErrorHandler errorHandler) {
        return platform.linker(errorHandler);
    }

    public LinkerOptions ldOptions() {
        return ldOptions;
    }

    List<String> ldArgs() {
        return ldArgs.stream().map(LdArg::toString).collect(Collectors.toList());
    }

    public String soFileName() {
        return linkedFileName(".so");
    }

    public String exeFileName() {
        return linkedFileName("");
    }

    private static final String DEFAULT_LINKER_OUTPUT = "a.out";

    private String linkedFileName(String newExt) {
        if (outputFileName != null) {
            return outputFileName;
        } else if (sourceFiles.size() == 1) {
            return sourceFiles.get(0).linkedFileName(newExt);
        } else {
            return DEFAULT_LINKER_OUTPUT;
        }
    }
}
