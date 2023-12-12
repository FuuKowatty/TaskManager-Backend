package pl.bartoszmech.domain.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TaskFacadeTest {
    TaskFacade taskFacade = new TaskFacade(new TaskRepositoryTestImpl());

    @Test
    public void should_successfully_create_task() {
        //given
        String title = "RandomTitle";
        String description = "dnjfouwfofw2r21  rr 32r r32 r2 3";
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        //when
        taskFacade.createTask(title, description, endDate);

    }
}
