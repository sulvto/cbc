package ir;

import asm.Type;
import entity.Entity;
import entity.Function;

import java.util.List;

/**
 * Created by sulvto on 16-12-3.
 */
public class Call extends Expr {
    private Expr expr;
    private List<Expr> args;

    public Call(Type type, Expr expr, List<Expr> args) {
        super(type);
        this.expr = expr;
        this.args = args;
    }

    public Expr getExpr() {
        return expr;
    }

    public List<Expr> getArgs() {
        return args;
    }

    public long numArgs() {
        return args.size();
    }

    public boolean isStaticCall() {
        return (expr.getEntityForce() instanceof Function);
    }

    public Function getFunction() {
        Entity ent = expr.getEntityForce();
        if (ent == null) {
            throw new Error("not a static funcall");
        }
        return (Function) ent;
    }

    @Override
    protected void doDump(Dumper d) {
        d.printMember("expr", expr);
        d.printMembers("args", args);
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
