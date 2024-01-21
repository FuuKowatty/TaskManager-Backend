package pl.bartoszmech.application.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.CompletedTasksByAssignedToResponseDto;
import pl.bartoszmech.domain.task.AdjustableClock;
import pl.bartoszmech.domain.task.TaskRepositoryTestImpl;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.domain.task.service.TaskServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.bartoszmech.domain.task.TaskStatus.COMPLETED;

public class TaskStatisticsTest {
    private final AdjustableClock clock = new AdjustableClock(
            LocalDateTime.of(2014, 6, 1, 1, 15, 30).toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );
    TaskService taskService = new TaskServiceImpl(new TaskRepositoryTestImpl(), clock);
    @BeforeEach
    public void setUp() {
        List<CreateAndUpdateTaskRequestDto> tasksToAdd = Arrays.asList(
                new CreateAndUpdateTaskRequestDto(
                        "Task 1", "Description 1", LocalDateTime.of(2014, 6, 1, 8, 0), 1L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 2", "Description 2", LocalDateTime.of(2014, 6, 3, 9, 0), 2L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 3", "Description 3", LocalDateTime.of(2014, 7, 22, 8, 0), 3L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 4", "Description 4", LocalDateTime.of(2014, 7, 23, 9, 0), 4L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 5", "Description 5", LocalDateTime.of(2014, 8, 22, 8, 0), 1L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 6", "Description 6", LocalDateTime.of(2014, 8, 24, 9, 0), 3L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 7", "Description 7", LocalDateTime.of(2014, 9, 22, 8, 0), 3L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 8", "Description 8", LocalDateTime.of(2014, 9, 23, 9, 0), 2L),
                new CreateAndUpdateTaskRequestDto(
                        "Task 9", "Description 9", LocalDateTime.of(2014, 10, 22, 8, 0), 1L));
                tasksToAdd.forEach(task -> taskService.createTask(task));
    }

    @AfterEach
    public void resetClock() {
        clock.setClockToLocalDateTime(LocalDateTime.of(2014, 6, 1, 1, 15, 30));
    }

    @Test
    public void should_return_completed_tasks_by_assignedTo() {
        //given
        clock.plusMonths(3);
        taskService.listTasks().forEach(task -> taskService.completeTask(task.id()));
        //when
        List<CompletedTasksByAssignedToResponseDto> completedTasksByAssignedTo = taskService.getCompletedTasksByAssignedTo(3);
        //then
        int allCompletedTasks = completedTasksByAssignedTo.stream()
                .reduce(0, (acc, obj) -> acc + obj.numberOfCompletedTasks(), Integer::sum);

        assertThat(allCompletedTasks).isEqualTo(taskService.listTasks().stream().filter(task -> task.status() == COMPLETED).toList().size());
    }

    @Test
    public void should_return_empty_array_if_there_is_not_completed_tasks() {
        //given
        clock.plusMonths(3);
        //when
        List<CompletedTasksByAssignedToResponseDto> completedTasksByAssignedTo = taskService.getCompletedTasksByAssignedTo(3);
        //then
        assertThat(completedTasksByAssignedTo).isEmpty();
    }

    @Test
    public void should_return_empty_array_if_there_is_failed_tasks() {
        //given
        clock.plusMonths(3);
        taskService.markAsFailedOutdatedTasks();
        //when
        List<CompletedTasksByAssignedToResponseDto> completedTasksByAssignedTo = taskService.getCompletedTasksByAssignedTo(3);
        //then
        assertThat(completedTasksByAssignedTo).isEmpty();

    }
}
