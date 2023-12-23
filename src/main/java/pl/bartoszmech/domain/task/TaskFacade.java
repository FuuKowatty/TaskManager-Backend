package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.task.dto.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.domain.task.dto.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static pl.bartoszmech.domain.task.TaskStatus.COMPLETED;
import static pl.bartoszmech.domain.task.TaskStatus.FAILED;
import static pl.bartoszmech.domain.task.TaskStatus.PENDING;

@AllArgsConstructor
public class TaskFacade {
    private final TaskService taskService;
    private final Clock clock;

    public TaskDto createTask(CreateAndUpdateTaskRequestDto taskRequestDto) {
        return taskService.createTask(TaskDto.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .status(PENDING)
                .startDate(getNow())
                .endDate(taskRequestDto.endDate())
                .completedAt(null)
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

    public TaskDto updateTask(long id, CreateAndUpdateTaskRequestDto taskRequestDto) {
        TaskDto foundTask = taskService.findById(id);
        return taskService.updateTask(TaskDto.builder()
                .id(foundTask.id())
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .startDate(foundTask.startDate())
                .endDate(taskRequestDto.endDate())
                .completedAt(foundTask.completedAt())
                .status(foundTask.status())
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
        taskService.markTaskAs(COMPLETED, id, getNow());
    }

    public List<CompletedTasksByAssignedtoResponseDto> getCompletedTasksByAssignedTo(int lastMonths) {
        LocalDateTime taskEndDateRange = getNow().minusMonths(lastMonths);
        return taskService.getCompletedTasksByAssignedTo(taskEndDateRange);
    }

    private LocalDateTime getNow() {
        return LocalDateTime.now(clock);
    }

    public void markAsFailedOutdatedTasks() {
        LocalDateTime checkedDateTime = getNow();
        taskService.listTasks()
                .stream()
                .filter(task -> task.status() == PENDING && task.endDate().isBefore(checkedDateTime))
                .forEach(task -> taskService.markTaskAs(FAILED, task.id(), checkedDateTime));
    }
}
