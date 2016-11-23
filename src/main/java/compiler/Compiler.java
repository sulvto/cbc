package compiler;

import ast.AST;
import exception.CompileException;
import exception.FileException;
import exception.SyntaxException;
import utils.ErrorHandler;

import java.io.File;
import java.util.List;

/**
 * Created by sulvto on 16-11-22.
 */
public class Compiler {
    public static void main(String[] args) {
        new Compiler().commandMain(args);
    }

    private final ErrorHandler errorHandler;

    public Compiler() {
        this.errorHandler = new ErrorHandler("cbc");

   }

    public void commandMain(String[] args) {
        Options opts = parseOptions(args);
        if (CompilerMode.CheckSyntax == opts.getMode()) {
            System.exit(checkSyntax(opts) ? 0 : 1);
        }
        try {
            List<SourceFile> srcs = opts.getSourceFiles();
            build(srcs, opts);
            System.exit(0);
        } catch (CompileException ex) {
            errorHandler.error(ex.getMessage());
            System.exit(1);
        }
    }

    private void build(List<SourceFile> srcs, Options opts) {
        for (SourceFile src : srcs) {
            // TODO
        }
    }

    private boolean checkSyntax(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.getSourceFiles()) {
            if (isValidSyntax(src.path(), opts)) {
                System.out.println(src.path() + ": Syntax OK");
            } else {
                System.out.println(src.path()+": Syntax Error");
                failed = true;
            }
        }
        return failed;
    }

    private boolean isValidSyntax(String path, Options opts) {
        try {
            parseFile(path, opts);
            return true;
        } catch (SyntaxException ex) {
            return false;
        } catch (FileException ex) {
            errorHandler.error(ex.getMessage());
            return false;
        };
    }

    private AST parseFile(String path, Options opts) {
        return Parser.parseFile(new File(path),opts.getLoader(),errorHandler,opts.doesDebugParser());
    }

    private Options parseOptions(String[] args) {
        return Options.parse(args);
    }
}
