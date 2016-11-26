package entity;

import asm.Label;
import asm.Symbol;
import ast.TypeNode;
import compiler.Parameter;
import type.Type;

import java.util.List;

/**
 * Created by sulvto on 16-11-19.
 */
public abstract class Function extends Entity {
    private Symbol callingSymbol;
    private Label label;

    public Function(boolean priv, TypeNode typeNode, String name) {
        super(priv, typeNode, name);
    }

    @Override
    public boolean isInitialized() {
        return true;
    }


    public abstract boolean isDefined();

    public abstract List<Parameter> parameters();

    public Type returnType() {
        return type().getFunctionType().getReturnType();
    }

    public boolean isVoid() {
        return returnType().isVoid();
    }

    public void setCallingSymbol(Symbol callingSymbol) {
        if (this.callingSymbol != null) {
            throw new Error("must not happen: Function#callingSymbol was set again");
        }
        this.callingSymbol = callingSymbol;
    }

    public Symbol getCallingSymbol() {
        if (this.callingSymbol != null) {
            throw new Error("must not happen: Function#callingSymbol called but null");
        }
        return callingSymbol;
    }

    public Label getLabel() {
        if (label == null) {
            return label = new Label(getCallingSymbol());
        } else {
            return label;
        }
    }
}
