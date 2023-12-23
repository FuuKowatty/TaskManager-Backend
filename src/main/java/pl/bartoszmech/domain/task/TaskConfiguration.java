package pl.bartoszmech.domain.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartoszmech.infrastructure.clock.AdjustableClock;

@Configuration
public class TaskConfiguration {
    @Bean
    public TaskFacade createTaskFacade(TaskService taskService, AdjustableClock clock) {
        return new TaskFacade(taskService, clock);
    }
}
