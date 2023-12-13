package pl.bartoszmech.domain.task;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TaskFacadeTest {
    @Mock
    private Clock clock = Clock.fixed(
            LocalDateTime.of(2014, 12, 22, 10, 15, 30).toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );
    TaskFacade taskFacade = new TaskFacade(new TaskRepositoryTestImpl(), clock);

    @Test
    public void should_successfully_create_task() {
        //given
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusSeconds(1);
        //when
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                        .builder()
                        .title(title)
                        .description(description)
                        .endDate(endDate)
                        .build()
        );
        //then
        assertAll("Task assertions",
                () -> assertThat(savedTask.title()).isEqualTo(title),
                () -> assertThat(savedTask.description()).isEqualTo(description),
                () -> assertThat(savedTask.isCompleted()).isEqualTo(false),
                () -> assertThat(savedTask.id()).isNotNull(),
                () -> assertTrue(endDate.isAfter(startDate))
        );
    }

    @Test
    public void should_throw_exception_if_endDate_isBefore_startDate() {
        //given
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.minusSeconds(1);
        //when
        Throwable endDateBeforeStartDate = assertThrows(
                EndDateBeforeStartDateException.class,
                () -> taskFacade.createTask(CreateTaskRequestDto
                        .builder()
                        .title(title)
                        .description(description)
                        .endDate(endDate)
                        .build())
        );
        //then
        assertThat(endDateBeforeStartDate).isInstanceOf(EndDateBeforeStartDateException.class);
        assertThat(endDateBeforeStartDate.getMessage()).isEqualTo("Provided invalid dates order");
    }

    @Test
    public void should_success_return_empty_list_after_list_tasks() {
        //when
        List<TaskDto> tasks = taskFacade.listTasks();
        //then
        assertThat(tasks).isEmpty();
    }

    @Test
    public void should_success_return_list_of_added_tasks() {
        //given
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                            .builder()
                            .title(title)
                            .description(description)
                            .endDate(endDate)
                            .build());
        //when
        List<TaskDto> tasks = taskFacade.listTasks();
        //then
        assertThat(tasks.get(0)).isEqualTo(savedTask);
    }

    @Test
    public void should_find_task_by_id() {
        //given
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                .builder()
                .title(title)
                .description(description)
                .endDate(endDate)
                .build());
        //when
        TaskDto foundTask = taskFacade.findById(savedTask.id());
        //then
        assertThat(foundTask).isEqualTo(savedTask);
    }

    @Test
    public void should_throw_exception_when_provided_invalid_id_in_findById() {
        //given
        String id = "NotExistingID";
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskFacade.findById(id));
        //then
        assertThat(taskNotFound).isInstanceOf(ResourceNotFound.class);
        assertThat(taskNotFound.getMessage()).isEqualTo("Task with provided id could not be found");
    }
}
