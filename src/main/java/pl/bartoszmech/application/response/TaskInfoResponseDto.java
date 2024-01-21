package pl.bartoszmech.application.response;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public record TaskInfoResponseDto(String message, HttpStatus status) {

    private static final String TASK_CREATED_SUCCESSFULLY = "Task created successfully";
    private static final String TASK_IS_OUTDATED = "Task is outdated";
    private static final String TASK_IS_ALREADY_COMPLETED = "Task is already completed";

    public static TaskInfoResponseDto TASK_COMPLETED() {
        return new TaskInfoResponseDto(TASK_CREATED_SUCCESSFULLY, OK);
    }

    public static TaskInfoResponseDto TASK_OUTDATED() {
        return new TaskInfoResponseDto(TASK_IS_OUTDATED, FORBIDDEN);
    }

    public static TaskInfoResponseDto TASK_ALREADY_COMPLETED() {
        return new TaskInfoResponseDto(TASK_IS_ALREADY_COMPLETED, FORBIDDEN);
    }

}
