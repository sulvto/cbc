package asm;

import utils.TextUtils;

/**
 * Created by sulvto on 16-11-26.
 */
public class Directive extends Assembly {
    protected String content;

    public Directive(String content) {
        this.content = content;
    }

    @Override
    public boolean isDirective() {
        return true;
    }

    @Override
    public String toSource(SymbolTable table) {
        return this.content;
    }

    @Override
    public String dump() {
        return "(Directive " + TextUtils.dumpString(content.trim() + ")");
    }
}
