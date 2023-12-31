package pl.bartoszmech.infrastructure.apivalidation;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        final List<String> errors = getErrorsFromException(e);
        return ResponseEntity.status(BAD_REQUEST).body(new ValidationResponse(errors));
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> handleDateTimeException(DateTimeParseException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ValidationResponse(List.of("Could not parse LocalDateTime: " + e.getMessage())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> handleDateTimeException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ValidationResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidLastMonthsParameterException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> handleDateTimeException(InvalidLastMonthsParameterException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ValidationResponse(List.of(e.getMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> handleDateTimeException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ValidationResponse(List.of(e.getMessage())));
    }



    private List<String> getErrorsFromException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}
