package entity;

import asm.Symbol;
import ast.Dumper;
import ast.ExprNode;
import ast.TypeNode;
import ir.Expr;
import type.Type;

/**
 * Created by sulvto on 16-11-14.
 */
public class DefinedVariable extends Variable {
    ExprNode initializer;
    Expr ir;
    long sequence;
    Symbol symbol;

    public DefinedVariable(boolean priv, TypeNode type, String name, ExprNode init) {
        super(priv, type, name);
        this.initializer = init;
        this.sequence = -1;
    }

    private static long temSeq = 0;

    public static DefinedVariable tem(Type type) {
        return new DefinedVariable(false, new TypeNode(type), "@tmp" + temSeq++, null);
    }

    public boolean isDefined() {
        return true;
    }

    @Override
    public boolean isInitialized() {
        // TODO
        return false;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String symbolString() {
        return (sequence < 0) ? name : (name + "." + sequence);
    }

    public boolean hasInitializer() {
        return initializer != null;
    }

    public boolean isInitializer() {
        return hasInitializer();
    }

    public ExprNode getInitializer() {
        return initializer;
    }

    public void setInitializer(ExprNode initializer) {
        this.initializer = initializer;
    }

    public void setIr(Expr ir) {
        this.ir = ir;
    }

    public Expr getIr() {
        return ir;
    }

    @Override
    protected void doDump(Dumper dumper) {
        dumper.printMember("name", name);
        dumper.printMember("isPrivate", isPrivate);
        dumper.printMember("typeNode", typeNode);
        dumper.printMember("initializer", initializer);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
