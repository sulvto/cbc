package ast;

/**
 * Created by sulvto on 16-11-14.
 */
public class Location {
    protected String sourceName;
    protected CflatToken token;

    public Location(String sourceName, Token token) {
        this(sourceName, new CflatToken(token));
    }

    public Location(String sourceName, CflatToken token) {
        this.sourceName = sourceName;
        this.token = token;
    }



    public CflatToken[] token() {
        return new CflatToken[0];
    }

    @Override
    public String toString() {
        return sourceName+":"+token.lineno();
    }
}
