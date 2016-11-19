package compiler;

import ast.AST;
import ast.BlockNode;
import ast.VariableNode;
import entity.*;
import exception.SemanticException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sulvto on 16-11-18.
 */
public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopesStack;
    private final ConstantTable constantTable;
    private final ErrorHandler errorHandler;

    public LocalResolver(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.constantTable = new ConstantTable();
        this.scopesStack = new LinkedList<>();
    }

    public void resolve(AST ast) {
        ToplevelScope toplevel = new ToplevelScope();
        scopesStack.add(toplevel );
        for (Entity decl: ast.declarations()) {
            toplevel.declareEntity(decl);
        }
        for (Entity ent: ast.definitions()) {
            toplevel.defineEntity(ent);
        }

        resolveGvarInitializers(ast.definedVariables());
        resolveConstantValues(ast.getConstants());
        resolveFunctions(ast.definedFunctions());


    }

    private void resolveFunctions(List<DefinedFunction> definedFunctions) {
        for (DefinedFunction function : definedFunctions) {
            pushScope(function.parameters());
            resolve(function.body());
            function.setScope(popScope());
        }

    }

    private LocalScope popScope() {
        return (LocalScope)scopesStack.removeLast();
    }

    private void pushScope(List<? extends DefinedVariable> definedVariables) {
        LocalScope scope = new LocalScope(currentScope());
        for (DefinedVariable variable : definedVariables) {
            if (scope.isDefinedLocally(variable.getName())) {
                error(variable.location(), "duplicated variable in scope" + variable.getName());
            }else{
                scope.defineVariable(variable);
            }
        }
        scopesStack.addLast(scope);
    }

    private Scope currentScope() {
        return scopesStack.getLast();
    }

    @Override
    public Void visit(BlockNode node) {
        pushScope(node.getVariables());
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        try {
            Entity entity = currentScope().getEntity(node.getName());
            entity.refered();
            node.setEntity(entity);
        } catch (SemanticException e) {
            error(node, e.getMessage());
        }
        return null;
    }
}
