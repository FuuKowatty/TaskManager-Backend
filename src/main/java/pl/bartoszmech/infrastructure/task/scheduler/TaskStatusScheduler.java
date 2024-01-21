package pl.bartoszmech.infrastructure.task.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.bartoszmech.domain.task.service.TaskService;

@Component
@AllArgsConstructor
@Log4j2
public class TaskStatusScheduler  {

    private final TaskService service;

    @Scheduled(fixedDelayString = "${task.status.update.delay}")
    public void updateTaskStatus() {
        log.info("Updating task status");
        service.markAsFailedOutdatedTasks();
        log.info("Task status updated");
    }

}
