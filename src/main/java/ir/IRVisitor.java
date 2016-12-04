package ir;

/**
 * Created by sulvto on 16-11-20.
 */
public interface IRVisitor<S, E> {

    S visit(Assign assign);

    S visit(CJump cJump);

    S visit(ExprStmt exprStmt);

    S visit(LabelStmt labelStmt);

    S visit(Jump jump);

    S visit(Switch aSwitch);

    S visit(Return aReturn);

    E visit(Bin bin);

    E visit(Int anInt);

    E visit(Addr addr);

    E visit(Call call);

    E visit(Mem mem);

    E visit(Str str);

    E visit(Uni uni);

    E visit(Var var);
}
