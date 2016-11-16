package ast;

/**
 * 成员表达式 （s.memb）
 * Created by sulvto on 16-11-15.
 */
public class MemberNode extends LHSNode {
    ExprNode exprNode;
    String memb;

    public MemberNode(ExprNode exprNode, String memb) {
        this.exprNode = exprNode;
        this.memb = memb;
    }
}
