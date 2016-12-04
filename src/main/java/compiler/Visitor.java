package compiler;

import ast.*;
import entity.DefinedVariable;

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
    public Void visit(BlockNode blockNode) {
        for (DefinedVariable variable : blockNode.getVariables()) {
            if (variable.hasInitializer()) {
                visitExpr(variable.getInitializer());
            }
        }
        visitStmts(blockNode.getStmts());
        return null;
    }

    @Override
    public Void visit(ExprStmtNode exprStmtNode) {
        visitExpr(exprStmtNode.getExpr());
        return null;
    }

    @Override
    public Void visit(IfNode ifNode) {
        visitExpr(ifNode.getCond());
        visitStmt(ifNode.getThenBody());
        StmtNode elseBody = ifNode.getElseBody();
        if (elseBody != null) {
            visitStmt(elseBody);
        }
        return null;
    }

    @Override
    public Void visit(SwitchNode switchNode) {
        visitExpr(switchNode.getCond());
        visitStmts(switchNode.getCases());
        return null;
    }

    @Override
    public Void visit(CaseNode caseNode) {
        visitExprs(caseNode.getValues());
        visitStmt(caseNode.getBody());
        return null;
    }

    @Override
    public Void visit(WhileNode whileNode) {
        visitExpr(whileNode.getCond());
        visitStmt(whileNode.getBody());
        return null;
    }

    @Override
    public Void visit(DoWhileNode doWhileNode) {
        visitStmt(doWhileNode.getBody());
        visitExpr(doWhileNode.getCond());
        return null;
    }

    @Override
    public Void visit(ForNode forNode) {
        StmtNode init = forNode.getInit();
        if (init != null) visitStmt(init);
        ExprNode cond = forNode.getCond();
        if(cond!=null) visitExpr(cond);
        StmtNode incr = forNode.getIncr();
        if(incr!=null) visitStmt(incr);
        visitStmt(forNode.getBody());
        return null;
    }

    @Override
    public Void visit(BreakNode breakNode) {
        return null;
    }

    @Override
    public Void visit(ContinueNode continueNode) {
        return null;
    }

    @Override
    public Void visit(GotoNode gotoNode) {
        return null;
    }

    @Override
    public Void visit(LabelNode labelNode) {
        visitStmt(labelNode.getStmt());
        return null;
    }

    @Override
    public Void visit(ReturnNode returnNode) {
        ExprNode expr = returnNode.getExpr();
        if (expr != null) {
            visitExpr(expr);
        }
        return null;
    }

    //
    // Expressions
    //

    @Override
    public Void visit(AssignNode assignNode) {
        visitExpr(assignNode.getLhs());
        visitExpr(assignNode.getRhs());
        return null;
    }

    @Override
    public Void visit(OpAssignNode opAssignNode) {
        visitExpr(opAssignNode.getLhs());
        visitExpr(opAssignNode.getRhs());
        return null;
    }

    @Override
    public Void visit(CondExprNode condExprNode) {
        visitExpr(condExprNode.getCond());
        visitExpr(condExprNode.getThenExpr());
        ExprNode elseExpr = condExprNode.getElseExpr();
        if (elseExpr != null) {
            visitExpr(elseExpr);
        }
        return null;
    }

    @Override
    public Void visit(LogicalOrNode logicalOrNode) {
        visitExpr(logicalOrNode.getLeft());
        visitExpr(logicalOrNode.getRight());
        return null;
    }

    @Override
    public Void visit(LogicalAndNode logicalAndNode) {
        visitExpr(logicalAndNode.getLeft());
        visitExpr(logicalAndNode.getRight());
        return null;
    }

    @Override
    public Void visit(BinaryOpNode binaryOpNode) {
        visitExpr(binaryOpNode.getLeft());
        visitExpr(binaryOpNode.getRight());
        return null;
    }

    @Override
    public Void visit(UnaryOpNode unaryOpNode) {
        visitExpr(unaryOpNode.getExpr());
        return null;
    }

    @Override
    public Void visit(PrefixOpNode prefixOpNode) {
        visitExpr(prefixOpNode.getExpr());
        return null;
    }

    @Override
    public Void visit(SuffixOpNode suffixOpNode) {
        visitExpr(suffixOpNode.getExpr());
        return null;
    }

    @Override
    public Void visit(ArefNode arefNode) {
        visitExpr(arefNode.getExpr());
        visitExpr(arefNode.getIndex());
        return null;
    }

    @Override
    public Void visit(MemberNode memberNode) {
        visitExpr(memberNode.getExpr());
        return null;
    }

    @Override
    public Void visit(PtrMemberNode ptrMemberNode) {
        visitExpr(ptrMemberNode.getExpr());
        return null;
    }

    @Override
    public Void visit(FuncallNode funcallNode) {
        visitExpr(funcallNode.getExpr());
        visitExprs(funcallNode.getArgs());
        return null;
    }

    @Override
    public Void visit(DereferenceNode dereferenceNode) {
        visitExpr(dereferenceNode.getExpr());
        return null;
    }

    @Override
    public Void visit(AddressNode addressNode) {
        visitExpr(addressNode.getExpr());
        return null;
    }

    @Override
    public Void visit(CastNode castNode) {
        visitExpr(castNode.getExpr());
        return null;
    }

    @Override
    public Void visit(SizeofExprNode sizeofExprNode) {
        visitExpr(sizeofExprNode.getExpr());
        return null;
    }

    @Override
    public Void visit(SizeofTypeNode sizeofTypeNode) {
        return null;
    }

    @Override
    public Void visit(VariableNode variableNode) {
        return null;
    }

    @Override
    public Void visit(IntegerLiteralNode integerLiteralNode) {
        return null;
    }

    @Override
    public Void visit(StringLiteralNode stringLiteralNode) {
        return null;
    }
}
