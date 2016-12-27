package utils;

import exception.IPCException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by sulvto on 16-12-27.
 */
public class CommandUtils {
    public static void invoke(List<String> cmdArgs, ErrorHandler errorHandler, boolean verbose) throws IPCException {
        if (verbose) {
            dumpCommand(cmdArgs);
        }

        try {
            String[] cmd = cmdArgs.toArray(new String[]{});
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            passThrough(process.getInputStream());
            passThrough(process.getErrorStream());
            if (process.exitValue() != 0) {
                errorHandler.error(cmd[0] + " failed." + "(status " + process.exitValue() + ")");
                throw new IPCException("compiler error");
            }
        } catch (InterruptedException e) {
            errorHandler.error("external command interrupted: " + cmdArgs.get(0) + ": " + e.getMessage());
            throw new IPCException("compiler error");
        } catch (IOException e) {
            errorHandler.error("IO error in external command: " + e.getMessage());
            throw new IPCException("compiler error");
        }
    }

    private static void dumpCommand(List<String> args) {
        String sep = "";
        for (String arg : args) {
            System.out.print(sep);
            sep = " ";
            System.out.print(arg);
        }
        System.out.println("");
    }

    private static void passThrough(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = r.readLine()) != null) {
            System.err.println(line);
        }
    }
}
