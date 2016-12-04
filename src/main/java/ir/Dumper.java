package ir;

import asm.Label;
import ast.Location;
import entity.DefinedFunction;
import entity.DefinedVariable;
import type.Type;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by sulvto on 16-11-27.
 */
public class Dumper {
    PrintStream stream;
    private int numIndent;

    public Dumper(PrintStream s) {
        stream = s;
        numIndent = 0;
    }

    public void printClass(Object obj) {
        printIndent();
        stream.println("<<" + obj.getClass().getSimpleName() + ">>");
    }

    public void printClass(Object obj, Location location) {
        printIndent();
        stream.println("<<" + obj.getClass().getSimpleName() + ">> (" + location + ")");
    }

    public void printMember(String name, int memb) {
        printPair(name, memb + "");
    }

    public void printMember(String name, long memb) {
        printPair(name, memb + "");
    }

    public void printMember(String name, boolean memb) {
        printPair(name, memb + "");
    }

    public void printMember(String name, String memb) {
        printPair(name, memb);
    }

    public void printMember(String name, Label memb) {
        printPair(name, Integer.toHexString(memb.hashCode()));
    }

    public void printMember(String name, Type memb) {
        printPair(name, memb.toString());
    }

    public void printMember(String name, asm.Type memb) {
        printPair(name, memb.toString());
    }

    private void printPair(String name, String value) {
        printIndent();
        stream.println(name + ": " + value);
    }

    public void printMember(String name, Dumpable memb) {
        printIndent();
        if (memb == null) {
            stream.println(name + ": null");
        } else {
            stream.println(name + ":");
            indent();
            memb.dump(this);
            unindent();
        }
    }


    public void printMembers(String name, List<? extends Dumpable> elems) {
        printIndent();
        stream.println(name + ":");
        indent();
        for (Dumpable elem : elems) {
            elem.dump(this);
        }
        unindent();
    }

    public void printVars(String name, List<DefinedVariable> vars) {
        printIndent();
        stream.println(name + ":");
        indent();
        for (DefinedVariable var : vars) {
            printClass(var, var.location());
            printMember("name", var.getName());
            printMember("isPrivate", var.isPrivate());
            printMember("type", var.getType());
            printMember("initializer", var.getIr());
        }
        unindent();
    }

    public void printFuncs(String name, List<DefinedFunction> definedFunctionList) {
        printIndent();
        stream.println(name + ":");
        indent();
        for (DefinedFunction fun : definedFunctionList) {
            printClass(fun, fun.location());
            printMember("name", fun.getName());
            printMember("isPrivate", fun.isPrivate());
            printMember("type", fun.getType());
            printMembers("body", fun.getIr());
        }
    }

    private void indent() {
        numIndent++;
    }

    private void unindent() {
        numIndent--;
    }

    final static private String indentString = "    ";

    private void printIndent() {
        int n = numIndent;
        while (n > 0) {
            stream.print(indentString);
            n--;
        }
    }
}
