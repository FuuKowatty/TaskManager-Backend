package pl.bartoszmech.domain.accountidentifier;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message) {
        super(message);
    }
}
