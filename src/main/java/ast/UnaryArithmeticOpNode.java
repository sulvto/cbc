package ast;

/**
 * DONE
 * Created by sulvto on 16-11-15.
 */
public class UnaryArithmeticOpNode extends UnaryOpNode {
    protected long amount;

    public UnaryArithmeticOpNode(String op, ExprNode exprNode) {
        super(op, exprNode);
        amount = 1;
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
