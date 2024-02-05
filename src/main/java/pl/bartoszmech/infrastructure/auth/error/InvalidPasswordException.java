package pl.bartoszmech.infrastructure.auth.error;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidPasswordException extends BadCredentialsException {
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
