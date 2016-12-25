package asm;

import utils.TextUtils;

/**
 * Created by sulvto on 16-11-26.
 */
public class Comment extends Assembly {
    protected String content;
    protected int indentLevel;

    public Comment(String content) {
        this(content, 0);
    }

    public Comment(String content, int indentLevel) {
        this.content = content;
        this.indentLevel = indentLevel;
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public String toSource(SymbolTable table) {
        return "\t" + indent() + "#" + content;
    }

    protected String indent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    @Override
    public String dump() {
        return "(Comment " + TextUtils.dumpString(content) + ")";
    }
}
