package utils;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;

import java.io.UnsupportedEncodingException;

/**
 * Created by sulvto on 16-11-15.
 */
public abstract class TextUtils {
    private final static byte vtab = 013;

    public static String dumpString(String str) {
        try {
            return dumpString(str, Parser.SOURCE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new Error("UTF-8 is not supported??: " + e.getMessage());
        }
    }

    public static String dumpString(String string, String encoding) throws UnsupportedEncodingException {
        byte[] src = string.getBytes(encoding);
        StringBuilder buffer = new StringBuilder();
        buffer.append("\"");
        for (int n = 0; n < src.length; n++) {
            int c = toUnsigned(src[n]);
            if (c == '"') buffer.append("\\\"");
            else if (isPrintable(c)) buffer.append((char) c);
            else if (c == '\b') buffer.append("\\b");
            else if (c == '\t') buffer.append("\\t");
            else if (c == '\n') buffer.append("\\n");
            else if (c == vtab) buffer.append("\\v");
            else if (c == '\f') buffer.append("\\f");
            else if (c == '\r') buffer.append("\\r");
            else buffer.append("\\").append(Integer.toOctalString(c));
        }
        buffer.append("\"");
        return buffer.toString();
    }

    private static int toUnsigned(byte b) {
        return b >= 0 ? b : 256 + b;
    }

    private static boolean isPrintable(int c) {
        return (' ' <= c) && (c <= '~');
    }
}
