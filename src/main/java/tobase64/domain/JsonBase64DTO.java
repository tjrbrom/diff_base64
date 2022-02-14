package tobase64.domain;

import javax.validation.constraints.NotNull;

/**
 * Base64 encoded binary data transfer object
 */
public class JsonBase64DTO {

    @NotNull
    private String base64EncodedData;

    public JsonBase64DTO() {
        super();
    }

    public JsonBase64DTO(String base64EncodedData) {
        this.base64EncodedData = base64EncodedData;
    }

    public String getBase64EncodedData() {
        return base64EncodedData;
    }

    public void setBase64EncodedData(String base64EncodedData) {
        this.base64EncodedData = base64EncodedData;
    }
}
