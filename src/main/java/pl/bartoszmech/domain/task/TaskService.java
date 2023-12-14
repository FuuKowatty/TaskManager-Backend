package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
class TaskService {
    private final TaskRepository repository;
    private  static String INVALID_DATE_ORDER = "Provided invalid dates order";
    private static final String TASK_NOT_FOUND = "Task with provided id could not be found";

    TaskDto createTask(CreateTaskRequestDto task, LocalDateTime startDate) {
        Task savedTask = repository.save(Task.builder()
                .title(task.title())
                .description(task.description())
                .startDate(startDate)
                .endDate(task.endDate())
                .build());
        return TaskMapper.mapFromTask(savedTask);
    }

    void checkIfStartDateIfBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }
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

    public TaskDto updateTask(String id, UpdateTaskRequestDto taskRequestDto, LocalDateTime startDate) {
        Task newTask = Task.builder()
                .title(taskRequestDto.title())
                .description(taskRequestDto.description())
                .isCompleted(taskRequestDto.isCompleted())
                .startDate(startDate)
                .endDate(taskRequestDto.endDate())
                .build();
        TaskDto foundTask = findById(id);
        return TaskMapper.mapFromTask(repository.update(foundTask.id(), newTask));
    }
}
