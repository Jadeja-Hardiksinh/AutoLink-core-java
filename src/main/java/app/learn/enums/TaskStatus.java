package app.learn.enums;

public enum TaskStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED");
    private final String value;

    private TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
