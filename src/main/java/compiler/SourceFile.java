package compiler;

/**
 * Created by sulvto on 16-11-23.
 */
public class SourceFile implements LdArg {
    static final String EXT_CFLAT_SOURCE = ".cb";
    static final String EXT_ASSEMBLY_SOURCE = ".s";
    static final String EXT_OBJECT_FILE = ".o";
    static final String EXT_STATIC_LIBRARY = ".a";
    static final String EXT_SHARED_LIBRARY = ".so";
    static final String EXT_EXECUTABLE_FILE = "";

    private static final String[] KNOWN_EXTENSIONS = {
            EXT_CFLAT_SOURCE,
            EXT_ASSEMBLY_SOURCE,
            EXT_OBJECT_FILE,
            EXT_STATIC_LIBRARY,
            EXT_SHARED_LIBRARY,
            EXT_EXECUTABLE_FILE
    };

    private final String originalName;
    private String currentName;

    public SourceFile(String name) {
        this.originalName = name;
        this.currentName = name;
    }

    @Override
    public boolean isSourceFile() {
        return true;
    }

    @Override
    public String toString() {
        return currentName;
    }

    public boolean isKnownFileType() {
        String ext = extName(originalName);
        for (String e : KNOWN_EXTENSIONS) {
            if(ext.equals(e)) return true;
        }
        return false;
    }

    public String path() {
        return currentName;
    }

    public String currentName() {
        return currentName;
    }

    private String extName(String path) {
        int idx = path.lastIndexOf(".");
        if(idx<0) return "";
        return path.substring(idx);
    }
}
