package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TaskFacade {
    private final TaskService taskService;
    private final Clock clock;

    public TaskDto createTask(CreateTaskRequestDto taskRequestDto) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        taskService.checkIfStartDateIfBeforeEndDate(startDate, taskRequestDto.endDate());
        taskService.checkIfUserHaveAlreadyThisTask(taskRequestDto);
        return taskService.createTask(taskRequestDto, startDate);
    }

    public List<TaskDto> listTasks() {
        return taskService.listTasks();
    }

    public TaskDto findById(long id) {
        return taskService.findById(id);
    }

    public TaskDto deleteById(long id) {
        return taskService.deleteById(id);
    }

    public TaskDto updateTask(long id, UpdateTaskRequestDto taskRequestDto) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = taskRequestDto.endDate();
        taskService.checkIfStartDateIfBeforeEndDate(startDate, endDate);
        return taskService.updateTask(id, taskRequestDto, startDate);
    }

    public List<TaskDto> listEmployeeTasks(long id) {
        return taskService.listTasks()
                .stream()
                .filter(task -> task.assignedTo().equals(id))
                .toList();
    }

    public void completeTask(long id) {
        taskService.completeTask(id);
    }
}
