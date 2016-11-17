package ast;

/**
 * Created by sulvto on 16-11-16.
 */
public  interface DeclarationVisitor<T> {

    T visit(StructNode structNode);

    T visit(TypedefNode typedefNode);

    T visit(UnionNode unionNode);
}