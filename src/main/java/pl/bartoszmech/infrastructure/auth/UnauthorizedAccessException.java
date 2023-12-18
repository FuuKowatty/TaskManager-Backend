package pl.bartoszmech.infrastructure.auth;

public class UnauthorizedAccessException extends RuntimeException{
    UnauthorizedAccessException(String message) {
        super(message);
    }
}
