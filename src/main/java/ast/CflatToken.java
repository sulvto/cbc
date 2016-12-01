package ast;

import parser.ParserConstants;
import parser.Token;
import utils.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sulvto on 16-11-14.
 */
public class CflatToken implements Iterable<CflatToken> {
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
        return ParserConstants.tokenImage[token.kind];
    }

    public int lineno() {
        return token.beginLine;
    }

    public int column() {
        return token.beginColumn;
    }

    public String image() {
        return token.image;
    }

    public String dumpedImage() {
        return TextUtils.dumpString(token.image);
    }

    protected List<CflatToken> tokensWithoutFirstSpecials() {
        return buildTokenList(token, true);
    }

    protected List<CflatToken> buildTokenList(Token first, boolean rejectFirstSpecials) {
        List<CflatToken> result = new ArrayList<>();
        boolean rejectSpecials = rejectFirstSpecials;
        for (Token t = first; t != null; t = t.next) {
            if (t.specialToken != null && !rejectSpecials) {
                Token s = specialTokenHead(t.specialToken);
                for (; s != null; s = s.next) {
                    result.add(new CflatToken(s));
                }
            }
            result.add(new CflatToken(t));
            rejectSpecials = false;
        }
        return result;
    }

    private Token specialTokenHead(Token firstSpecial) {
        Token s = firstSpecial;
        while (s.specialToken != null) {
            s = s.specialToken;
        }
        return s;
    }

    @Override
    public Iterator<CflatToken> iterator() {
        return buildTokenList(token, false).iterator();
    }
}
