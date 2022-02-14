package tobase64.domain.error;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

/**
 * Object to be used as part of an HTTP error response.
 */
public class ErrorDetails {

    private String error;
    private int status;

    @JsonInclude(Include.NON_NULL)
    private String detail;
    private Instant timeStamp;

    @JsonInclude(Include.NON_NULL)
    private List<String> errors;

    public ErrorDetails(String error, int status, String detail, Instant timeStamp) {
        this.error = error;
        this.status = status;
        this.detail = detail;
        this.timeStamp = timeStamp;
    }

    public ErrorDetails(String error, int status, Instant timeStamp, List<String> errors) {
        this.error = error;
        this.status = status;
        this.timeStamp = timeStamp;
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
