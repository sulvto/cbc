package asm;

import sysdep.x86.RegisterClass;
import type.Type;

/**
 * 寄存器
 * Created by sulvto on 16-11-26.
 */
public class Register extends Operand {
    public Register(RegisterClass bp, Type naturalType) {

    }


    @Override
    public String toSource(SymbolTable table) {
        return null;
    }

    @Override
    public String dump() {
        return null;
    }

    @Override
    public void collectStatistics(Statistics stats) {

    }
}
