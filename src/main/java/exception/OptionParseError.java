package exception;

/**
 * Created by sulvto on 16-11-23.
 */
public class OptionParseError extends Error {
    public OptionParseError(String message) {
        super(message);
    }
}
