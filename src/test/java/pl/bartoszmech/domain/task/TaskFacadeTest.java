package pl.bartoszmech.domain.task;

import org.junit.jupiter.api.Test;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.TaskResponseDto;
import pl.bartoszmech.domain.task.service.TaskServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.bartoszmech.domain.task.TaskStatus.COMPLETED;
import static pl.bartoszmech.domain.task.TaskStatus.FAILED;
import static pl.bartoszmech.domain.task.TaskStatus.PENDING;

public class TaskFacadeTest {
    private final AdjustableClock clock = new AdjustableClock(
            LocalDateTime.of(2014, 12, 22, 10, 15, 30).toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );
    TaskService taskService = new TaskServiceImpl(new TaskRepositoryTestImpl(), clock);
    @Test
    public void should_successfully_create_task() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusSeconds(1);
        //when
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto
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
                () -> assertThat(savedTask.status()).isEqualTo(PENDING),
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
                () -> taskService.createTask(CreateAndUpdateTaskRequestDto
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
        List<TaskResponseDto> tasks = taskService.listTasks();
        //then
        assertThat(tasks).isEmpty();
    }

    @Test
    public void should_success_return_list_of_two_tasks_after_add_them() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
                        .endDate(endDate)
                        .assignedTo(userId)
                        .build());
        TaskResponseDto savedTask2 = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(endDate)
                .assignedTo(userId+1)
                .build());

        //when
        List<TaskResponseDto> tasks = taskService.listTasks();
        //then
        assertThat(tasks.size()).isEqualTo(2);
        assertThat(tasks).containsExactlyInAnyOrder(
                savedTask,
                savedTask2
        );
    }

    @Test
    public void should_success_return_list_of_added_tasks() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.now(clock).plusSeconds(1);
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                            .title(title)
                            .description(description)
                            .endDate(endDate)
                            .assignedTo(userId)
                            .build());
        //when
        List<TaskResponseDto> tasks = taskService.listTasks();
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
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
                        .endDate(endDate)
                        .assignedTo(userId)
                        .build());
        //when
        TaskResponseDto foundTask = taskService.findById(savedTask.id());
        //then
        assertThat(foundTask).isEqualTo(savedTask);
    }

    @Test
    public void should_throw_exception_when_provided_invalid_id_in_findById() {
        //given
        long id = 999L;
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskService.findById(id));
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
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto
                .builder()
                .title(title)
                .description(description)
                .endDate(endDate)
                .assignedTo(userId)
                .build());
        //when
        TaskResponseDto deletedTask = taskService.deleteById(savedTask.id());
        //then
        assertThat(deletedTask).isEqualTo(savedTask);
        assertThat(taskService.listTasks()).isEmpty();
    }

    @Test
    public void should_throw_not_found_exception_when_client_provide_invalid_id_in_deleteById() {
        //given
        long id = 9999L;
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskService.deleteById(id));
        //then
        assertThat(taskNotFound.getMessage()).isEqualTo("Task with provided id could not be found");
    }

    @Test
    public void should_success_update_task() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusDays(1);
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto
                .builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        TaskResponseDto updatedTask = taskService.updateTask(savedTask.id(), CreateAndUpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
                        .endDate(endDate)
                        .assignedTo(userId)
                        .build());
        //then
        assertAll("Update task assertions",
                () -> assertThat(updatedTask.title()).isEqualTo(title),
                () -> assertThat(updatedTask.description()).isEqualTo(description),
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
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = startDate.plusDays(1);
        long id = 997L;
        taskService.createTask(CreateAndUpdateTaskRequestDto
                .builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> taskService.updateTask(id, CreateAndUpdateTaskRequestDto.builder()
                        .title(title)
                        .description(description)
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
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("dododod")
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());

        //when
        Throwable endDateBeforeStartDate = assertThrows(
                EndDateBeforeStartDateException.class,
                () -> taskService.updateTask(savedTask.id(), CreateAndUpdateTaskRequestDto
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
    public void should_throw_exception_if_assign_this_same_task_title_to_this_same_user_updateTask() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable duplicateUserTask = assertThrows(DuplicateUserTaskException.class,
                () -> taskService.updateTask(savedTask.id(), CreateAndUpdateTaskRequestDto.builder()
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
    public void should_throw_exception_if_assign_this_same_task_title_to_this_same_user_createTask() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description("fkiwfofwofwowf")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        Throwable duplicateUserTask = assertThrows(DuplicateUserTaskException.class,
                () -> taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
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
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        //when
        TaskResponseDto newTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                        .title("Rdaiodiod")
                        .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                        .endDate(LocalDateTime.now(clock).plusSeconds(3))
                        .assignedTo(userId)
                        .build());
        //then
        assertThat(savedTask.title()).isNotEqualTo(newTask.title());
        assertThat(savedTask.assignedTo()).isEqualTo(newTask.assignedTo());
    }

    @Test
    public void should_list_only_employee_tasks_by_id() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId+1)
                .build());
        taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId+2)
                .build());
        //when
        List<TaskResponseDto> foundTasks = taskService.listEmployeeTasks(userId);
        //then
        assertThat(foundTasks.size()).isEqualTo(1);
        assertThat(foundTasks.get(0).assignedTo()).isEqualTo(userId);
    }

    @Test
    public void should_success_mark_task_as_completed() {
        //given
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(997L)
                .build());
        //when
        String message = taskService.completeTask(savedTask.id());
        //then
        TaskResponseDto updatedTask = taskService.findById(savedTask.id());
        assertThat(updatedTask.status()).isEqualTo(COMPLETED);
        assertThat(message).isEqualTo("Task successfully completed");
        assertThat(updatedTask.completedAt()).isEqualTo(LocalDateTime.now(clock));
    }

    @Test
    public void should_return_task_already_completed_message_when_trying_complete_completed_task() {
        //given
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(997L)
                .build());
        taskService.completeTask(savedTask.id());
        //when
        String message = taskService.completeTask(savedTask.id());
        //then
        TaskResponseDto updatedTask = taskService.findById(savedTask.id());
        assertThat(updatedTask.status()).isEqualTo(COMPLETED);
        assertThat(message).isEqualTo("Task is already completed");
    }

    @Test
    public void should_return_task_already_failed_message_when_trying_complete_failed_task() {
        //given
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(997L)
                .build());
        clock.advanceInTimeBy(Duration.ofSeconds(2));
        taskService.markAsFailedOutdatedTasks();
        //when
        String message = taskService.completeTask(savedTask.id());
        //then
        TaskResponseDto updatedTask = taskService.findById(savedTask.id());
        assertThat(updatedTask.status()).isEqualTo(FAILED);
        assertThat(message).isEqualTo("Task is outdated");
        assertThat(updatedTask.completedAt()).isNull();
    }


    @Test
    public void should_list_only_employee_task() {
        //given
        long userId = 997L;
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        TaskResponseDto savedTask1 = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        TaskResponseDto savedTask2 = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title + "to make originally title")
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId)
                .build());
        taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title(title)
                .description(description)
                .endDate(LocalDateTime.now(clock).plusSeconds(1))
                .assignedTo(userId+1)
                .build());
        //when
        List<TaskResponseDto> foundTasks = taskService.listEmployeeTasks(userId);
        //then
        assertThat(taskService.listEmployeeTasks(userId).size()).isEqualTo(2);
        assertThat(foundTasks).containsExactlyInAnyOrder(
                savedTask1,
                savedTask2
        );
    }

    @Test
    public void should_mark_outdated_tasks_as_failed() {
        //given
        TaskResponseDto savedTask = taskService.createTask(CreateAndUpdateTaskRequestDto.builder()
                .title("RandomTitle")
                .description("dnjfouwfofw2r21  rr 32r r32 r2 3")
                .endDate(LocalDateTime.now(clock).plusDays(1))
                .assignedTo(997L)
                .build());

        //when
        clock.plusDaysAndMinutes(1, 1);
        taskService.markAsFailedOutdatedTasks();
        //then
        TaskResponseDto updatedTask = taskService.findById(savedTask.id());
        assertThat(updatedTask.status()).isEqualTo(FAILED);
    }
}
