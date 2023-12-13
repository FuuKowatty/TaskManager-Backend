package pl.bartoszmech.domain.task;

import pl.bartoszmech.domain.task.dto.TaskDto;

public class TaskMapper {
    public static TaskDto mapFromTask(Task savedTask) {
        return TaskDto
                .builder()
                .id(savedTask.id())
                .title(savedTask.title())
                .description(savedTask.description())
                .isCompleted(savedTask.isCompleted())
                .startDate(savedTask.startDate())
                .endDate(savedTask.endDate())
                .build();
    }
}
