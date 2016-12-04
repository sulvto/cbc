package compiler;

import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import exception.SemanticException;
import type.FunctionType;
import type.IntegerType;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public Void visit(BlockNode blockNode) {
        for (DefinedVariable var : blockNode.getVariables()) {
            checkVariable(var);
        }
        for (StmtNode n : blockNode.getStmts()) {
            check(n);
        }
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        check(node.getExpr());
        if (isInvalidStatementType(node.getExpr().getType())) {
            error(node.location(), "invalid statement type: " + node.getExpr().getType());
        }
        return null;
    }

    private boolean isInvalidStatementType(Type type) {
        return type.isStruct() || type.isUnion();
    }

    @Override
    public Void visit(IfNode node) {
        super.visit(node);
        checkCond(node.getCond());
        return null;
    }

    @Override
    public Void visit(WhileNode whileNode) {
        super.visit(whileNode);
        checkCond(whileNode.getCond());
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
            }
            node.setExpr(implicitCast(currentFunction.returnType(), node.getExpr()));
        }
        return null;
    }

    @Override
    public Void visit(AssignNode assignNode) {
        super.visit(assignNode);
        if (!checkLhs(assignNode.getLhs())) return null;
        if (!checkRhs(assignNode.getRhs())) return null;
        assignNode.setRhs(implicitCast(assignNode.getLhs().getType(), assignNode.getRhs()));
        return null;
    }

    private boolean checkRhs(ExprNode rhs) {
        if (isInvalidRHSType(rhs.getType())) {
            error(rhs, "invalid RHS expression type: " + rhs.getType());
            return false;
        }
        return true;
    }

    private boolean checkLhs(ExprNode lhs) {
        if (lhs.isParameter()) {
            return true;
        } else if (isInvalidLHSType(lhs.getType())) {
            error(lhs, "invalid LHS expression type: " + lhs.getType());
            return false;
        }
        return true;
    }

    @Override
    public Void visit(OpAssignNode opAssignNode) {
        super.visit(opAssignNode);
        if (!checkLhs(opAssignNode.getLhs())) return null;
        if (!checkRhs(opAssignNode.getRhs())) return null;
        if ("+".equals(opAssignNode.getOperator())
                || "-".equals(opAssignNode.getOperator())) {
            if (opAssignNode.getLhs().getType().isPointer()) {
                mustBeInteger(opAssignNode.getRhs(), opAssignNode.getOperator());
                opAssignNode.setRhs(integralPromotedExpr(opAssignNode.getRhs()));
                return null;
            }
        }
        if (!mustBeInteger(opAssignNode.getLhs(), opAssignNode.getOperator())) return null;
        if (!mustBeInteger(opAssignNode.getRhs(), opAssignNode.getOperator())) return null;
        Type l = integralPromotion(opAssignNode.getLhs().getType());
        Type r = integralPromotion(opAssignNode.getRhs().getType());
        Type opType = usualArithmeticConversion(l, r);
        if (!opType.isCompatible(l) && !isSafeIntegerCast(opAssignNode.getRhs(), opType)) {
            warn(opAssignNode, "incompatible implicit cast from " + opType + " to " + l);
        }
        if (!r.isSameType(opType)) {
            opAssignNode.setRhs(new CastNode(opType, opAssignNode.getRhs()));
        }
        return null;
    }

    private ExprNode integralPromotedExpr(ExprNode expr) {
        Type t = integralPromotion(expr.getType());
        if (t.isSameType(expr.getType())) {
            return expr;
        } else {
            return new CastNode(t, expr);
        }
    }

    @Override
    public Void visit(CondExprNode condExprNode) {
        super.visit(condExprNode);
        checkCond(condExprNode.getCond());
        Type t = condExprNode.getThenExpr().getType();
        Type e = condExprNode.getElseExpr().getType();
        if (t.isSameType(e)) {
            return null;
        } else if (t.isCompatible(e)) {
            condExprNode.setThenExpr(new CastNode(e, condExprNode.getThenExpr()));
        } else if (t.isCompatible(t)) {
            condExprNode.setElseExpr(new CastNode(e, condExprNode.getElseExpr()));
        } else {
            invalidCastError(condExprNode.getThenExpr(), e, t);
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

    @Override
    public Void visit(LogicalAndNode logicalAndNode) {
        super.visit(logicalAndNode);
        expectsComparableScalars(logicalAndNode);
        return null;
    }

    @Override
    public Void visit(LogicalOrNode logicalOrNode) {
        super.visit(logicalOrNode);
        expectsComparableScalars(logicalOrNode);
        return null;
    }

    @Override
    public Void visit(UnaryOpNode unaryOpNode) {
        super.visit(unaryOpNode);
        if ("!".equals(unaryOpNode.getOperator())) {
            mustBeScalar(unaryOpNode.getExpr(), unaryOpNode.getOperator());
        } else {
            mustBeInteger(unaryOpNode.getExpr(), unaryOpNode.getOperator());
        }
        return null;
    }

    @Override
    public Void visit(PrefixOpNode prefixOpNode) {
        super.visit(prefixOpNode);
        expectsScalarLHS(prefixOpNode);
        return null;
    }

    @Override
    public Void visit(SuffixOpNode suffixOpNode) {
        super.visit(suffixOpNode);
        expectsScalarLHS(suffixOpNode);
        return null;
    }

    private void expectsScalarLHS(UnaryArithmeticOpNode node) {
        if (node.getExpr().isParameter()) {
            // parameter is always a scalar
        } else if (node.getExpr().getType().isArray()) {
            // We connot modify non-parameter array
            wrongTypeError(node.getExpr(), node.getOperator());
            return;
        } else {
            mustBeScalar(node.getExpr(), node.getOperator());
        }
        if (node.getExpr().getType().isInteger()) {
            Type opType = integralPromotion(node.getExpr().getType());
            if (!node.getExpr().getType().isSameType(opType)) {
                node.setOpType(opType);
            }
            node.setAmount(1);
        } else if (node.getExpr().getType().isPointer()) {
            if (node.getExpr().getType().getBaseType().isVoid()) {
                // We cannot increment/decrement void*
                wrongTypeError(node.getExpr(), node.getOperator());
                return;
            }
            node.setAmount(node.getExpr().getType().getBaseType().size());
        } else {
            throw new Error("must not happen");
        }
    }

    @Override
    public Void visit(FuncallNode funcallNode) {
        super.visit(funcallNode);
        FunctionType type = funcallNode.getFunctionType();
        if (!type.acceptsArgc(funcallNode.numArgs())) {
            error(funcallNode, "wrong number of argments: " + funcallNode.numArgs());
            return null;
        }

        Iterator<ExprNode> args = funcallNode.getArgs().iterator();
        List<ExprNode> newArgs = new ArrayList<>();

        // mandatory args
        for (Type param : type.getParamTypes()) {
            ExprNode arg = args.next();
            newArgs.add(checkRhs(arg) ? implicitCast(param, arg) : arg);
        }
        // optional args
        while (args.hasNext()) {
            ExprNode arg = args.next();
            newArgs.add(checkRhs(arg) ? castOptionalArg(arg) : arg);
        }
        funcallNode.replaceArgs(newArgs);
        return null;
    }

    private ExprNode castOptionalArg(ExprNode arg) {
        if (!arg.getType().isInteger()) {
            return arg;
        }
        Type t = arg.getType().isSigned() ? typeTable.signedStackType() : typeTable.unsignedStackType();
        return arg.getType().size() < t.size() ? implicitCast(t, arg) : arg;
    }

    @Override
    public Void visit(ArefNode arefNode) {
        super.visit(arefNode);
        mustBeInteger(arefNode.getIndex(), "[]");
        return null;
    }

    @Override
    public Void visit(CastNode castNode) {
        super.visit(castNode);
        if (!castNode.getExpr().getType().isCastableTo(castNode.getType())) {
            invalidCastError(castNode, castNode.getExpr().getType(), castNode.getType());
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
            wrongTypeError(expr, operator);
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

    private Type integralPromotion(Type type) {
        if (!type.isInteger()) {
            throw new Error("integralPromotion for" + type);
        }
        Type intType = typeTable.signedInt();
        if (type.size() < intType.size()) {
            return intType;
        } else {
            return type;
        }
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

    private ExprNode implicitCast(Type type, ExprNode expr) {
        if (expr.getType().isSameType(type)) {
            return expr;
        } else if (expr.getType().isCastableTo(type)) {
            if (!expr.getType().isCompatible(type) && !isSafeIntegerCast(expr, type)) {
                warn(expr, "incompatible implicit cast from " + expr.getType() + " to " + type);
            }
            return new CastNode(type, expr);
        } else {
            invalidCastError(expr, expr.getType(), type);
            return expr;
        }

    }

    private boolean mustBeScalar(ExprNode expr, String op) {
        if (expr.getType().isScalar()) {
            return true;
        } else {
            wrongTypeError(expr, op);
            return false;
        }
    }

    private void invalidCastError(Node node, Type l, Type r) {
        error(node, "invalid cast from " + l + " to " + r);
    }

    private boolean isSafeIntegerCast(Node node, Type type) {
        if (!type.isInteger()) return false;
        IntegerType t = (IntegerType) type;
        if (!(node instanceof IntegerLiteralNode)) return false;
        IntegerLiteralNode n = (IntegerLiteralNode) node;
        return t.isInDomain(n.getValue());
    }

    private boolean isInvalidLHSType(Type type) {
        return type.isStruct() || type.isUnion() || type.isVoid() || type.isArray();
    }

    private boolean isInvalidRHSType(Type type) {
        return type.isStruct() || type.isUnion() || type.isVoid();
    }

    private void wrongTypeError(ExprNode expr, String op) {
        error(expr, "wrong operand type for " + op + ": " + expr.getType());
    }

    private void warn(Node n, String message) {
        errorHandler.warn(n.location(), message);
    }

    private void error(Node node, String message) {
        error(node.location(), message);
    }

    private void error(Location location, String message) {
        errorHandler.error(location, message);
    }
}
