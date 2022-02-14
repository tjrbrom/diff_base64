package tobase64.exceptions;

/**
 * Thrown to indicate that not all data to be diffed are available.
 */
public class MissingInformationException extends RuntimeException {

    public MissingInformationException(String message) {
        super(message);
    }
}
