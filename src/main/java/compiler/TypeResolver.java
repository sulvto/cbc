package compiler;

import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import entity.Entity;
import entity.EntityVisitor;
import type.Type;
import type.TypeTable;

import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public class TypeResolver extends Visitor implements EntityVisitor<Void>, DeclarationVisitor<Void>{
    private final TypeTable typeTable;
    private final Errorhandler errorhandler;

    public TypeResolver(TypeTable typeTable, Errorhandler errorhandler) {
        this.typeTable = typeTable;
        this.errorhandler = errorhandler;
    }

    public void resolve(AST ast) {
        defineTypes(ast.types());
        for (TypeDefinition t : ast.types()) {
            t.accept(this);
        }

        for (Entity entity : ast.entities()) {
            entity.accept(this);
        }


    }

    void defineTypes(List<TypeDefinition> typeDefinitionList) {
        for (TypeDefinition typeDefinition : typeDefinitionList) {
            if (typeTable.isDefined(typeDefinition.getTypeRef())) {

            }else{
                typeTable.put(typeDefinition.getTypeRef(),typeDefinition.definingType());
            }
        }
    }

    @Override
    public Void visit(StructNode structNode) {
        return null;
    }

    @Override
    public Void visit(TypedefNode typedefNode) {
        return null;
    }

    @Override
    public Void visit(UnionNode unionNode) {
        return null;
    }

    @Override
    public Void visit(DefinedVariable variable) {
        bindType(variable.getTypeNode());
        if (variable.hasInitializer()) {
            visitExpr(variable.getInitializer());
        }
        return null;
    }

    private void bindType(TypeNode typeNode) {
        if(typeNode.isResolved()) return;
        typeNode.setType(typeTable.get(typeNode.getTypeRef()));
    }

    @Override
    public Void visit(UndefinedVariable variable) {
        return null;
    }

    @Override
    public Void visit(DefinedFunction definedFunction) {
        resolvFunctionHeader(definedFunction);
        visitStmt(definedFunction.getBody());
        return null;
    }

    private void resolvFunctionHeader(DefinedFunction definedFunction) {
        bindType(definedFunction.getTypeNode());
        for (Parameter parameter : definedFunction.parameters()) {
            Type t = typeTable.getParamType(parameter.getTypeNode().getTypeRef());
            parameter.getTypeNode().setType(t);
        }
    }

    @Override
    public Void visit(UndefinedFunction variable) {
        return null;
    }

    @Override
    public Void visit(Constant constant) {
        return null;
    }
}
