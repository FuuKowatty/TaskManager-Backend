package pl.bartoszmech.domain.task;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String taskNotFound) {
        super(taskNotFound);
    }
}
