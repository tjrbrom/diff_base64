package scalableweb.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * To be used as part of HTTP diff responses
 */
public class DiffCalculationResponse {

    @JsonInclude(Include.NON_NULL)
    private String message;

    @JsonInclude(Include.NON_NULL)
    private String differentOffsets;

    public DiffCalculationResponse() {
        super();
    }

    public DiffCalculationResponse(String message, String differentOffsets) {
        this.message = message;
        this.differentOffsets = differentOffsets;
    }

    public String getDifferentOffsets() {
        return differentOffsets;
    }

    public void setDifferentOffsets(String differentOffsets) {
        this.differentOffsets = differentOffsets;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
