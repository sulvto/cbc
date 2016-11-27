package utils;

import ast.Location;

import java.io.PrintStream;

/**
 * Created by sulvto on 16-11-19.
 */
public class ErrorHandler {
    private String programId;
    private PrintStream stream;
    private long nError;
    private long nWarning;

    public ErrorHandler(String programId) {
        this.programId = programId;
        this.stream = System.err;
    }

    public ErrorHandler(String programId, PrintStream stream) {
        this.programId = programId;
        this.stream = stream;
    }

    public void error(Location location, String message) {
        error(location.toString() + ":" + message);
    }

    public void error(String message) {
        stream.println(programId + ": error:" + message);
        nError++;
    }

    public boolean errorOccured() {
        return nError > 0;
    }

    public void warn(Location location, String message) {
        warn(location.toString() + ": " + message);
    }

    public void warn(String message) {
        stream.println(programId + ": warning: " + message);
        nWarning++;
    }
}
