package pl.bartoszmech.domain.task;

public class DuplicateUserTaskException extends RuntimeException{
    public DuplicateUserTaskException(String message) {
        super(message);
    }
}
