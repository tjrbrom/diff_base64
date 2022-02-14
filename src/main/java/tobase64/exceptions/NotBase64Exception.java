package tobase64.exceptions;

/**
 * Thrown when input is not of base64 encoded binary format.
 */
public class NotBase64Exception extends RuntimeException {

    public NotBase64Exception(String message) {
        super(message);
    }
}
