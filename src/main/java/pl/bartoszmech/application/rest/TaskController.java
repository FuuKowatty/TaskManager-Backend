package pl.bartoszmech.application.rest;

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
import pl.bartoszmech.infrastructure.auth.AuthorizationService;
import pl.bartoszmech.infrastructure.task.TaskInfoResponseDto;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final AuthorizationService authorizationService;
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> listTasks() {
        return ResponseEntity.status(OK).body(taskService.listTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findTaskById(@PathVariable("id") long id) {
        TaskResponseDto task = findTaskAndCheckIfEmployeeHasPermission(id);
        return ResponseEntity.status(OK).body(task);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid CreateAndUpdateTaskRequestDto requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(CREATED).body(taskService.createTask(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDto> deleteTaskById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskService.deleteById(id));    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> editTaskById(@PathVariable("id") long id, @RequestBody @Valid CreateAndUpdateTaskRequestDto  requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(OK).body(taskService.updateTask(id, requestDto));
    }

    @GetMapping("/employee/{userId}")
    public ResponseEntity<List<TaskResponseDto>> listEmployeeTasks(@PathVariable("userId") long id) {
        authorizationService.hasUserPermissionToReadTasksOfEmployee(id);
        return ResponseEntity.status(OK).body(taskService.listEmployeeTasks(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskInfoResponseDto> completeTask(@PathVariable("id") long id) {
        findTaskAndCheckIfEmployeeHasPermission(id);
        String messageStatus = taskService.completeTask(id);
        return ResponseEntity.status(OK).body(new TaskInfoResponseDto(messageStatus));
    }

    private TaskResponseDto findTaskAndCheckIfEmployeeHasPermission(long id) {
        TaskResponseDto task = taskService.findById(id);
        authorizationService.hasUserPermissionToReadTaskWithId(id, task.assignedTo());
        return task;
    }
}
