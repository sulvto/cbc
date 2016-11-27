package utils;

/**
 * Created by sulvto on 16-11-27.
 */
public final class AsmUtils {
    private AsmUtils() {}

    public static long align(long n, long alignment) {
        return (n + alignment - 1) / alignment * alignment;
    }
}
