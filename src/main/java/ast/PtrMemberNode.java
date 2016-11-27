package ast;

import exception.SemanticError;
import type.CompositeType;
import type.PointerType;
import type.Type;

/**
 * DONE
 * 成员表达式 （ptr->memb）
 * Created by sulvto on 16-11-15.
 */
public class PtrMemberNode extends LHSNode {
    protected ExprNode expr;
    protected String member;

    public PtrMemberNode(ExprNode exprNode, String memb) {
        this.expr = exprNode;
        this.member = memb;
    }

    public CompositeType dereferedCompositeType() {
        try {
            PointerType pt = expr.getType().getPointerType();
            return pt.getBaseType().getCompositeType();
        } catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public Type dereferedType() {
        try {
            PointerType pt = expr.getType().getPointerType();
            return pt.getBaseType();
        } catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public ExprNode getExpr() {
        return expr;
    }

    public String getMember() {
        return member;
    }

    public long offset() {
        return dereferedCompositeType().memberOffset(member);
    }

    @Override
    public Type origType() {
        return dereferedCompositeType().memberType(member);
    }

    @Override
    public Location location() {
        return expr.location();
    }

    @Override
    protected void doDump(Dumper d) {
        if (type != null) {
            d.printMember("type", type);
        }
        d.printMember("expr", expr);
        d.printMember("member", member);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
