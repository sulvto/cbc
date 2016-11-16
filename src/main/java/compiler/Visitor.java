package compiler;

import ast.*;

import java.util.List;

/**
 * Created by sulvto on 16-11-17.
 */
public abstract class Visitor implements ASTVisitor<Void, Void> {

    void visitStmt(StmtNode stmtNode) {
        stmtNode.accept(this);
    }

    void visitStmts(List<? extends StmtNode> stmtNodes) {
        for (StmtNode stmtNode : stmtNodes) {
            visitStmt(stmtNode);
        }
    }

    void visitExprs(List<? extends ExprNode> exprNodes) {
        for (ExprNode exprNode : exprNodes) {
            visitExpr(exprNode);
        }
    }

    void visitExpr(ExprNode exprNode) {
        exprNode.accept(this);
    }

    @Override
    public Void visit(BlockNode node) {
        for (DefinedVariable variable : node.getVariables()) {
            if (variable.hasInitializer()) {
                visitExpr(variable.initializer());
            }
        }
        visitStmts(node.getStmtNodes());
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        visitExpr(node.getCond());
        visitStmt(node.getThenBody());
        StmtNode elseBody = node.getElseBody();
        if (elseBody != null) {
            visitStmt(elseBody);
        }
        return null;
    }

    @Override
    public Void visit(SwitchNode node) {
        visitExpr(node.getCond());
        visitStmts(node.getCases());
        return null;
    }

    @Override
    public Void visit(CaseNode node) {
        visitExprs(node.getValues());
        visitStmt(node.getBody());
        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        visitExpr(node.getCond());
        visitStmt(node.getBody());
        return null;
    }

    @Override
    public Void visit(DoWhileNode node) {
        visitStmt(node.getBody());
        visitExpr(node.getCond());
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        visitStmt(node.getInit());
        visitExpr(node.getCond());
        visitStmt(node.getIncr());
        visitStmt(node.getBody());
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        return null;
    }

    @Override
    public Void visit(GotoNode node) {
        return null;
    }

    @Override
    public Void visit(LabelNode node) {
        visitStmt(node.getStmt());
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        ExprNode expr = node.getExpr();
        if (expr != null) {
            visitExpr(expr);
        }
        return null;
    }

    //
    // Expressions
    //

    @Override
    public Void visit(AssignNode node) {
        visitExpr(node.getLhs());
        visitExpr(node.getRhs());
        return null;
    }

    @Override
    public Void visit(OpAssignNode node) {
        visitExpr(node.getLhs());
        visitExpr(node.getRhs());
        return null;
    }

    @Override
    public Void visit(CondExprNode node) {
        visitExpr(node.getCond());
        visitExpr(node.getThenExpr());
        ExprNode elseExpr = node.getElseExpr();
        if (elseExpr != null) {
            visitExpr(elseExpr);
        }
        return null;
    }

    @Override
    public Void visit(LogicalOrNode node) {
        visitExpr(node.getLeft());
        visitExpr(node.getRight());
        return null;
    }

    @Override
    public Void visit(LogicalAndNode node) {
        visitExpr(node.getLeft());
        visitExpr(node.getRight());
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
        visitExpr(node.getLeft());
        visitExpr(node.getRight());
        return null;
    }

    @Override
    public Void visit(UnaryOpNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(PrefixOpNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(SuffixOpNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(ArefNode node) {
        visitExpr(node.getExpr());
        visitExpr(node.getIndex());
        return null;
    }

    @Override
    public Void visit(MemberNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(PtrMemberNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(FuncallNode node) {
        visitExpr(node.getExpr());
        visitExprs(node.getArgs());
        return null;
    }

    @Override
    public Void visit(DereferenceNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(AddressNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(CastNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(SizeofExprNode node) {
        visitExpr(node.getExpr());
        return null;
    }

    @Override
    public Void visit(SizeofTypeNode node) {
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        return null;
    }

    @Override
    public Void visit(IntegerLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        return null;
    }
}
