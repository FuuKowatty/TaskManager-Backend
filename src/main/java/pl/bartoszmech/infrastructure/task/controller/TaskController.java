package pl.bartoszmech.infrastructure.task.controller;

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
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;
import pl.bartoszmech.infrastructure.auth.AuthorizationService;
import pl.bartoszmech.infrastructure.task.TaskInfoResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
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
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequestDto requestDto) {
        return ResponseEntity.status(CREATED).body(taskFacade.createTask(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTaskById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskFacade.deleteById(id));    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTaskById(@PathVariable("id") long id, @RequestBody UpdateTaskRequestDto  requestDto) {
        return ResponseEntity.status(OK).body(taskFacade.updateTask(id, requestDto));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<TaskDto>> listEmployeeTasks(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskFacade.listEmployeeTasks(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskInfoResponse> completeTask(@PathVariable("id") long id) {
        findTaskAndCheckIfEmployeeHasPermission(id);
        taskFacade.completeTask(id);
        return ResponseEntity.status(OK).body(new TaskInfoResponse("Task completed"));
    }

    private TaskDto findTaskAndCheckIfEmployeeHasPermission(long id) {
        TaskDto task = taskFacade.findById(id);
        authorizationService.hasUserPermissionToReadTaskWithId(id, task.assignedTo());
        return task;
    }
}
