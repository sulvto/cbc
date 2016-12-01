package compiler;

import ast.*;
import entity.*;
import type.CompositeType;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public class TypeResolver extends Visitor implements EntityVisitor<Void>, DeclarationVisitor<Void> {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public TypeResolver(TypeTable typeTable, ErrorHandler errorHandler) {
        this.typeTable = typeTable;
        this.errorHandler = errorHandler;
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

            } else {
                typeTable.put(typeDefinition.getTypeRef(), typeDefinition.definingType());
            }
        }
    }

    @Override
    public Void visit(StructNode structNode) {
        resolveCompositeType(structNode);
        return null;
    }

    @Override
    public Void visit(UnionNode unionNode) {
        resolveCompositeType(unionNode);
        return null;
    }


    public void resolveCompositeType(CompositeTypeDefinition compositeTypeDefinition) {
        CompositeType type = (CompositeType) typeTable.get(compositeTypeDefinition.getTypeNode().getTypeRef());
        if (type == null) {
            throw new Error("cannot intern struct/union: " + compositeTypeDefinition.getName());
        }
        for (Slot s : type.getMembers()) {
            bindType(s.getTypeNode());
        }
    }

    @Override
    public Void visit(TypedefNode typedefNode) {
        bindType(typedefNode.getTypeNode());
        bindType(typedefNode.realTypeNode());
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
        if (typeNode.isResolved()) return;
        typeNode.setType(typeTable.get(typeNode.getTypeRef()));
    }

    @Override
    public Void visit(UndefinedVariable variable) {
        bindType(variable.getTypeNode());
        return null;
    }

    @Override
    public Void visit(Constant constant) {
        bindType(constant.getTypeNode());
        visitExpr(constant.getValue());
        return null;
    }

    @Override
    public Void visit(DefinedFunction definedFunction) {
        resolveFunctionHeader(definedFunction);
        visitStmt(definedFunction.getBody());
        return null;
    }

    private void resolveFunctionHeader(Function fun) {
        bindType(fun.getTypeNode());
        for (Parameter parameter : fun.parameters()) {
            Type t = typeTable.getParamType(parameter.getTypeNode().getTypeRef());
            parameter.getTypeNode().setType(t);
        }
    }

    @Override
    public Void visit(UndefinedFunction function) {
        resolveFunctionHeader(function);
        return null;
    }

    @Override
    public Void visit(BlockNode blockNode) {
        for (DefinedVariable var : blockNode.getVariables()) {
            var.accept(this);
        }
        visitStmts(blockNode.getStmts());
        return null;
    }

    @Override
    public Void visit(CastNode castNode) {
        bindType(castNode.getTypeNode());
        super.visit(castNode);
        return null;
    }

    @Override
    public Void visit(SizeofExprNode sizeofExprNode) {
        bindType(sizeofExprNode.getTypeNode());
        super.visit(sizeofExprNode);
        return null;
    }

    @Override
    public Void visit(SizeofTypeNode sizeofTypeNode) {
        bindType(sizeofTypeNode.getOperandTypeNode());
        bindType(sizeofTypeNode.getTypeNode());
        super.visit(sizeofTypeNode);
        return null;
    }

    @Override
    public Void visit(IntegerLiteralNode integerLiteralNode) {
        bindType(integerLiteralNode.getTypeNode());
        return null;
    }

    @Override
    public Void visit(StringLiteralNode stringLiteralNode) {
        bindType(stringLiteralNode.getTypeNode());
        return null;
    }

    private void error(Node node, String message) {
        errorHandler.error(node.location(), message);
    }
}
