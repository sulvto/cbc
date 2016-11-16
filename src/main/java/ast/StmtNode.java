package ast;

/**
 * 表示语句的节点
 * Created by sulvto on 16-11-14.
 */
public abstract class StmtNode extends Node{
    @Override
    public Location location() {
        return null;
    }

    @Override
    protected void _dump(Dumper d) {

    }

    abstract public <S,E> S accept(ASTVisitor<S,E> visitor);

}
