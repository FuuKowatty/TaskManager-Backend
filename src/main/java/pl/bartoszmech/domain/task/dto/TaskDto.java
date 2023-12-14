package pl.bartoszmech.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        String id,
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String assignedTo
) {
}
