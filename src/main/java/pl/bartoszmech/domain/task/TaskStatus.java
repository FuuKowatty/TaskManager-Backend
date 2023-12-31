package pl.bartoszmech.domain.task;

public enum TaskStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed");

    TaskStatus(String status) {
    }
}
