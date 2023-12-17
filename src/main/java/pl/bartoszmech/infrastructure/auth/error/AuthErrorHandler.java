package pl.bartoszmech.infrastructure.auth.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.domain.accountidentifier.EmailTakenException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class AuthErrorHandler {
    private static final String BAD_CREDENTIALS = "Bad Credentials";
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleBadCredentials() {
        return ResponseEntity.status(UNAUTHORIZED).body(new AuthErrorResponseBody(BAD_CREDENTIALS));
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleEmailTaken(EmailTakenException error) {
        return ResponseEntity.status(CONFLICT).body(new AuthErrorResponseBody(error.getMessage()));
    }
}
