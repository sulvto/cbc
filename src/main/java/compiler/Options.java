package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

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
    private List<LdArg> ldArgs;
    private List<SourceFile> sourceFiles;
    private LibraryLoader loader = new LibraryLoader();
    private boolean debugParser = false;

    private void parseArgs(String[] args) {
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
                } //TODO
                else {

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
                parseError("unknown file type: "+src.path());
            }
        }

        // TODO
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
        // OptionParseError
        throw new Error(message);
    }

    public CompilerMode getMode() {
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
}
