package pl.bartoszmech.domain.task;

public class DuplicateUserTaskException extends RuntimeException{
    DuplicateUserTaskException(String message) {
        super(message);
    }
}
