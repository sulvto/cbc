package ast;

/**
 * 结构体的定义
 * Created by sulvto on 16-11-15.
 */
public class StructNode extends CompositeTypeDefinition {
    @Override
    public <T> T accept(DeclarationVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
