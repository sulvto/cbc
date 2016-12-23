package sysdep.x86;

/**
 * Created by sulvto on 16-12-23.
 */
public interface ELFConstants {

    // Flags
    public static final String SectionFlag_allocatable = "a";
    public static final String SectionFlag_writable = "w";
    public static final String SectionFlag_executable = "x";
    public static final String SectionFlag_sectiongroup = "G";
    public static final String SectionFlag_strings = "S";
    public static final String SectionFlag_threadlocalstorage = "T";


    // argument of "G" flag
    public static final String Linkage_linkonce = "comdat";

    // Types
    public static final String SectionType_bits = "@progbits";
    public static final String SectionType_nobits = "@nobits";
    public static final String SectionType_note = "@note";

    public static final String SymbolType_function = "@function";
}
