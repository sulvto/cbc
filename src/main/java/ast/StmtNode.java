package ast;

/**
 * 表示语句的节点
 * Created by sulvto on 16-11-14.
 */
public abstract class StmtNode extends Node{
    private Location location;

    public StmtNode(Location location) {
        this.location = location;
    }
    @Override
    public Location location() {
        return location;
    }

    abstract public <S,E> S accept(ASTVisitor<S,E> visitor);

}
