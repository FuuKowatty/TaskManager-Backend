package pl.bartoszmech.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateTaskRequestDto(
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime endDate,
        Long assignedTo
) {
}
