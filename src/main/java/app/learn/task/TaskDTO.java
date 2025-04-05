package app.learn.task;

import app.learn.common.enums.JsonStatusKey;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "message", "task"})
public class TaskDTO {
    private String message;
    private Task task;
    private JsonStatusKey status;

    public TaskDTO() {

    }

    public TaskDTO(JsonStatusKey status, String message, Task task) {
        this.status = status;
        this.message = message;
        this.task = task;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public JsonStatusKey getStatus() {
        return status;
    }

    public void setStatus(JsonStatusKey status) {
        this.status = status;
    }
}
