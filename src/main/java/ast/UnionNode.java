package ast;

/**
 * 联合体的定义
 * Created by sulvto on 16-11-15.
 */
public class UnionNode extends CompositeTypeDefinition {
    @Override
    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
