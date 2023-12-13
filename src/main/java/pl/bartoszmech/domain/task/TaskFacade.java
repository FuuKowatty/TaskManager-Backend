package pl.bartoszmech.domain.task;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TaskFacade {
    private final TaskRepository repository;
    private final Clock clock;
    private  static String INVALID_DATE_ORDER = "Provided invalid dates order";
    public Task createTask(CreateTaskRequestDto taskRequestDto) {
        LocalDateTime startDate = LocalDateTime.now(clock);
        LocalDateTime endDate = taskRequestDto.endDate();
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new EndDateBeforeStartDateException(INVALID_DATE_ORDER);
        }
        return repository.save(Task.builder()
                        .title(taskRequestDto.title())
                        .description(taskRequestDto.description())
                        .startDate(startDate)
                        .endDate(endDate)
                        .build());
    }

    public List<Task> listTasks() {
        return repository.findAll();
    }
}
