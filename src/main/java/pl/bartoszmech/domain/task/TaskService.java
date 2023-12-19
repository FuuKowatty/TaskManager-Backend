package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Log4j2
class TaskService {
    private static final String TASK_DUPLICATE = "Provided task is already assigned to this same user";
    private  static final String INVALID_DATE_ORDER = "Provided invalid dates order";
    private static final String TASK_NOT_FOUND = "Task with provided id could not be found";

    private final TaskRepository repository;

    TaskDto createTask(TaskDto inputTask) {
        validateIfTaskCanBeCreated(inputTask);
        Task savedTask = repository.save(TaskMapper.mapToTask(inputTask));
        return TaskMapper.mapFromTask(savedTask);
    }

    List<TaskDto> listTasks() {
        return repository
                .findAll()
                .stream()
                .map(TaskMapper::mapFromTask)
                .toList();
    }

    TaskDto findById(long id) {
        Task foundTask = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));
        return TaskMapper.mapFromTask(foundTask);
    }

    TaskDto deleteById(long id) {
        TaskDto deletedTask = findById(id);
        repository.deleteById(id);
        return deletedTask;
    }

    TaskDto updateTask(long id, TaskDto inputTask) {
        findById(id);
        validateIfTaskCanBeCreated(inputTask);
        Task newTask = new Task(
                id,
                inputTask.title(),
                inputTask.description(),
                inputTask.isCompleted(),
                inputTask.startDate(),
                inputTask.endDate(),
                inputTask.assignedTo()
        );
        return TaskMapper.mapFromTask(repository.save(newTask));
    }

    void completeTask(long id) {
        repository.markTaskAsCompleted(id);
    }

    private void validateIfTaskCanBeCreated(TaskDto inputTask) {
        checkIfStartDateIfBeforeEndDate(inputTask.startDate(), inputTask.endDate());
        checkIfUserHaveAlreadyThisTask(inputTask);
    }

    private void checkIfUserHaveAlreadyThisTask(TaskDto inputTask) {
        if(isTaskAssignedToSameUser(inputTask)) {
            throw new DuplicateUserTaskException(TASK_DUPLICATE);
        }
    }


    private void checkIfStartDateIfBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            log.error(startDate + "cant be after or this same as " + endDate);
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }
    }

    private boolean isTaskAssignedToSameUser(TaskDto inputTask) {
        return listTasks().stream()
                .anyMatch(task -> task.title().equals(inputTask.title()) &&
                        task.assignedTo().equals(inputTask.assignedTo()));
    }
}
