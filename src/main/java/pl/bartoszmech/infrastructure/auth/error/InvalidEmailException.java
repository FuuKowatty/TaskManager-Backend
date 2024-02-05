package pl.bartoszmech.infrastructure.auth.error;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidEmailException extends BadCredentialsException {
    public InvalidEmailException(String msg) {
        super(msg);
    }
}
