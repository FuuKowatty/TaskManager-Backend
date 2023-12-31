package pl.bartoszmech.infrastructure.task.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartoszmech.domain.task.repository.TaskRepository;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.domain.task.service.TaskServiceImpl;

import java.time.Clock;

@Configuration
public class TaskConfiguration {
    @Bean
    public TaskService createTaskService(TaskRepository repository, Clock clock) {
        return new TaskServiceImpl(repository, clock);
    }
}
