package compiler;

import asm.Label;
import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import entity.Entity;
import entity.LocalScope;
import exception.JumpError;
import exception.SemanticException;
import ir.*;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;
import utils.ListUtils;

import java.util.*;

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
    Map<String, JumpEntry> jumpMap;

    class JumpEntry {
        public Label label;
        public long numRefered;
        public boolean isDefined;
        public Location location;

        public JumpEntry(Label label) {
            this.label = label;
            numRefered = 0;
            isDefined = false;
        }
    }

    private Label referLabel(String name) {
        JumpEntry ent = getJumpEntry(name);
        ent.numRefered++;
        return ent.label;
    }

    private JumpEntry getJumpEntry(String name) {
        JumpEntry jumpEntry = jumpMap.get(name);
        if (jumpEntry == null) {
            jumpEntry = new JumpEntry(new Label());
            jumpMap.put(name, jumpEntry);
        }
        return jumpEntry;
    }


    private Label defineLabel(String name, Location location) throws SemanticException {
        JumpEntry jumpEntry = getJumpEntry(name);
        if (jumpEntry.isDefined) {
            throw new SemanticException("duplicated jump labels in " + name + "():" + name);
        }
        jumpEntry.isDefined = true;
        jumpEntry.location = location;
        return jumpEntry.label;
    }


    private List<Stmt> compileFunctionBody(DefinedFunction fun) {
        stmts = new ArrayList<>();
        scopeStack = new LinkedList<>();
        breakStack = new LinkedList<>();
        continueStack = new LinkedList<>();
        jumpMap = new HashMap<>();
        transformStmt(fun.getBody());
        checkJumpLinks(jumpMap);
        return stmts;
    }

    private void checkJumpLinks(Map<String, JumpEntry> jumpMap) {
        jumpMap.entrySet().forEach(ent -> {
            String labelName = ent.getKey();
            JumpEntry jump = ent.getValue();
            if (!jump.isDefined) {
                errorHandler.error(jump.location, "undefined labelL: " + labelName);
            }
            if (jump.numRefered == 0) {
                errorHandler.warn(jump.location, "useless labelL: " + labelName);
            }
        });
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

    private void assign(Location location, Expr lhs, Expr rhs) {
        stmts.add(new Assign(location, addressOf(lhs), rhs));
    }

    private DefinedVariable tmpVar(Type type) {
        return scopeStack.getLast().allocateTmp(type);
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
        scopeStack.add(node.getScope());
        for (DefinedVariable var : node.getVariables()) {
            if (var.hasInitializer()) {
                if (var.isPrivate()) {
                    // static variables
                    var.setIr(transformExpr(var.getInitializer()));
                } else {
                    assign(var.location(), ref(var), transformExpr(var.getInitializer()));
                }
            }
        }

        for (StmtNode s : node.getStmts()) {
            transformStmt(s);
        }
        scopeStack.removeLast();
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        Expr e = node.getExpr().accept(this);
        if (e != null) {
            errorHandler.warn(node.location(), "useless expression");
        }
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
        List<Case> cases = new ArrayList<>();
        Label endLabel = new Label();
        Label defaultLabel = new Label();
        Expr cond = transformExpr(node.getCond());
        for (CaseNode cn : node.getCases()) {
            if (cn.isDefault()) {
                defaultLabel = cn.getLabel();
            } else {
                for (ExprNode val : cn.getValues()) {
                    Expr expr = transformExpr(val);
                    cases.add(new Case(((Int) expr).getValue(), cn.getLabel()));
                }
            }
        }
        stmts.add(new Switch(node.location(), cond, cases, defaultLabel, endLabel));
        pushBreak(endLabel);
        for (CaseNode cn : node.getCases()) {
            label(cn.getLabel());
            transformStmt(cn.getBody());
        }
        popBreak();
        label(endLabel);
        return null;
    }

    @Override
    public Void visit(CaseNode node) {
        throw new Error("must not happen");
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

        StmtNode init = node.getInit();
        if (init != null) transformStmt(init);

        label(begLabel);
        cjump(node.location(), transformExpr(node.getCond()), bodyLabel, endLabel);
        label(bodyLabel);
        pushContinue(contLabel);
        pushBreak(endLabel);
        transformStmt(node.getBody());
        popBreak();
        popContinue();
        label(contLabel);
        StmtNode incr = node.getIncr();
        if (incr != null) transformStmt(incr);
        jump(begLabel);
        label(endLabel);
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        try {
            jump(node.location(), currentBreakTarget());
        } catch (JumpError error) {
            error(node, error.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        try {
            jump(node.location(), currentContinueTarget());
        } catch (JumpError error) {
            error(node, error.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(GotoNode node) {
        jump(node.location(), referLabel(node.getTarget()));
        return null;
    }


    @Override
    public Void visit(LabelNode node) {
        try {
            stmts.add(new LabelStmt(node.location(), defineLabel(node.getName(), node.location())));
            if (node.getStmt() != null) {
                transformStmt(node.getStmt());
            }
        } catch (SemanticException e) {
            error(node, e.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        stmts.add(new Return(node.location(), node.getExpr() == null ? null : transformExpr(node.getExpr())));
        return null;
    }


    private Expr transformOpAssign(Location location, Op op, Type lhsType, Expr lhs, Expr rhs) {
        if (lhs.isVar()) {
            assign(location, lhs, bin(op, lhsType, lhs, rhs));
            return isStatement() ? null : lhs;
        } else {
            DefinedVariable a = tmpVar(pointerTo(lhsType));
            assign(location, ref(a), addressOf(lhs));
            assign(location, mem(a), bin(op, lhsType, mem(a), rhs));
            return isStatement() ? null : mem(a);
        }
    }

    private Expr bin(Op op, Type leftType, Expr left, Expr right) {
        if (isPointerArithmetic(op, leftType)) {
            return new Bin(left.getType(), op, left, new Bin(right.getType(), Op.MUL, right, ptrBaseSize(leftType)));
        } else {
            return new Bin(left.getType(), op, left, right);
        }
    }

    @Override
    public Expr visit(CondExprNode node) {
        Label thenLabel = new Label();
        Label elseLabel = new Label();
        Label endLabel = new Label();
        DefinedVariable var = tmpVar(node.getType());

        Expr cond = transformExpr(node.getCond());
        cjump(node.location(), cond, thenLabel, elseLabel);
        label(thenLabel);
        assign(node.getThenExpr().location(), ref(var), transformExpr(node.getThenExpr()));
        jump(endLabel);
        label(elseLabel);
        assign(node.getElseExpr().location(), ref(var), transformExpr(node.getElseExpr()));
        jump(endLabel);
        label(endLabel);
        return isStatement() ? null : ref(var);
    }

    @Override
    public Expr visit(LogicalOrNode node) {
        Label rightLabel = new Label();
        Label endLabel = new Label();
        DefinedVariable var = tmpVar(node.getType());
        assign(node.getLeft().location(), ref(var), transformExpr(node.getLeft()));
        cjump(node.location(), ref(var), rightLabel, endLabel);
        label(rightLabel);
        assign(node.getRight().location(), ref(var), transformExpr(node.getRight()));
        label(endLabel);
        return isStatement() ? null : ref(var);
    }

    @Override
    public Expr visit(LogicalAndNode node) {
        Label rightLabel = new Label();
        Label endLabel = new Label();
        DefinedVariable var = tmpVar(node.getType());
        assign(node.getLeft().location(), ref(var), transformExpr(node.getLeft()));
        cjump(node.location(), ref(var), endLabel, rightLabel);
        label(rightLabel);
        assign(node.getRight().location(), ref(var), transformExpr(node.getRight()));
        label(endLabel);
        return isStatement() ? null : ref(var);
    }

    @Override
    public Expr visit(BinaryOpNode node) {
        Expr right = transformExpr(node.getRight());
        Expr left = transformExpr(node.getLeft());
        Op op = Op.internBinary(node.getOperator(), node.getType().isSigned());
        Type t = node.getType();
        Type r = node.getRight().getType();
        Type l = node.getLeft().getType();
        if (isPointerDiff(op, l, r)) {
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


    @Override
    public Expr visit(AssignNode node) {
        Location lloc = node.getLhs().location();
        Location rloc = node.getRhs().location();
        if (isStatement()) {
            Expr rhs = transformExpr(node.getRhs());
            Expr lhs = transformExpr(node.getLhs());
            assign(lloc, lhs, rhs);
            return null;
        } else {
            DefinedVariable tmp = tmpVar(node.getRhs().getType());
            Expr lhs = transformExpr(node.getLhs());
            Expr rhs = transformExpr(node.getRhs());
            assign(rloc, ref(tmp), rhs);
            assign(lloc, lhs, ref(tmp));
            return ref(tmp);
        }
    }

    @Override
    public Expr visit(OpAssignNode node) {
        Expr lhs = transformExpr(node.getLhs());
        Expr rhs = transformExpr(node.getRhs());
        Type type = node.getLhs().getType();
        Op op = Op.internBinary(node.getOperator(), type.isSigned());
        return transformOpAssign(node.location(), op, type, lhs, rhs);
    }

    @Override
    public Expr visit(PrefixOpNode node) {
        // ++expr -> expr += 1
        Type t = node.getExpr().getType();
        return transformOpAssign(node.location(), binOp(node.getOperator()), t, transformExpr(node.getExpr()), imm(t, 1));
    }

    @Override
    public Expr visit(SuffixOpNode node) {
        Expr expr = transformExpr(node.getExpr());
        Type type = node.getExpr().getType();
        Op op = binOp(node.getOperator());
        Location location = node.location();
        if (isStatement()) {
            // expr++  ->  expr += 1
            transformOpAssign(location, op, type, expr, imm(type, 1));
            return null;
        } else if (expr.isVar()) {
            // cont(expr++)  ->  var = expr; expr = var + 1; cont(var)
            DefinedVariable var = tmpVar(type);
            assign(location, ref(var), expr);
            assign(location, expr, bin(op, type, ref(var), imm(type, 1)));
            return ref(var);
        } else {
            DefinedVariable a = tmpVar(pointerTo(type));
            DefinedVariable v = tmpVar(type);
            assign(location, ref(a), addressOf(expr));
            assign(location, ref(v), mem(a));
            assign(location, mem(a), bin(op, type, mem(a), imm(type, 1)));
            return ref(v);
        }
    }

    @Override
    public Expr visit(ArefNode node) {
        Expr expr = transformExpr(node.getExpr());
        Expr offset = new Bin(ptrdiff_t(), Op.MUL, size(node.elementSize()), transformIndex(node));
        Bin addr = new Bin(ptr_t(), Op.ADD, expr, offset);
        return mem(addr, node.getType());
    }

    private Expr transformIndex(ArefNode node) {
        if (node.isMultiDimension()) {
            return new Bin(int_t(), Op.ADD, transformExpr(node.getIndex()), new Bin(int_t(), Op.MUL, new Int(int_t(), node.length()), transformIndex((ArefNode) node.getExpr())));
        } else {
            return transformExpr(node.getIndex());
        }
    }

    @Override
    public Expr visit(MemberNode node) {
        Expr expr = addressOf(transformExpr(node.getExpr()));
        Expr offset = ptrdiff(node.offset());
        Expr addr = new Bin(ptr_t(), Op.ADD, expr, offset);
        return node.isLoadable() ? mem(addr, node.getType()) : addr;
    }


    @Override
    public Expr visit(PtrMemberNode node) {
        Expr expr = transformExpr(node.getExpr());
        Expr offset = ptrdiff(node.offset());
        Expr addr = new Bin(ptr_t(), Op.ADD, expr, offset);
        return node.isLoadable() ? mem(addr, node.getType()) : addr;
    }

    @Override
    public Expr visit(FuncallNode node) {
        List<Expr> args = new ArrayList<>();
        for (ExprNode arg : ListUtils.reverse(node.getArgs())) {
            args.add(0, transformExpr(arg));
        }
        Expr call = new Call(asmType(node.getType()), transformExpr(node.getExpr()), args);
        if (isStatement()) {
            stmts.add(new ExprStmt(node.location(), call));
            return null;
        } else {
            DefinedVariable tmp = tmpVar(node.getType());
            assign(node.location(), ref(tmp), call);
            return ref(tmp);
        }
    }

    @Override
    public Expr visit(DereferenceNode node) {
        Expr addr = transformExpr(node.getExpr());
        return node.isLoadable() ? mem(addr, node.getType()) : addr;
    }

    @Override
    public Expr visit(AddressNode node) {
        Expr expr = transformExpr(node.getExpr());
        return node.getExpr().isLoadable() ? addressOf(expr) : expr;
    }

    @Override
    public Expr visit(CastNode node) {
        if (node.isEffectiveCast()) {
            return new Uni(asmType(node.getType()), node.getExpr().getType().isSigned() ? Op.S_CAST : Op.U_CAST, transformExpr(node.getExpr()));
        } else if (isStatement()) {
            transformStmt(node.getExpr());
            return null;
        } else {
            return transformExpr(node.getExpr());
        }
    }

    @Override
    public Expr visit(SizeofExprNode node) {
        return new Int(size_t(), node.getExpr().allocSize());
    }

    @Override
    public Expr visit(SizeofTypeNode node) {
        return new Int(size_t(), node.operand().allocSize());
    }

    @Override
    public Expr visit(VariableNode node) {
        if (node.getEntity().isConstant()) {
            return transformExpr(node.getEntity().getValue());
        }
        Var var = ref(node.getEntity());
        return node.isLoadable() ? var : addressOf(var);
    }

    @Override
    public Expr visit(IntegerLiteralNode node) {
        return new Int(asmType(node.getType()), node.getValue());
    }

    @Override
    public Expr visit(StringLiteralNode node) {
        return new Str(asmType(node.getType()), node.getEntry());
    }


    private Op binOp(String uniOp) {
        return "++".equals(uniOp) ? Op.ADD : Op.SUB;
    }

    private Expr addressOf(Expr expr) {
        return expr.addressNode(ptr_t());
    }

    private Var ref(Entity entity) {
        return new Var(varType(entity.getType()), entity);
    }

    private Int ptrdiff(long n) {
        return new Int(ptrdiff_t(), n);
    }

    private Int size(long n) {
        return new Int(size_t(), n);
    }

    private Int imm(Type operandType, long n) {
        if (operandType.isPointer()) {
            return new Int(ptrdiff_t(), n);
        } else {
            return new Int(int_t(), n);
        }
    }

    private Type pointerTo(Type type) {
        return typeTable.pointerTo(type);
    }

    private Mem mem(Entity entity) {
        return new Mem(asmType(entity.getType().getBaseType()), ref(entity));
    }

    private Mem mem(Expr expr, Type type) {
        return new Mem(asmType(type), expr);
    }

    private asm.Type varType(Type type) {
        if (!type.isScalar()) return null;
        return asm.Type.get(type.size());
    }

    private asm.Type asmType(Type type) {
        if (type.isVoid()) return int_t();
        return asm.Type.get(type.size());
    }

    private asm.Type int_t() {
        return asm.Type.get(typeTable.getIntSize());
    }

    private asm.Type size_t() {
        return asm.Type.get(typeTable.getLongSize());
    }

    private asm.Type ptr_t() {
        return asm.Type.get(typeTable.getPointerSize());
    }

    private asm.Type ptrdiff_t() {
        return asm.Type.get(typeTable.getLongSize());
    }

    private void error(Node node, String message) {
        errorHandler.error(node.location(), message);
    }
}
