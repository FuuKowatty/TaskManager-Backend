package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@AllArgsConstructor
public class TaskFacade {
    private final TaskRepository repository;
    private final Clock clock;
    private  static String INVALID_DATE_ORDER = "Provided invalid dates order";
    public Task createTask(String title, String description, LocalDateTime endDate) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }
        return repository.save(Task.builder()
                        .title(title)
                        .description(description)
                        .startDate(startDate)
                        .endDate(endDate)
                        .isCompleted(false)
                        .build());
    }
}
