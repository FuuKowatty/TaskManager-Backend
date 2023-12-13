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
    private static final String TASK_NOT_FOUND = "Task with provided id could not be found";
    private final TaskRepository repository;
    private final Clock clock;
    private  static String INVALID_DATE_ORDER = "Provided invalid dates order";
    public TaskDto createTask(CreateTaskRequestDto taskRequestDto) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = taskRequestDto.endDate();
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }
        Task savedTask = repository.save(Task.builder()
                        .title(taskRequestDto.title())
                        .description(taskRequestDto.description())
                        .startDate(startDate)
                        .endDate(endDate)
                        .build());
        return TaskMapper.mapFromTask(savedTask);
    }

    public List<TaskDto> listTasks() {
        return repository
                .findAll()
                .stream()
                .map(task -> TaskMapper.mapFromTask(task))
                .toList();
    }

    public TaskDto findById(String id) {
        Task foundTask = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));
        return TaskMapper.mapFromTask(foundTask);
    }

    public TaskDto deleteById(String id) {
        Task deletedTask = repository.deleteById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));
        return TaskMapper.mapFromTask(deletedTask);
    }

    public TaskDto updateTask(String id, UpdateTaskRequestDto taskRequestDto) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = taskRequestDto.endDate();
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }

        Task newTask = Task.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .isCompleted(taskRequestDto.isCompleted())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Task foundTask = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));

        return TaskMapper.mapFromTask(repository.update(foundTask.id(), newTask));
    }
}
