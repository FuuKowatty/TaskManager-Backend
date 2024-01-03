package pl.bartoszmech.domain.task.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.application.response.TaskInfoResponseDto;
import pl.bartoszmech.application.response.TaskResponseDto;

import pl.bartoszmech.domain.task.DuplicateUserTaskException;
import pl.bartoszmech.domain.task.EndDateBeforeStartDateException;
import pl.bartoszmech.domain.task.Task;
import pl.bartoszmech.domain.task.TaskMapper;
import pl.bartoszmech.domain.task.TaskStatus;
import pl.bartoszmech.domain.task.repository.TaskRepository;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.bartoszmech.application.response.TaskInfoResponseDto.TASK_COMPLETED;
import static pl.bartoszmech.application.response.TaskInfoResponseDto.TASK_OUTDATED;
import static pl.bartoszmech.application.response.TaskInfoResponseDto.TASK_ALREADY_COMPLETED;
import static pl.bartoszmech.domain.task.TaskStatus.COMPLETED;
import static pl.bartoszmech.domain.task.TaskStatus.FAILED;
import static pl.bartoszmech.domain.task.TaskStatus.PENDING;


@AllArgsConstructor
@Log4j2
public class TaskServiceImpl implements TaskService {
    private static final String TASK_DUPLICATE = "Provided task is already assigned to this same user";
    private  static final String INVALID_DATE_ORDER = "Provided invalid dates order";
    private static final String TASK_NOT_FOUND = "Task with provided id could not be found";


    private final TaskRepository repository;

    private final Clock clock;

    @Override
    public TaskResponseDto createTask(CreateAndUpdateTaskRequestDto requestedTask) {
        TaskResponseDto inputTask = TaskMapper.mapFromCreateAndUpdateRequestDto(requestedTask, getNow());
        validateIfTaskCanBeCreated(inputTask);
        return TaskMapper.mapFromTask(
                repository.save(TaskMapper.mapToTask(inputTask))
        );
    }

    @Override
    public List<TaskResponseDto> listTasks() {
        return repository
                .findAll()
                .stream()
                .map(TaskMapper::mapFromTask)
                .toList();
    }

    @Override
    public List<TaskResponseDto> listEmployeeTasks(long id) {
        return listTasks()
                .stream()
                .filter(task -> task.assignedTo().equals(id))
                .toList();
    }

    @Override
    @Transactional
    public TaskInfoResponseDto completeTask(long id) {
        Task task = findEntityById(id);
        TaskStatus status = task.getStatus();
        if (status.equals(PENDING)) {
            task.complete(getNow());
            return TASK_COMPLETED();
        }
        if (status.equals(FAILED)) {
            return TASK_OUTDATED();
        }
        return TASK_ALREADY_COMPLETED();
    }


    @Override
    public TaskResponseDto findById(long id) {
        return TaskMapper.mapFromTask(findEntityById(id));
    }

    private Task findEntityById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(TASK_NOT_FOUND));
    }

    @Override
    public TaskResponseDto deleteById(long id) {
        TaskResponseDto deletedTask = findById(id);
        repository.deleteById(id);
        return deletedTask;
    }

    @Override
    public TaskResponseDto updateTask(long id, CreateAndUpdateTaskRequestDto requestedTask) {
        TaskResponseDto foundTask = findById(id);
        TaskResponseDto inputTask = TaskResponseDto.builder()
                .id(foundTask.id())
                .title(requestedTask.title())
                .description(requestedTask.description())
                .startDate(foundTask.startDate())
                .endDate(requestedTask.endDate())
                .completedAt(foundTask.completedAt())
                .status(foundTask.status())
                .assignedTo(requestedTask.assignedTo())
                .build();
        validateIfTaskCanBeCreated(inputTask);
        return TaskMapper.mapFromTask(
                repository.save(TaskMapper.mapToTask(inputTask))
        );
    }




    private void markTaskAs(TaskStatus status, long id, LocalDateTime completedAt) {
        TaskResponseDto foundTask = findById(id);
        repository.save(new Task(
                foundTask.id(),
                foundTask.title(),
                foundTask.description(),
                status,
                foundTask.startDate(),
                foundTask.endDate(),
                completedAt,
                foundTask.assignedTo()
        ));
    }

    @Override
    public List<CompletedTasksByAssignedtoResponseDto> getCompletedTasksByAssignedTo(int lastMonths) {
        LocalDateTime taskEndDateRange = getNow().minusMonths(lastMonths);
        List<TaskResponseDto> tasksFromLastSixMonths = getTasksFromLastSixMonths(taskEndDateRange);
        Map<Long, Integer> tasksByAssignedTo = groupByAssignedToAndCountCompletedTasks(tasksFromLastSixMonths);
        return tasksByAssignedTo.entrySet().stream()
                .map(entry -> new CompletedTasksByAssignedtoResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsFailedOutdatedTasks() {
        LocalDateTime checkedDateTime = getNow();
        listTasks()
            .stream()
            .filter(task -> task.status() == PENDING && task.endDate().isBefore(checkedDateTime))
            .forEach(task -> markTaskAs(FAILED, task.id(), null));
    }

    private void validateIfTaskCanBeCreated(TaskResponseDto inputTask) {
        checkIfStartDateIfBeforeEndDate(inputTask.startDate(), inputTask.endDate());
        checkIfUserHaveAlreadyThisTask(inputTask);
    }

     private void checkIfUserHaveAlreadyThisTask(TaskResponseDto inputTask) {
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

    private boolean isTaskAssignedToSameUser(TaskResponseDto inputTask) {
        return listTasks().stream()
                .anyMatch(task -> task.title().equals(inputTask.title()) &&
                        task.assignedTo().equals(inputTask.assignedTo()));
    }

    private List<TaskResponseDto> getTasksFromLastSixMonths(LocalDateTime taskEndDateRange) {
        List<TaskResponseDto> tasks = listTasks();
        return tasks.stream()
                .filter(task -> task.status() == COMPLETED && task.endDate().isAfter(taskEndDateRange))
                .toList();
    }

    private Map<Long, Integer> groupByAssignedToAndCountCompletedTasks(List<TaskResponseDto> tasks) {
        return tasks.stream()
                .filter(task -> task.status() == COMPLETED)
                .collect(Collectors.groupingBy(TaskResponseDto::assignedTo, Collectors.summingInt(task -> 1)));
    }

    private LocalDateTime getNow() {
        return LocalDateTime.now(clock);
    }
}
