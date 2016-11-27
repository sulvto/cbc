package ast;

import java.util.Iterator;

/**
 *
 * Created by sulvto on 16-11-14.
 */
public class CflatToken implements Iterator<CflatToken>{
    private Token token;
    private boolean isSpecial;

    public CflatToken(Token token) {
        this.token = token;
    }

    public CflatToken(Token token, boolean isSpecial) {
        this.token = token;
        this.isSpecial = isSpecial;
    }

    @Override
    public String toString() {
        return token.image;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public int kindId() {
        return token.kind;
    }
    public String kindName() {
        return null;
    }

    public String dumpedImage() {
        return null;
    }

    // TODO

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CflatToken next() {
        return null;
    }
}
