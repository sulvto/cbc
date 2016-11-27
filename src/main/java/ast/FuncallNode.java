package ast;

import exception.SemanticError;
import type.FunctionType;
import type.Type;

import java.util.List;

/**
 * DONE
 * 函数调用
 * Created by sulvto on 16-11-15.
 */
public class FuncallNode extends ExprNode {
    ExprNode expr;
    List<ExprNode> args;

    public FuncallNode(ExprNode exprNode, List<ExprNode> args) {
        this.expr = exprNode;
        this.args = args;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public List<ExprNode> getArgs() {
        return args;
    }

    public FunctionType getFunctionType() {
        return expr.getType().getPointerType().getBaseType().getFunctionType();
    }

    @Override
    public Type getType() {
        try {
            return getFunctionType().getReturnType();
        } catch (ClassCastException e) {
            throw new SemanticError(e.getMessage());
        }
    }

    public long numArgs() {
        return args.size();
    }

    public void replaceArgs(List<ExprNode> args) {
        this.args = args;
    }

    @Override
    public Location location() {
        return expr.location();
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
        d.printNodeList("args", args);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
