package parser;

import ast.Declarations;
import exception.CompileException;
import exception.FileException;
import exception.SemanticException;
import utils.ErrorHandler;

import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.util.*;

/**
 * Created by sulvto on 16-11-28.
 */
public class LibraryLoader {
    private List<String> loadPath;
    private LinkedList<String> loadingLibraries;
    private Map<String, Declarations> loadedLibraries;

    public static List<String> defaultLoadPath() {
        List<String> paths = new ArrayList<>();
        paths.add(".");
        return paths;
    }

    public LibraryLoader() {
        this(defaultLoadPath());
    }

    public LibraryLoader(List<String> loadPath) {
        this.loadPath = loadPath;
        this.loadingLibraries = new LinkedList<>();
        this.loadedLibraries = new HashMap<>();
    }

    public void addLoadPath(String path) {
        loadPath.add(path);
    }

    public Declarations loadLibrary(String libid, ErrorHandler errorHandler) throws CompileException {
        if (loadingLibraries.contains(libid)) {
            throw new SemanticException("recursive import from " + loadingLibraries.getLast() + ": " + libid);
        }
        loadingLibraries.addLast(libid);
        Declarations decls = loadedLibraries.get(libid);
        if (decls != null) {
            return decls;
        }
        decls = Parser.parseDeclFile(searchLibrary(libid), this, errorHandler);
        loadedLibraries.put(libid, decls);
        loadingLibraries.removeLast();
        return decls;
    }

    public File searchLibrary(String libid) throws FileException {
        try {
            for (String path : loadPath) {
                File file = new File(path + "/" + libid + ".nb");
                if (file.exists()) {
                    return file;
                }
            }
            throw new FileException("no such library header file: " + libid);
        } catch (SecurityException ex) {
            throw new FileException(ex.getMessage());
        }
    }

    private String libPath(String id) {
        return id.replace(".", "/");
    }
}
