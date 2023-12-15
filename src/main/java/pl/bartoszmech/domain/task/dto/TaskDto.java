package pl.bartoszmech.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        Long id,
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long assignedTo
) {
}
