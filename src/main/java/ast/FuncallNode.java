package ast;

import type.Type;

import java.util.List;

/**
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

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {

    }
}
