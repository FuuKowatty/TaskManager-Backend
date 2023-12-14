package pl.bartoszmech.domain.task;

import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;

class TaskMapper {
    static TaskDto mapFromTask(Task savedTask) {
        return TaskDto
                .builder()
                .id(savedTask.id())
                .title(savedTask.title())
                .description(savedTask.description())
                .isCompleted(savedTask.isCompleted())
                .startDate(savedTask.startDate())
                .endDate(savedTask.endDate())
                .assignedTo(savedTask.assignedTo())
                .build();
    }
}
