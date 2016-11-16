package ast;

/**
 * 成员表达式 （ptr->memb）
 * Created by sulvto on 16-11-15.
 */
public class PtrMemberNode  extends LHSNode{
    ExprNode exprNode;
    String memb;

    public PtrMemberNode(ExprNode exprNode, String memb) {
        this.exprNode = exprNode;
        this.memb = memb;
    }
}
