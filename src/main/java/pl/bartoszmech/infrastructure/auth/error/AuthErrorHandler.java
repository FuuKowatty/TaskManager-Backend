package pl.bartoszmech.infrastructure.auth.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.domain.user.EmailTakenException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class AuthErrorHandler {


    @ExceptionHandler(InvalidEmailException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleBadEmail(InvalidEmailException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body(new AuthErrorResponseBody("email", exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleBadCredentials() {
        return ResponseEntity.status(UNAUTHORIZED).body(new AuthErrorResponseBody("password", "Password does not match"));
    }


    @ExceptionHandler(EmailTakenException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleEmailTaken(EmailTakenException error) {
        return ResponseEntity.status(CONFLICT).body(new AuthErrorResponseBody("email", error.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponseBody> handleInvalidPermission(UnauthorizedAccessException error) {
        return ResponseEntity.status(FORBIDDEN).body(new AuthErrorResponseBody("permission",error.getMessage()));
    }

}
