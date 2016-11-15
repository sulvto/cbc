package ast;

import type.TypeRef;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by sulvto on 16-11-14.
 */
public class Dumper {
    int nIndex;
    PrintStream stream;

    public Dumper(PrintStream printStream) {
        this.stream = printStream;
        this.nIndex = 0;
    }

    public void printClass(Object obj, Location location) {
        printIndent();
        stream.println("<<" + obj.getClass().getSimpleName() + ">>(" + location + ")");
    }

    public void printNodeList(String name, List<? extends Dumpable> nodes) {
        printIndent();
        stream.print(name + ":");
        indent();
        for (Dumpable n : nodes) {
            n.dump(this);
        }
        unindent();
    }

    public void printMember(String name, int n) {
        printPair(name, "" + n);

    }

    public void printMember(String name, long n) {
        printPair(name, "" + n);

    }

    public void printMember(String name, boolean b) {
        printPair(name, "" + b);
    }

    public void printMember(String name, TypeRef ref) {
        printPair(name, ref.toString());
    }

    public void printMember(String name, Type t) {
        printPair(name, (t == null ? "null" : t.toString()));
    }

    public void printMember(String name, String str, boolean isResolved) {
        printPair(name, TextUtils.dumpString(str) + (isResolved ? "(resolved)" : ""));
    }

    public void printMember(String name, String str) {
        printMember(name, str, false);
    }

    void printPair(String name, String value) {
        printIndent();
        stream.println(name + ": " + value);
    }

    public void printMember(String name, TypeNode n) {
        printIndent();
        stream.println(name + ": " + n.typeRef() + (n.isResolved() ? "(resolved)" : ""));
    }

    public void printMember(String name, Dumpable n) {
        printIndent();
        if (n == null) {
            stream.println(name + ": null");
        } else {
            stream.println(name + ":");
            indent();
            n.dump(this);
            unindent();
        }
    }

    void indent() {
        nIndex++;
    }

    void unindent() {
        nIndex--;
    }

    static final protected String indentString = "    ";

    void printIndent() {
        int n = nIndex;
        while (n > 0) {
            stream.print(indentString);
            n--;
        }
    }

}
