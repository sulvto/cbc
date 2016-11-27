package ast;

/**
 * DONE
 * 后置的++和--
 * Created by sulvto on 16-11-15.
 */
public class SuffixOpNode extends UnaryArithmeticOpNode {

    public SuffixOpNode(String op, ExprNode exprNode) {
        super(op, exprNode);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
