package pl.bartoszmech.domain.task;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.*;

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
        Task savedTask = taskFacade.createTask(title, description, endDate);
        //then
        assertAll("Task assertions",
                () -> assertThat(savedTask.title()).isEqualTo(title),
                () -> assertThat(savedTask.description()).isEqualTo(description),
                () -> assertThat(savedTask.isCompleted()).isEqualTo(false),
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
                () -> taskFacade.createTask(title, description, endDate)
        );
        //then
        assertThat(endDateBeforeStartDate).isInstanceOf(EndDateBeforeStartDateException.class);
        assertThat(endDateBeforeStartDate.getMessage()).isEqualTo("Provided invalid dates order");
    }


}
