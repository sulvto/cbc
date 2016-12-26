package compiler;

import java.io.File;

/**
 * Created by sulvto on 16-11-23.
 */
public class SourceFile implements LdArg {
    static final String EXT_CFLAT_SOURCE = ".c";
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
            if (ext.equals(e)) return true;
        }
        return false;
    }

    public boolean isCflatSource() {
        return EXT_CFLAT_SOURCE.equals(extName(currentName));
    }

    public boolean isAssemblySource() {
        return EXT_ASSEMBLY_SOURCE.equals(extName(currentName));
    }

    public boolean isObjectFile() {
        return EXT_OBJECT_FILE.equals(extName(currentName));
    }

    public boolean isSharedLibrary() {
        return EXT_SHARED_LIBRARY.equals(extName(currentName));
    }

    public boolean isStaticLibrary() {
        return EXT_STATIC_LIBRARY.equals(extName(currentName));
    }

    public boolean isExecutable() {
        return EXT_EXECUTABLE_FILE.equals(extName(currentName));
    }


    public String path() {
        return currentName;
    }

    public String currentName() {
        return currentName;
    }

    String asmFileName() {
        return replaceExt(EXT_ASSEMBLY_SOURCE);
    }

    public String objFileName() {
        return replaceExt(EXT_OBJECT_FILE);
    }

    private String replaceExt(String ext) {
        return baseName(originalName, true) + ext;
    }

    private String baseName(String path) {
        return new File(path).getName();
    }

    private String baseName(String path, boolean stripExt) {
        if (stripExt) {
            return new File(path).getName().replaceFirst("\\.[^.]^$", "");
        } else {
            return baseName(path);
        }
    }

    private String extName(String path) {
        int idx = path.lastIndexOf(".");
        if (idx < 0) return "";
        return path.substring(idx);
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String linkedFileName(String newExt) {
        return replaceExt(newExt);
    }
}
