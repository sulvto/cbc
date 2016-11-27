package ast;

import exception.SemanticError;
import type.CompositeType;
import type.Type;

/**
 * DONE
 * 成员表达式 （s.memb）
 * Created by sulvto on 16-11-15.
 */
public class MemberNode extends LHSNode {
    private ExprNode expr;
    private String member;

    public MemberNode(ExprNode exprNode, String member) {
        this.expr = exprNode;
        this.member = member;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public CompositeType baseType() {
        try {
            return expr.getType().getCompositeType();
        } catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public String getMember() {
        return member;
    }

    public long offset() {
        return baseType().memberOffset(member);
    }

    @Override
    public Type origType() {
        return baseType().memberType(member);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void doDump(Dumper d) {
        if (type != null) {
            d.printMember("type", type);
        }
        d.printMember("expr", expr);
        d.printMember("member", member);
    }
}
