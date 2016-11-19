package compiler;

import ast.AST;
import ast.BlockNode;
import ast.ExprNode;
import ast.StmtNode;
import entity.DefinedFunction;
import entity.DefinedVariable;
import exception.SemanticError;
import exception.SemanticException;
import type.TypeTable;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-11-19.
 */
public class DereferenceChecker extends Visitor {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public DereferenceChecker(TypeTable table, ErrorHandler errorHandler) {
        this.typeTable = table;
        this.errorHandler = errorHandler;
    }

    public void check(AST ast) throws SemanticException {
        for (DefinedVariable var : ast.definedVariables()) {
            checkToplevelVariable(var);
        }
        for (DefinedFunction fun : ast.definedFunctions()) {
            check(fun.getBody());
        }
        if (errorHandler.errorOccured()) {
            throw new SemanticException("compile failed");
        }
    }

    private void checkToplevelVariable(DefinedVariable var) {
        checkVariable(var);
        if (var.hasInitializer()) {
            checkConstant(var.getInitializer());
        }
    }

    private void checkVariable(DefinedVariable var) {
        if (var.hasInitializer()) {
            try {
                check(var.getInitializer());
            } catch (SemanticError error) {
            }
        }
    }

    private void checkConstant(ExprNode expr) {
        if (!expr.isConstant()) {
            errorHandler.error(expr.location(), "not a constant");
        }
    }

    private void check(StmtNode node) {
        node.accept(this);
    }

    private void check(ExprNode node) {
        node.accept(this);
    }

    @Override
    public Void visit(BlockNode node) {
        for (DefinedVariable var : node.getVariables()) {
            checkVariable(var);
        }
        for (StmtNode stmt : node.getStmts()) {
            try {
                check(stmt);
            } catch (SemanticError e) {

            }
        }
        return null;
    }
}
