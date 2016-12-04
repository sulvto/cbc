package ir;

import asm.Label;

/**
 * Created by sulvto on 16-12-4.
 */
public class Case implements Dumpable {
    public long value;
    public Label label;

    public Case(long value, Label label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public void dump(Dumper dumper) {
        dumper.printClass(this);
        dumper.printMember("value", value);
        dumper.printMember("label", label);
    }
}
