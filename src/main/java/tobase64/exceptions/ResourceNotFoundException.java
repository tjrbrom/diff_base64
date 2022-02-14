package tobase64.exceptions;

import tobase64.domain.Diff;

/**
 * Thrown when a resource is not found, like a non-existent {@link Diff}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
