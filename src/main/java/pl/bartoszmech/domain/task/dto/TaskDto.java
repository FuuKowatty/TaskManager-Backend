package pl.bartoszmech.domain.task.dto;

import lombok.Builder;
import pl.bartoszmech.domain.task.TaskStatus;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime completedAt,
        Long assignedTo
) {
}
