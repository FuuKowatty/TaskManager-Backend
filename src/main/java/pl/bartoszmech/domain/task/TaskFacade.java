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
    public static final String TASK_SUCCESSFULLY_COMPLETED = "Task successfully completed";
    public static final String TASK_IS_OUTDATED = "Task is outdated";
    public static final String TASK_IS_ALREADY_COMPLETED = "Task is already completed";

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

    public String completeTask(long id) {
        TaskDto task = taskService.findById(id);
        if (task.status().equals(PENDING)) {
            taskService.markTaskAs(COMPLETED, id, getNow());
            return TASK_SUCCESSFULLY_COMPLETED;
        }
        if (task.endDate().isBefore(getNow())) {
            return TASK_IS_OUTDATED;
        }
        return TASK_IS_ALREADY_COMPLETED;
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
                .forEach(task -> taskService.markTaskAs(FAILED, task.id(), null));
    }


}
