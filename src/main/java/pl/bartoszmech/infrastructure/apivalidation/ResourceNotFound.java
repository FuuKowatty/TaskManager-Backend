package pl.bartoszmech.infrastructure.apivalidation;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }
}
