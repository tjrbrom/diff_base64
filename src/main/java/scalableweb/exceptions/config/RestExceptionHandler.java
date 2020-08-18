package scalableweb.exceptions.config;

import java.time.Instant;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import scalableweb.domain.error.ErrorDetails;
import scalableweb.exceptions.MissingInformationException;
import scalableweb.exceptions.NotBase64Exception;
import scalableweb.exceptions.ResourceNotFoundException;

/**
 * Centralized exception handling.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Resource Not Found", HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotBase64Exception.class)
    public ResponseEntity<?> handleNotBase64Exception(
            NotBase64Exception ex, HttpServletRequest request) {

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Invalid input detected.",
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingInformationException.class)
    public ResponseEntity<?> handleMissingInformationException(
            MissingInformationException ex, HttpServletRequest request) {

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Missing information.", HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Illegal argument given.",
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Invalid input detected.",
                        HttpStatus.BAD_REQUEST.value(),
                        Instant.now(),
                        ex.getBindingResult().getAllErrors().stream()
                                .map(ObjectError::getDefaultMessage)
                                .collect(Collectors.toList()));

        return new ResponseEntity<>(errorDetails, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Handler Not Found", HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ErrorDetails errorDetails =
                new ErrorDetails(
                        "Method Not Allowed",
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        ex.getMessage(),
                        Instant.now());

        return new ResponseEntity<>(errorDetails, null, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
