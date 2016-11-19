package entity;

import ast.Constant;

/**
 * Created by sulvto on 16-11-18.
 */
public interface EntityVisitor<T> {
    T visit(DefinedVariable definedVariable);

    T visit(UndefinedVariable undefinedVariable);

    T visit(DefinedFunction definedFunction);

    T visit(UndefinedFunction undefinedFunction);

    T visit(Constant constant);
}
