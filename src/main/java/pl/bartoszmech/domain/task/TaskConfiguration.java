package pl.bartoszmech.domain.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TaskConfiguration {
    @Bean
    public TaskFacade createTaskFacade(TaskService taskService, Clock clock) {
        return new TaskFacade(taskService, clock);
    }
}
