package pl.bartoszmech.domain.task;

import pl.bartoszmech.domain.task.dto.TaskDto;

import java.time.LocalDateTime;

class TaskMapper {
    static TaskDto mapFromTask(Task savedTask) {
        return TaskDto.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .isCompleted(savedTask.isCompleted())
                .startDate(reducePrecisionToSeconds(savedTask.getStartDate()))
                .endDate(reducePrecisionToSeconds(savedTask.getEndDate()))
                .assignedTo(savedTask.getAssignedTo())
                .build();
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