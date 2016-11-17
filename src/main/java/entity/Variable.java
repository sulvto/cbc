package entity;

import ast.TypeNode;

/**
 * Created by sulvto on 16-11-18.
 */
public abstract class Variable extends Entity {
    public Variable(boolean priv, TypeNode type, String name) {
        super(priv, type, name);
    }
}
