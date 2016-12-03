package compiler;

import asm.Label;
import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import entity.LocalScope;
import exception.JumpError;
import exception.SemanticException;
import ir.*;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sulvto on 16-11-20.
 */
public class IRGenerator implements ASTVisitor<Void, Expr> {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public IRGenerator(TypeTable table, ErrorHandler errorHandler) {
        this.typeTable = table;
        this.errorHandler = errorHandler;
    }

    public IR generate(AST ast) throws SemanticException {
        for (DefinedVariable var : ast.definedVariables()) {
            if (var.hasInitializer()) {
                var.setIr(transformExpr(var.getInitializer()));
            }
        }
        for (DefinedFunction fun : ast.definedFunctions()) {
            fun.setIr(compileFunctionBody(fun));
        }
        if (errorHandler.errorOccured()) {
            throw new SemanticException("IR generation failed.");
        }
        return ast.ir();
    }

    List<Stmt> stmts;
    LinkedList<LocalScope> scopeStack;
    LinkedList<Label> breakStack;
    LinkedList<Label> continueStack;
//    Map<String, JumpEntry> jumpMap;

    private List<Stmt> compileFunctionBody(DefinedFunction fun) {
        stmts = new ArrayList<>();
        scopeStack = new LinkedList<>();
        breakStack = new LinkedList<>();
        continueStack = new LinkedList<>();
//        jumpMap = new HashMap<>();
        transformStmt(fun.getBody());
//        checkJumpLinks(jumpMap);
        return stmts;
    }

    private void transformStmt(StmtNode node) {
        node.accept(this);
    }

    private void transformStmt(ExprNode node) {
        node.accept(this);
    }

    private int exprNestLevel = 0;

    private Expr transformExpr(ExprNode node) {
        exprNestLevel++;
        Expr expr = node.accept(this);
        exprNestLevel--;
        return expr;
    }

    public boolean isStatement() {
        return exprNestLevel == 0;
    }

    private void jump(Label label) {
        jump(null, label);
    }

    private void jump(Location location, Label label) {
        stmts.add(new Jump(location, label));
    }

    private void label(Label label) {
        stmts.add(new LabelStmt(null, label));
    }

    private void cjump(Location location, Expr cond, Label thenLabel, Label endLabel) {
        stmts.add(new CJump(location, cond, thenLabel, endLabel));
    }

    private void popBreak() {
        if (breakStack.isEmpty()) {
            throw new JumpError("unmatched push/pop for break stack");
        }
        breakStack.removeLast();
    }

    private void pushBreak(Label label) {
        breakStack.add(label);
    }

    private Label currentBreakTarget() {
        if (breakStack.isEmpty()) {
            throw new JumpError("break from out of loop");
        }
        return breakStack.getLast();
    }

    private void popContinue() {
        if (continueStack.isEmpty()) {
            throw new JumpError("unmatched push/pop for continue stack");
        }
    }


    private void pushContinue(Label label) {
        continueStack.add(label);
    }

    private Label currentContinueTarget() {
        if (continueStack.isEmpty()) {
            throw new JumpError("continue from out of loop");
        }
        return continueStack.getLast();
    }

    @Override
    public Void visit(BlockNode node) {
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        Label thenLabel = new Label();
        Label elseLabel = new Label();
        Label endLabel = new Label();
        Expr cond = transformExpr(node.getCond());
        if (node.getElseBody() == null) {
            cjump(node.location(), cond, thenLabel, endLabel);
            label(thenLabel);
            transformStmt(node.getThenBody());
            label(endLabel);
        } else {
            cjump(node.location(), cond, thenLabel, elseLabel);
            label(thenLabel);
            transformStmt(node.getThenBody());
            jump(endLabel);
            label(elseLabel);
            transformStmt(node.getElseBody());
            label(endLabel);
        }
        return null;
    }

    @Override
    public Void visit(SwitchNode node) {
        return null;
    }

    @Override
    public Void visit(CaseNode node) {
        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        Label begLabel = new Label();
        Label bodyLabel = new Label();
        Label endLabel = new Label();

        label(begLabel);
        cjump(node.location(), transformExpr(node.getCond()), bodyLabel, endLabel);
        label(bodyLabel);
        pushContinue(begLabel);
        pushBreak(endLabel);
        transformStmt(node.getBody());
        popBreak();
        popContinue();
        jump(begLabel);
        label(endLabel);
        return null;
    }

    @Override
    public Void visit(DoWhileNode node) {
        Label begLabel = new Label();
        Label contLabel = new Label();
        Label endLabel = new Label();

        label(begLabel);
        pushContinue(contLabel);
        pushBreak(endLabel);
        transformStmt(node.getBody());
        popBreak();
        popContinue();
        label(contLabel);
        cjump(node.location(), transformExpr(node.getCond()), begLabel, endLabel);
        label(endLabel);
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        Label begLabel = new Label();
        Label bodyLabel = new Label();
        Label contLabel = new Label();
        Label endLabel = new Label();

        transformStmt(node.getInit());

        label(begLabel);
        cjump(node.location(), transformExpr(node.getCond()), bodyLabel, endLabel);
        label(bodyLabel);
        pushContinue(contLabel);
        pushBreak(endLabel);
        transformStmt(node.getBody());
        popBreak();
        popContinue();
        label(contLabel);
        transformStmt(node.getIncr());
        jump(begLabel);
        label(endLabel);

        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        try {
            jump(node.location(), currentBreakTarget());
        } catch (JumpError error) {
//            error(node, error.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        try {
            jump(node.location(), currentContinueTarget());
        } catch (JumpError error) {
//            error(node, error.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(GotoNode node) {
        return null;
    }

    @Override
    public Void visit(LabelNode node) {
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        return null;
    }

    @Override
    public Expr visit(AssignNode node) {
        return null;
    }

    @Override
    public Expr visit(OpAssignNode node) {
        return null;
    }

    @Override
    public Expr visit(CondExprNode node) {
        return null;
    }

    @Override
    public Expr visit(LogicalOrNode node) {
        return null;
    }

    @Override
    public Expr visit(LogicalAndNode node) {
        return null;
    }

    @Override
    public Expr visit(BinaryOpNode node) {
        Expr right = transformExpr(node.getRight());
        Expr left = transformExpr(node.getLeft());
        Op op = Op.internBinary(node.getOperator(), node.getType().isSigned());
        Type t = node.getType();
        Type r = node.getRight().getType();
        Type l = node.getLeft().getType();
        if (isPointerDiff(op, r, l)) {
            // ptr - ptr -> (ptr - ptr) / ptrBaseSize
            Expr tmp = new Bin(asmType(t), op, left, right);
            return new Bin(asmType(t), Op.S_DIV, tmp, ptrBaseSize(l));
        } else if (isPointerArithmetic(op, l)) {
            // ptr + int -> ptr + (int * ptrBaseSize)
            return new Bin(asmType(t), op, left, new Bin(asmType(t), Op.MUL, right, ptrBaseSize(l)));
        } else if (isPointerArithmetic(op, r)) {
            // int + ptr -> (int * ptrBaseSize) + ptr
            return new Bin(asmType(t), op, new Bin(asmType(t), Op.MUL, left, ptrBaseSize(r)), right);
        } else {
            // int + int
            return new Bin(asmType(t), op, left, right);
        }
    }

    private Expr ptrBaseSize(Type t) {
        return new Int(ptrdiff_t(), t.getBaseType().size());
    }

    private Type ptrdiff_t() {
        return null;
//        return Type.get(typeTable.getLongSize());
    }

    private boolean isPointerArithmetic(Op op, Type operandType) {
        switch (op) {
            case ADD:
            case SUB:
                return operandType.isPointer();
            default:
                return false;
        }
    }

    private boolean isPointerDiff(Op op, Type r, Type l) {
        return op == Op.SUB && l.isPointer() && l.isPointer();
    }

    @Override
    public Expr visit(UnaryOpNode node) {
        if ("+".equals(node.getOperator())) {
            return transformExpr(node.getExpr());
        }
        return new Uni(asmType(node.getType()), Op.internUnary(node.getOperator()), transformExpr(node.getExpr()));
    }

    private Type asmType(Type type) {
//        if (type.isVoid()) return int_t();
//        return Type.get(type.size());
        return null;
    }


    @Override
    public Expr visit(PrefixOpNode node) {
        return null;
    }

    @Override
    public Expr visit(SuffixOpNode node) {
        return null;
    }

    @Override
    public Expr visit(ArefNode node) {
        return null;
    }

    @Override
    public Expr visit(MemberNode node) {
        return null;
    }

    @Override
    public Expr visit(PtrMemberNode node) {
        return null;
    }

    @Override
    public Expr visit(FuncallNode node) {
        return null;
    }

    @Override
    public Expr visit(DereferenceNode node) {
        return null;
    }

    @Override
    public Expr visit(AddressNode node) {
        return null;
    }

    @Override
    public Expr visit(CastNode node) {
        return null;
    }

    @Override
    public Expr visit(SizeofExprNode node) {
        return null;
    }

    @Override
    public Expr visit(SizeofTypeNode node) {
        return null;
    }

    @Override
    public Expr visit(VariableNode node) {
        return null;
    }

    @Override
    public Expr visit(IntegerLiteralNode node) {
        return null;
    }

    @Override
    public Expr visit(StringLiteralNode node) {
        return null;
    }


}
