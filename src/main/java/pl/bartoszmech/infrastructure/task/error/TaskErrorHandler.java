package pl.bartoszmech.infrastructure.task.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.infrastructure.task.TaskInfoResponseDto;


@ControllerAdvice
public class TaskErrorHandler {
    @ExceptionHandler(ResourceNotFound.class)
    @ResponseBody
    public ResponseEntity<TaskInfoResponseDto> handleEmailTaken(ResourceNotFound error) {
        return ResponseEntity.status(404).body(new TaskInfoResponseDto(error.getMessage()));
    }
}

