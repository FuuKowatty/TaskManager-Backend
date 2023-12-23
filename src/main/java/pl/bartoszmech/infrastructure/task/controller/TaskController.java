package pl.bartoszmech.infrastructure.task.controller;

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
import pl.bartoszmech.domain.task.TaskFacade;
import pl.bartoszmech.domain.task.dto.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.infrastructure.auth.AuthorizationService;
import pl.bartoszmech.infrastructure.task.TaskInfoResponseDto;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    public static final String TASK_COMPLETED = "Task completed";
    private final TaskFacade taskFacade;
    private final AuthorizationService authorizationService;
    @GetMapping
    public ResponseEntity<List<TaskDto>> listTasks() {
        return ResponseEntity.status(OK).body(taskFacade.listTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findTaskById(@PathVariable("id") long id) {
        TaskDto task = findTaskAndCheckIfEmployeeHasPermission(id);
        return ResponseEntity.status(OK).body(task);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid CreateAndUpdateTaskRequestDto requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(CREATED).body(taskFacade.createTask(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTaskById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskFacade.deleteById(id));    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> editTaskById(@PathVariable("id") long id, @RequestBody @Valid CreateAndUpdateTaskRequestDto  requestDto) {
        authorizationService.checkIfTaskAssignedToEmployee(requestDto.assignedTo());
        return ResponseEntity.status(OK).body(taskFacade.updateTask(id, requestDto));
    }

    @GetMapping("/employee/{userId}")
    public ResponseEntity<List<TaskDto>> listEmployeeTasks(@PathVariable("userId") long id) {
        authorizationService.hasUserPermissionToReadTasksOfEmployee(id);
        return ResponseEntity.status(OK).body(taskFacade.listEmployeeTasks(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskInfoResponseDto> completeTask(@PathVariable("id") long id) {
        findTaskAndCheckIfEmployeeHasPermission(id);
        taskFacade.completeTask(id);
        return ResponseEntity.status(OK).body(new TaskInfoResponseDto(TASK_COMPLETED));
    }

    private TaskDto findTaskAndCheckIfEmployeeHasPermission(long id) {
        TaskDto task = taskFacade.findById(id);
        authorizationService.hasUserPermissionToReadTaskWithId(id, task.assignedTo());
        return task;
    }
}
