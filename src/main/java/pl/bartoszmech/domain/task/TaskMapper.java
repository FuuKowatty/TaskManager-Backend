package pl.bartoszmech.domain.task;

import pl.bartoszmech.domain.task.dto.TaskDto;

import java.time.LocalDateTime;

class TaskMapper {
    static TaskDto mapFromTask(Task savedTask) {
        return TaskDto.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .status(savedTask.getStatus())
                .startDate(reducePrecisionToSeconds(savedTask.getStartDate()))
                .endDate(reducePrecisionToSeconds(savedTask.getEndDate()))
                .completedAt(savedTask.getCompletedAt())
                .assignedTo(savedTask.getAssignedTo())
                .build();
    }

    static Task mapToTask(TaskDto taskDto) {
        return new Task(
                taskDto.id(),
                taskDto.title(),
                taskDto.description(),
                taskDto.status(),
                taskDto.startDate(),
                taskDto.endDate(),
                taskDto.completedAt(),
                taskDto.assignedTo()
        );
    }


    private static LocalDateTime reducePrecisionToSeconds(LocalDateTime dateTime) {
        return LocalDateTime.of(
                dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond()
        );
    }
}