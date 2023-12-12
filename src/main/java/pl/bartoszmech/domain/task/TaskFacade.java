package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class TaskFacade {
    private final TaskRepository repository;

    public Task createTask(String title, String description, LocalDateTime endDate) {
        LocalDateTime startDate = LocalDateTime.now();
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            //error
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
