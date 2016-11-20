package compiler;

import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import exception.SemanticException;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

/**
 * Created by sulvto on 16-11-20.
 */
public class TypeChecker extends Visitor {
    private TypeTable typeTable;
    private ErrorHandler errorHandler;

    public TypeChecker(TypeTable table, ErrorHandler errorHandler) {
        this.typeTable = table;
        this.errorHandler = errorHandler;
    }

    DefinedFunction currentFunction;

    public void check(AST ast) throws SemanticException {
        for (DefinedVariable var : ast.definedVariables()) {
            checkVariable(var);
        }
        for (DefinedFunction fun : ast.definedFunctions()) {
            currentFunction = fun;
            checkReturnType(fun);
            checkParamTypes(fun);
            check(fun.getBody());
        }
        if (errorHandler.errorOccured()) {
            throw new SemanticException("compile failed");
        }
    }

    private void checkVariable(DefinedVariable var) {
        if (isInvalidVariableType(var.getType())) {
            error(var.location(), "invalid variable type");
        } else if (var.hasInitializer()) {
            if (isInvalidLHSType(var.getType())) {
                error(var.location(), "invalid LHS type: " + var.getType());
            } else {
                check(var.getInitializer());
                var.setInitializer(implicitCast(var.getType(), var.getInitializer()));
            }
        }
    }

    private void checkParamTypes(DefinedFunction fun) {
        for (Parameter parameter : fun.parameters()) {
            if (isInvalidParameterType(parameter.getType())) {
                error(fun.location(), "invalid parameter  type: " + parameter.getType());
            }
        }
    }

    private void checkReturnType(DefinedFunction fun) {
        if (isInvalidReturnType(fun.returnType())) {
            error(fun.location(), "returns invalid type: " + fun.returnType());
        }
    }

    private boolean isInvalidReturnType(Type type) {
        return type.isUnion() || type.isStruct() || type.isArray();
    }

    private boolean isInvalidParameterType(Type type) {
        return type.isUnion() || type.isStruct() || type.isVoid() || type.isIncompleteArray();
    }

    private boolean isInvalidVariableType(Type type) {
        return type.isVoid() || (type.isArray() && !type.isAllocatedArray());
    }

    private void check(StmtNode node) {
        visitStmt(node);
    }

    private void check(ExprNode node) {
        visitExpr(node);
    }

    @Override
    public Void visit(ExprStmtNode node) {
        check(node.getExpr());
        if (isInvalidStatementType(node.getExpr().getType())) {
            error(node.location(), "invalid statement type: " + node.getExpr().getType());
        }
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        super.visit(node);
        checkCond(node.getCond());
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        super.visit(node);
        checkCond(node.getCond());
        return null;
    }

    private void checkCond(ExprNode cond) {
        mustBeScalar(cond, "condition expression");
    }

    @Override
    public Void visit(SwitchNode node) {
        super.visit(node);
        mustBeInteger(node.getCond(), "condition expression");
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        super.visit(node);

        if (currentFunction.isVoid()) {
            if (node.getExpr() != null) {
                error(node.location(), "returning value from void function");
            }
        } else {
            if (node.getExpr() == null) {
                error(node.location(), "missing return value");
            } else if (node.getExpr().getType().isVoid()) {
                error(node.location(), "returning void");
            } else {
                // TODO
//                node.setExpr()
            }
        }
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
        super.visit(node);
        if ("+".equals(node.getOperator()) ||
                "-".equals(node.getOperator())) {
            expectsSameIntegerOpPointerDiff(node);
        } else if ("*".equals(node.getOperator()) ||
                "/".equals(node.getOperator()) ||
                "%".equals(node.getOperator()) ||
                "&".equals(node.getOperator()) ||
                "|".equals(node.getOperator()) ||
                "^".equals(node.getOperator()) ||
                "<<".equals(node.getOperator()) ||
                ">>".equals(node.getOperator())) {
            expectsSameInteger(node);
        } else if ("==".equals(node.getOperator()) ||
                "!=".equals(node.getOperator()) ||
                "<".equals(node.getOperator()) ||
                "<=".equals(node.getOperator()) ||
                ">".equals(node.getOperator()) ||
                ">=".equals(node.getOperator())) {
            expectsComparableScalars(node);
        } else {
            throw new Error("unknown binary operator: " + node.getOperator());
        }
        return null;
    }

    private void expectsComparableScalars(BinaryOpNode node) {

    }

    private void expectsSameInteger(BinaryOpNode node) {
        if (!mustBeInteger(node.getLeft(), node.getOperator())) return;
        if (!mustBeInteger(node.getRight(), node.getOperator())) return;
        arithmeticImplicitCast(node);
    }

    private void expectsSameIntegerOpPointerDiff(BinaryOpNode node) {

    }

    private boolean mustBeInteger(ExprNode expr, String operator) {
        if (!expr.getType().isInteger()) {
            // wrongTypeError()
            return false;
        } else {
            return true;
        }
    }

    private void arithmeticImplicitCast(BinaryOpNode node) {
        Type l = integralPromotion(node.getLeft().getType());
        Type r = integralPromotion(node.getRight().getType());
        Type target = usualArithmeticConversion(l, r);
        if (!l.isSameType(target)) {
            node.setLeft(new CastNode(target, node.getLeft()));
        }
        if (!r.isSameType(target)) {
            node.setRight(new CastNode(target, node.getRight()));
        }
        node.setType(target);
    }

    private Type usualArithmeticConversion(Type l, Type r) {
        Type s_int = typeTable.signedInt();
        Type u_int = typeTable.unsignedInt();
        Type s_long = typeTable.signedLong();
        Type u_long = typeTable.unsignedLong();
        if ((l.isSameType(u_int) && r.isSameType(s_long))
                || (r.isSameType(u_int) && r.isSameType(s_long))) {
            return u_long;
        } else if (l.isSameType(u_long) || r.isSameType(u_long)) {
            return u_long;
        } else if (l.isSameType(s_long) || r.isSameType(s_long)) {
            return s_long;
        } else if (l.isSameType(u_int) || r.isSameType(u_int)) {
            return u_int;
        } else {
            return s_int;
        }
    }


    private void error(Location location, String message) {
        errorHandler.error(location, message);
    }
}
