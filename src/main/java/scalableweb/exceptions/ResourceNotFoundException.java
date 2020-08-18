package scalableweb.exceptions;

import scalableweb.domain.Diff;

/**
 * Thrown when a resource is not found, like a non-existent {@link Diff}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
