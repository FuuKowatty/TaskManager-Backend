package pl.bartoszmech.infrastructure.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.domain.task.TaskFacade;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TaskController {
    TaskFacade taskFacade;
    @GetMapping("/tasks")
    public List<TaskDto> listTasks() {
        List<TaskDto> taskDtos = taskFacade.listTasks();
        System.out.println(taskDtos.get(0).toString());
        return taskDtos;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequestDto requestDto) {
        TaskDto createdTask = taskFacade.createTask(requestDto);
        return ResponseEntity.status(CREATED).body(createdTask);
    }
}
