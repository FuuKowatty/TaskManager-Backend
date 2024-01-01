package pl.bartoszmech.infrastructure.task.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="task.schedule.enabled", matchIfMissing = true)
public class SchedulerConfig {
}
