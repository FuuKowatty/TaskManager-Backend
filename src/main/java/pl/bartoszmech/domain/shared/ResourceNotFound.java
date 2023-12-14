package pl.bartoszmech.domain.shared;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String taskNotFound) {
        super(taskNotFound);
    }
}
