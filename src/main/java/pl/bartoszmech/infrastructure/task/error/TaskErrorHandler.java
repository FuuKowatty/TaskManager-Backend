package pl.bartoszmech.infrastructure.task.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import pl.bartoszmech.application.response.TaskInfoResponseDto;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
public class TaskErrorHandler {

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseBody
    public ResponseEntity<TaskInfoResponseDto> handleEmailTaken(ResourceNotFound error) {
        return ResponseEntity.status(404).body(new TaskInfoResponseDto(error.getMessage(), NOT_FOUND));
    }

}

