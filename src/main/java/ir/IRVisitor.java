package ir;

/**
 * Created by sulvto on 16-11-20.
 */
public interface IRVisitor<S, E> {

    S visit(CJump cJump);

    S visit(LabelStmt labelStmt);

    S visit(Jump jump);
}
