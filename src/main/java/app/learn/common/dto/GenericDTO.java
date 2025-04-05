package app.learn.common.dto;

import app.learn.common.enums.JsonStatusKey;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "message"})
public class GenericDTO {
    private JsonStatusKey status;
    private String message;

    public GenericDTO() {

    }

    public GenericDTO(JsonStatusKey status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonStatusKey getStatus() {
        return status;
    }

    public void setStatus(JsonStatusKey status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
