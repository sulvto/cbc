package compiler;

import asm.AssemblyCode;
import ast.AST;
import ast.ExprNode;
import ast.StmtNode;
import exception.CompileException;
import exception.FileException;
import exception.SemanticException;
import exception.SyntaxException;
import ir.IR;
import parser.Parser;
import type.TypeTable;
import utils.ErrorHandler;

import java.io.*;
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
        if (CompilerMode.CheckSyntax == opts.mode()) {
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

    private void build(List<SourceFile> srcs, Options opts) throws CompileException {
        for (SourceFile src : srcs) {
            // TODO
            if (src.isCflatSource()) {
                String destPath = opts.asmFileNameOf(src);
                compile(src.path(), destPath, opts);
                src.setCurrentName(destPath);
            }
            if (!opts.isAssembleRequired()) continue;
            if (src.isAssemblySource()) {
                String destPath = opts.objFileNameOf(src);
                assemble(src.path(), destPath, opts);
                src.setCurrentName(destPath);
            }
        }
        if (!opts.isLinkRequired()) return;
        link(opts);
    }

    private void compile(String srcPath, String destPath, Options opts) throws SemanticException, SyntaxException, FileException {
        AST ast = parseFile(srcPath, opts);
        if (dumpAST(ast, opts.mode())) return;
        TypeTable types = opts.typeTable();
        AST sem = semanticAnalyze(ast, types, opts);
        if (dumpSemant(ast, opts.mode())) return;
        IR ir = new IRGenerator(types, errorHandler).generate(sem);
        if (dumpIR(ir, opts.mode())) return;
        AssemblyCode asm = generateAssembly(ir, opts);
        if (dumpAsm(asm, opts.mode())) return;
        if (printAsm(asm, opts.mode())) return;
        writeFile(destPath, asm.toSource());
    }

    private AssemblyCode generateAssembly(IR ir, Options opts) {
        return opts.codeGenerator(errorHandler).generate(ir);
    }

    private AST semanticAnalyze(AST ast, TypeTable types, Options opts)throws SemanticException {
        new LocalResolver(errorHandler).resolve(ast);
        new TypeResolver(types, errorHandler).resolve(ast);
        types.semanticCheck(errorHandler);
        if (opts.mode() == CompilerMode.DumpReference) {
            ast.dump();
            return ast;
        }
        new DereferenceChecker(types, errorHandler).check(ast);
        new TypeChecker(types,errorHandler).check(ast);
        return ast;
    }

    private ExprNode findExpr(AST ast) {
        ExprNode expr = ast.getSingleMainExpr();
        if (expr == null) {
            errorExit("source file does not contains main()");
        }
        return expr;
    }



    private StmtNode findStmt(AST ast) {
        StmtNode stmt = ast.getSingleMainStmt();
        if (stmt == null) {
            errorExit("source file does not contains main()");
        }
        return stmt;
    }

    private void link(Options opts) {
        // TODO
    }

    private void assemble(String srcPath, String destPath, Options opts) {
        // TODO
//        opts.assembler(errorHandler).assemble(srcPath, destPath, opts.asOptions());
    }

    private boolean checkSyntax(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.getSourceFiles()) {
            if (isValidSyntax(src.path(), opts)) {
                System.out.println(src.path() + ": Syntax OK");
            } else {
                System.out.println(src.path() + ": Syntax Error");
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
        }

    }

    private AST parseFile(String path, Options opts) throws SyntaxException, FileException {
        return Parser.parseFile(new File(path), opts.getLoader(), errorHandler, opts.doesDebugParser());
    }

    private Options parseOptions(String[] args) {
        return Options.parse(args);
    }

    private void errorExit(String message) {
        errorHandler.error(message);
        System.exit(1);
    }

    private void writeFile(String destPath, String str) throws FileException {
        if ("-".equals(destPath)) {
            System.out.print(str);
            return;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destPath)))){

            bufferedWriter.write(str);
        } catch (FileNotFoundException e) {
            errorHandler.error("file not found: "+destPath);
            throw new FileException("file error");
        } catch (IOException e) {
            errorHandler.error("io error: "+e.getMessage());
            throw new FileException("file error");
        }
    }

    private boolean printAsm(AssemblyCode asm, CompilerMode mode) {
        if (CompilerMode.PrintAsm == mode) {
            System.out.print(asm.toSource());
            return true;
        }
        return false;

    }

    private boolean dumpAsm(AssemblyCode asm, CompilerMode mode) {
        if (CompilerMode.DumpAsm == mode) {
            asm.dump(System.out);
            return true;
        }
        return false;
    }

    private boolean dumpIR(IR ir, CompilerMode mode) {
        if (CompilerMode.DumpIr == mode) {
            ir.dump();
            return true;
        }
        return false;
    }

    private boolean dumpSemant(AST ast, CompilerMode mode) {
        switch (mode) {
            case DumpReference:
                return true;
            case DumpSemantic:
                ast.dump();
                return true;
            default:
                return false;
        }
    }

    private boolean dumpAST(AST ast, CompilerMode mode) {
        switch (mode) {
            case DumpTokens:
                ast.dumpTokens(System.out);
                return true;
            case DumpAst:
                ast.dump();
                return true;
            case DumpStmt:
                findStmt(ast).dump();
                return true;
            case DumpExpr:
                findExpr(ast).dump();
                return true;
            default:
                return false;
        }
    }
}
