package pl.bartoszmech.application.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.TaskResponseDto;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.application.services.AuthorizationService;
import pl.bartoszmech.application.response.TaskInfoResponseDto;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;
import pl.bartoszmech.infrastructure.auth.error.UnauthorizedAccessException;

import javax.naming.AuthenticationException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthorizationService authorizationService;

    @Operation(summary = "Find all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class)))
    })
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> findAllTasks() {
        return ResponseEntity.status(OK).body(taskService.listTasks());
    }

    @Operation(summary = "Find task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Task with provided id not found in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findTaskById(@PathVariable("id") long id) {
        TaskResponseDto task = findTaskAndCheckIfEmployeeHasPermission(id);
        return ResponseEntity.status(OK).body(task);
    }

    @Operation(summary = "Create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class)))
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid CreateAndUpdateTaskRequestDto requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(CREATED).body(taskService.createTask(requestDto));
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Task with provided id not found in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDto> deleteTaskById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskService.deleteById(id));    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "Cannot access this resource (You have to be manager or admin)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),
            @ApiResponse(responseCode = "404", description = "Task with provided id not found in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> editTaskById(@PathVariable("id") long id, @RequestBody @Valid CreateAndUpdateTaskRequestDto  requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(OK).body(taskService.updateTask(id, requestDto));
    }

    @Operation(summary = "Get employee task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "Cannot access this resource (You have to be manager or admin)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),
            @ApiResponse(responseCode = "404", description = "Employee with provided id not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @GetMapping("/employee/{userId}")
    public ResponseEntity<List<TaskResponseDto>> listEmployeeTasks(@PathVariable("userId") long id) {
        authorizationService.hasUserPermissionToReadTasksOfEmployee(id);
        return ResponseEntity.status(OK).body(taskService.listEmployeeTasks(id));
    }

    @Operation(summary = "Complete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "Cannot access this resource (You have to be employee and it has to be task assigned to you)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),
            @ApiResponse(responseCode = "403", description = "You cant complete task because it is already completed or outdated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task with provided id not found in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskInfoResponseDto> completeTask(@PathVariable("id") long id) {
        findTaskAndCheckIfEmployeeHasPermission(id);
        TaskInfoResponseDto taskResponse = taskService.completeTask(id);
        return ResponseEntity.status(taskResponse.status()).body(taskResponse);
    }

    private TaskResponseDto findTaskAndCheckIfEmployeeHasPermission(long id) {
        TaskResponseDto task = taskService.findById(id);
        authorizationService.hasUserPermissionToReadTaskWithId(id, task.assignedTo());
        return task;
    }

}
