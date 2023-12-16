package pl.bartoszmech.infrastructure.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.domain.task.TaskFacade;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TaskController {
    TaskFacade taskFacade;
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> listTasks() {
        return ResponseEntity.status(OK).body(taskFacade.listTasks());
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> listTasks(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskFacade.findById(id));
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequestDto requestDto) {
        return ResponseEntity.status(CREATED).body(taskFacade.createTask(requestDto));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> deleteTaskById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(taskFacade.deleteById(id));    }
}
