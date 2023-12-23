package pl.bartoszmech.domain.task;

public enum TaskStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed");
    private String status;
    TaskStatus(String status) {
        this.status = status;
    }
}
