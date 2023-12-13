package pl.bartoszmech.domain.task;

public class EndDateBeforeStartDateException extends RuntimeException {
    public EndDateBeforeStartDateException(String message) {
        super(message);
    }
}
