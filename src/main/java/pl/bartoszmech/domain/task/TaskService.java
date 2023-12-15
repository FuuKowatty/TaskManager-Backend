package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Log4j2
class TaskService {
    private static final String TASK_DUPLICATE = "Provided task is already assigned to this same user";
    private  static String INVALID_DATE_ORDER = "Provided invalid dates order";
    private static final String TASK_NOT_FOUND = "Task with provided id could not be found";

    private final TaskRepository repository;

    TaskDto createTask(CreateTaskRequestDto task, LocalDateTime startDate) {
        Task savedTask = repository.save(new Task(
                task.title(),
                task.description(),
                false,
                startDate,
                task.endDate(),
                task.assignedTo()
        ));
        return TaskMapper.mapFromTask(savedTask);
    }

    void checkIfStartDateIfBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            log.error(startDate + "cant be after or this same as " + endDate);
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

    public TaskDto findById(Long id) {
        Task foundTask = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));
        return TaskMapper.mapFromTask(foundTask);
    }

    public TaskDto deleteById(Long id) {
        TaskDto deletedTask = findById(id);
        repository.deleteById(id);
        return deletedTask;
    }

    public TaskDto updateTask(Long id, UpdateTaskRequestDto taskRequestDto, LocalDateTime startDate) {
        findById(id);
        Task newTask = new Task(
                id,
                taskRequestDto.title(),
                taskRequestDto.description(),
                taskRequestDto.isCompleted(),
                startDate,
                taskRequestDto.endDate(),
                taskRequestDto.assignedTo()
        );
        return TaskMapper.mapFromTask(repository.save(newTask));
    }

    public void checkIfUserHaveAlreadyThisTask(CreateTaskRequestDto inputTask) {
        if(isTaskAssignedToSameUser(inputTask)) {
            throw new DuplicateUserTaskException(TASK_DUPLICATE);
        }
    }

    private boolean isTaskAssignedToSameUser(CreateTaskRequestDto inputTask) {
        return listTasks().stream()
                .anyMatch(task -> task.title().equals(inputTask.title()) &&
                        task.assignedTo().equals(inputTask.assignedTo()));
    }
}
