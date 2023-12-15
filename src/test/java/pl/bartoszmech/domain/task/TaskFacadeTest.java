package pl.bartoszmech.domain.task;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;
import pl.bartoszmech.domain.task.dto.UpdateTaskRequestDto;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.*;

public class TaskFacadeTest {
    @Mock
    private Clock clock = Clock.fixed(
            LocalDateTime.of(2014, 12, 22, 10, 15, 30).toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );
    TaskFacade taskFacade = new TaskFacade(new TaskService(new TaskRepositoryTestImpl()), clock);

    @Test
    public void should_successfully_create_task() {
        //given
        long userId = 997L;
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
                        .assignedTo(userId)
                        .build()
        );
        //then
        assertAll("Task assertions",
                () -> assertThat(savedTask.title()).isEqualTo(title),
                () -> assertThat(savedTask.description()).isEqualTo(description),
                () -> assertThat(savedTask.isCompleted()).isEqualTo(false),
                () -> assertThat(savedTask.id()).isNotNull(),
                () -> assertTrue(endDate.isAfter(startDate)),
                () -> assertThat(savedTask.assignedTo()).isEqualTo(userId)
        );
    }

    @Test
    public void should_throw_exception_if_endDate_isBefore_startDate_in_createTask() {
        //given
        long userId = 997L;
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
                        .assignedTo(userId)
                        .build())
        );
        //then
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
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto.builder()
                            .title(title)
                            .description(description)
                            .endDate(endDate)
                            .assignedTo(userId)
                            .build());
        //when
        List<TaskDto> tasks = taskFacade.listTasks();
        //then
        assertThat(tasks.get(0)).isEqualTo(savedTask);
    }

    @Test
    public void should_find_task_by_id() {
        //given
        long userId = 999L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(endDate)
                .assignedTo(userId)
                .build());
        //when
        TaskDto foundTask = taskFacade.findById(savedTask.id());
        //then
        assertThat(foundTask).isEqualTo(savedTask);
    }

    @Test
    public void should_throw_exception_when_provided_invalid_id_in_findById() {
        //given
        long id = 999L;
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskFacade.findById(id));
        //then
        assertThat(taskNotFound.getMessage()).isEqualTo("Task with provided id could not be found");
    }

    @Test
    public void should_success_delete_task_by_id() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                .builder()
                .title(title)
                .description(description)
                .endDate(endDate)
                .assignedTo(userId)
                .build());
        //when
        TaskDto deletedTask = taskFacade.deleteById(savedTask.id());
        //then
        assertThat(deletedTask).isEqualTo(savedTask);
        assertThat(taskFacade.listTasks()).isEmpty();
    }

    @Test
    public void should_throw_not_found_exception_when_client_provide_invalid_id_in_deleteById() {
        //given
        long id = 9999L;
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskFacade.deleteById(id));
        //then
        assertThat(taskNotFound.getMessage()).isEqualTo("Task with provided id could not be found");
    }

    @Test
    public void should_success_update_task() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        boolean isCompleted = true;
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusDays(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                .builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        TaskDto updatedTask = taskFacade.updateTask(savedTask.id(), UpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
                        .isCompleted(isCompleted)
                        .endDate(endDate)
                        .assignedTo(userId)
                        .build());
        //then
        assertAll("Update task assertions",
                () -> assertThat(updatedTask.title()).isEqualTo(title),
                () -> assertThat(updatedTask.description()).isEqualTo(description),
                () -> assertThat(updatedTask.isCompleted()).isEqualTo(isCompleted),
                () -> assertThat(updatedTask.assignedTo()).isEqualTo(userId),
                () -> assertThat(updatedTask.id()).isNotNull(),
                () -> assertThat(updatedTask.id()).isEqualTo(savedTask.id()),
                () -> assertThat(updatedTask.endDate()).isNotEqualTo(savedTask.endDate()),
                () -> assertThat(updatedTask.startDate()).isEqualTo(startDate),
                () -> assertThat(updatedTask.endDate().isAfter(updatedTask.startDate()))
        );
    }

    @Test
    public void should_throw_exception_if_client_provide_invalid_id_in_updateTask() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        boolean isCompleted = true;
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusDays(1);
        long id = 997L;
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto
                .builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskFacade.updateTask(id, UpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
                        .isCompleted(isCompleted)
                        .endDate(endDate)
                        .assignedTo(userId)
                        .build()));
        //then
        assertThat(taskNotFound.getMessage()).isEqualTo("Task with provided id could not be found");
    }

    @Test
    public void should_throw_exception_if_endDate_isBefore_startDate_in_updateTask() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.minusSeconds(1);
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto.builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable endDateBeforeStartDate = assertThrows(
                EndDateBeforeStartDateException.class,
                () -> taskFacade.updateTask(savedTask.id(), UpdateTaskRequestDto
                        .builder()
                        .title(title)
                        .description(description)
                        .isCompleted(true)
                        .endDate(endDate)
                        .build())
        );
        //then
        assertThat(endDateBeforeStartDate.getMessage()).isEqualTo("Provided invalid dates order");
    }

    @Test
    public void should_throw_exception_if_assign_this_same_task_title_to_this_same_user() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        taskFacade.createTask(CreateTaskRequestDto.builder()
                .title(title)
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable duplicateUserTask = assertThrows(DuplicateUserTaskException.class,
                () -> taskFacade.createTask(CreateTaskRequestDto.builder()
                    .title(title)
                    .description(description)
                    .endDate(LocalDateTime.now(clock).plusSeconds(3))
                    .assignedTo(userId)
                    .build())
        );
        //then
        assertThat(duplicateUserTask.getMessage()).isEqualTo("Provided task is already assigned to this same user");
    }

    @Test
    public void should_success_create_task_with_same_title_but_assigned_to_other_user() {
        //given
        long userId = 997L;
        TaskDto savedTask = taskFacade.createTask(CreateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        TaskDto newTask = taskFacade.createTask(CreateTaskRequestDto.builder()
                        .title("Rdaiodiod")
                        .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                        .endDate(LocalDateTime.now(clock).plusSeconds(3))
                        .assignedTo(userId)
                        .build());
        //then
        assertThat(savedTask.title()).isNotEqualTo(newTask.title());
        assertThat(savedTask.assignedTo()).isEqualTo(newTask.assignedTo());
    }
}
