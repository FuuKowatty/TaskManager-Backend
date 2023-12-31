package pl.bartoszmech.domain.task;

import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.TaskResponseDto;

import java.time.LocalDateTime;

import static pl.bartoszmech.domain.task.TaskStatus.PENDING;

public class TaskMapper {
    public static TaskResponseDto mapFromTask(Task savedTask) {
        return TaskResponseDto.builder()
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

    public static Task mapToTask(TaskResponseDto taskDto) {
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

    public static TaskResponseDto mapFromCreateAndUpdateRequestDto(CreateAndUpdateTaskRequestDto requestedTask, LocalDateTime now) {
        return TaskResponseDto.builder()
                .title(requestedTask.title())
                .description(requestedTask.description())
                .status(PENDING)
                .startDate(now)
                .endDate(requestedTask.endDate())
                .completedAt(null)
                .assignedTo(requestedTask.assignedTo())
                .build();
    }
}