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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.request.CreateUserDto;
import pl.bartoszmech.application.request.UpdateUserDto;
import pl.bartoszmech.application.response.CompletedTasksByAssignedToResponseDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.application.services.EmployeeAnalysisService;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.domain.user.UserMapper;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.infrastructure.apivalidation.ParameterValidation;
import pl.bartoszmech.application.services.AuthorizationService;
import pl.bartoszmech.application.response.CompletedTasksStatisticResponseDto;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;
import pl.bartoszmech.infrastructure.auth.error.UnauthorizedAccessException;

import javax.naming.AuthenticationException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final TaskService taskService;
    private final EmployeeAnalysisService employeeAnalysisService;

    @Operation(summary = "Find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers() {
        return ResponseEntity.status(OK).body(userService.listUsers());
    }

    @Operation(summary = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "User with provided id not found in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(userService.findById(id));
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "You cannot create user with admin role using this method",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto requestDto) {
        authorizationService.checkIfUserWantsCreateAdmin(requestDto.role());
        return ResponseEntity.status(CREATED).body(userService.createUser(UserMapper.mapToCreateAndUpdateRequest(requestDto)));
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "User with provided id not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(userService.deleteById(id));    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "You cannot create user with admin role with this method",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),
            @ApiResponse(responseCode = "404", description = "User with provided id not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceNotFound.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> editUserById(@PathVariable("id") long id, @Valid @RequestBody UpdateUserDto requestDto) {
        authorizationService.checkIfUserWantsCreateAdmin(requestDto.role());
        return ResponseEntity.status(OK).body(userService.updateUser(id, requestDto));
    }

    @Operation(summary = "Find all users with employee role and sort it by number of completed tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success operation"),
            @ApiResponse(responseCode = "400", description = "Last months parameter was invalid type or out of range",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Error, Dont pass token or pass invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationException.class)))
    })
    @GetMapping("/stats/sorted-by-completed-tasks")
    public ResponseEntity<List<CompletedTasksStatisticResponseDto>> listBestEmployee(@RequestParam(
            name = "last-months",
            required = false,
            defaultValue = "6"
    ) int lastMonths) {
        ParameterValidation.validateLastMonths(lastMonths);

        List<CompletedTasksByAssignedToResponseDto> completedTasks = taskService.getCompletedTasksByAssignedTo(lastMonths);
        List<UserResponseDto> employees = userService.listEmployees();

        List<CompletedTasksStatisticResponseDto> statistics = employeeAnalysisService.sortEmployeesByCompletedTasks(employees, completedTasks, lastMonths);
        return ResponseEntity.ok(statistics);
    }

}

