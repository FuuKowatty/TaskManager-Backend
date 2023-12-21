package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.task.dto.CompletedTasksByAssignedtoResponseDto;
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
        return taskService.createTask(TaskDto.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .startDate(getNow())
                .endDate(taskRequestDto.endDate())
                .assignedTo(taskRequestDto.assignedTo())
                .build());
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
        return taskService.updateTask(id,TaskDto.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .startDate(getNow())
                .endDate(taskRequestDto.endDate())
                .assignedTo(taskRequestDto.assignedTo())
                .build());
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

    public List<CompletedTasksByAssignedtoResponseDto> getCompletedTasksByAssignedTo(int lastMonths) {
        LocalDateTime taskEndDateRange = getNow().minusMonths(lastMonths);
        return taskService.getCompletedTasksByAssignedTo(taskEndDateRange);
    }

    private LocalDateTime getNow() {
        return LocalDateTime.now(clock);
    }

}
